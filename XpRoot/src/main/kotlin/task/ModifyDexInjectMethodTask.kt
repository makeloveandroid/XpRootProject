package task

import NewBuilderInstructionUtils
import kotlinx.coroutines.*
import org.jf.baksmali.Adaptors.ClassDefinition
import org.jf.baksmali.BaksmaliOptions
import org.jf.baksmali.formatter.BaksmaliWriter
import org.jf.dexlib2.*
import org.jf.dexlib2.base.reference.BaseMethodReference
import org.jf.dexlib2.builder.BuilderInstruction
import org.jf.dexlib2.builder.MutableMethodImplementation
import org.jf.dexlib2.builder.instruction.*
import org.jf.dexlib2.dexbacked.DexBackedDexFile
import org.jf.dexlib2.dexbacked.raw.CodeItem
import org.jf.dexlib2.iface.*
import org.jf.dexlib2.iface.Annotation
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction
import org.jf.dexlib2.iface.instruction.ReferenceInstruction
import org.jf.dexlib2.iface.instruction.formats.Instruction31c
import org.jf.dexlib2.immutable.reference.ImmutableMethodReference
import org.jf.dexlib2.immutable.reference.ImmutableStringReference
import org.jf.dexlib2.rewriter.*
import org.jf.dexlib2.writer.io.FileDataStore
import org.jf.dexlib2.writer.pool.DexPool
import util.Log
import java.io.File
import java.io.StringWriter
import java.util.*

class ModifyDexInjectMethodTask(val unZipFile: File, val virus: String) :
    Task<File, File>() {
    var intcount = 0

    override fun execute(): File = runBlocking<File> {
        // 读取需要注入的方法
        val readInjectConfig = readInjectConfig()
        if (readInjectConfig.isEmpty()) {
            Log.d("ModifyDexInjectMethodTask", "无需注入Trace方法")
            return@runBlocking unZipFile
        }
        // 注入方法
        injectMethod(readInjectConfig)

        return@runBlocking unZipFile
    }

    private fun readInjectConfig(): List<String> {
        try {
            val parent = File(virus).parent
            return File(parent, "injectMethod.config").apply {
                Log.d("ModifyDexInjectMethodTask", "配置路径${absolutePath}")
                if (!exists()) {
                    createNewFile()
                }
            }.readText().split("\n")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return emptyList()
    }

    private suspend fun CoroutineScope.injectMethod(configs: List<String>) {
        // 寻找  Application 的 Dex
        //  过滤 dex 文件
        val dexs = unZipFile.listFiles { dir, name -> name.endsWith(".dex") }
        val jobs = mutableListOf<Job>()
        // 多线程查找
        for (dex in dexs) {
            val childJob = launch(Dispatchers.IO) {
                val dexFile = injectDex(dex.absolutePath, configs)

                val dexPool = DexPool(dexFile.opcodes)

                // 写入new dex
                for (classDef in dexFile.classes) {


                    dexPool.internClass(classDef)
                }

                // 删除原来的
                dex.delete()
                // 从新写入
                dexPool.writeTo(FileDataStore(dex));
            }

            jobs.add(childJob)
        }

        jobs.joinAll()
    }

    private fun toJavaPkg(pkg: String): String {
        return pkg.removeRange(0, 1).replace("/", ".").replace(";->", ".")
    }

    private fun classToSmali(classDef: ClassDef): String {
        val options = BaksmaliOptions()
        options.deodex = false
        options.implicitReferences = false
        options.parameterRegisters = true
        options.localsDirective = true
        options.sequentialLabels = true
        options.debugInfo = true
        options.codeOffsets = false
        options.accessorComments = false
        options.registerInfo = 0
        options.inlineResolver = null

        val classDefinition =
            ClassDefinition(options, classDef)
        val stringWriter = StringWriter()
        val indentingWriter = BaksmaliWriter(stringWriter,"")
        classDefinition.writeTo(indentingWriter)
        return stringWriter.toString()
    }

    override fun complete(result: File) {
        Log.d("ModifyDexInjectMethodTask", "操作完成")
    }

    private fun injectDex(path: String, configs: List<String>): DexFile {
        val dexBackedDexFile =
            DexFileFactory.loadDexFile(File(path), Opcodes.getDefault())
        val dexWriter = DexRewriter(object : RewriterModule() {
            override fun getMethodRewriter(rewriters: Rewriters): Rewriter<Method> {
                return Rewriter { method ->
                    val result = configs.stream().filter { configS ->
                        toJavaPkg(method.toString()).matches(Regex(configS))
                    }.findFirst()
                    if (result.isEmpty) {
                        return@Rewriter method
                    }
                    intcount++
                    Log.t("提示", "注入方法$method  注入总数:$intcount")
                    InjectRewrittenMethod(rewriters, method)
                }
            }

            override fun getClassDefRewriter(rewriters: Rewriters): Rewriter<ClassDef> {
                val classDef = super.getClassDefRewriter(rewriters)
                return classDef
            }
        })
        return dexWriter.dexFileRewriter.rewrite(dexBackedDexFile)
    }


    class InjectRewrittenMethod(private var rewriters: Rewriters, var method: Method) :
        BaseMethodReference(), Method {
        var injectMethodImplementation: MethodImplementation? = null

        override fun getDefiningClass(): String {
            return rewriters.methodReferenceRewriter.rewrite(method).definingClass
        }

        override fun getName(): String {
            return rewriters.methodReferenceRewriter.rewrite(method).name
        }

        override fun getParameterTypes(): List<CharSequence?> {
            return rewriters.methodReferenceRewriter.rewrite(method).parameterTypes
        }

        override fun getParameters(): List<MethodParameter> {
            return RewriterUtils.rewriteList(rewriters.methodParameterRewriter, method.parameters)
        }

        override fun getReturnType(): String {
            return rewriters.methodReferenceRewriter.rewrite(method).returnType
        }

        override fun getAccessFlags(): Int {
            return method.accessFlags
        }

        override fun getAnnotations(): Set<Annotation> {
            return RewriterUtils.rewriteSet(rewriters.annotationRewriter, method.annotations)
        }

        override fun getHiddenApiRestrictions(): Set<HiddenApiRestriction> {
            return method.hiddenApiRestrictions
        }

        override fun getImplementation(): MethodImplementation? {
            var brek = AccessFlags.ABSTRACT.isSet(accessFlags) || AccessFlags.INTERFACE.isSet(accessFlags) || AccessFlags.CONSTRUCTOR.isSet(accessFlags)

            val rewriteNullable = RewriterUtils.rewriteNullable(
                rewriters.methodImplementationRewriter,
                method.implementation
            )
            if (brek) return rewriteNullable
            if (injectMethodImplementation != null) {
                return injectMethodImplementation
            }
            if (rewriteNullable != null) {
                val addRegisterCount = 1
                // 增加 XpRoot.start() 方法
                val mutableMethodImplementation = object : MutableMethodImplementation(rewriteNullable) {
                    val addRegisterCount = 1
                    override fun getRegisterCount(): Int {
                        val localCount = getLocalCountCount()
                        return if (localCount >= addRegisterCount) {
                            super.getRegisterCount()
                        } else {
                            super.getRegisterCount() + (addRegisterCount - localCount)
                        }
                    }

                    fun getLocalCountCount(): Int {
                        try {
                            val ins = getInsSize()
                            return super.getRegisterCount() - ins
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        val isStatic = AccessFlags.STATIC.isSet(accessFlags)
                        val localCount = if (isStatic) {
                            super.getRegisterCount() - method.parameters.size
                        } else {
                            super.getRegisterCount() - method.parameters.size - 1
                        }
                        return localCount
                    }

                    public fun getInsSize(): Int {
                        val dexFile: DexBackedDexFile = method::class.java.getDeclaredField("dexFile").let {
                            it.isAccessible = true
                            it.get(method)
                        } as DexBackedDexFile

                        val codeOffset: Int = method::class.java.getDeclaredField("codeOffset").let {
                            it.isAccessible = true
                            it.get(method)
                        } as Int
                        val ins = dexFile.dataBuffer.readUshort(codeOffset + CodeItem.INS_OFFSET)
                        return ins
                    }
                }
//                // 参数 todo
                if (mutableMethodImplementation.getInsSize() >= 15) {
                    return rewriteNullable
                }

                val startRegister = mutableMethodImplementation.registerCount - parameters.size
                val isStatic = AccessFlags.STATIC.isSet(accessFlags)
                val isCons = AccessFlags.CONSTRUCTOR.isSet(accessFlags)

                val instructions = mutableListOf<BuilderInstruction>()
                var str = method.toString()
                if (str.length > 127) {
                    str = str.substring(0, 127);
                }

                instructions.add(
                    BuilderInstruction21c(
                        Opcode.CONST_STRING,
                        0,
                        ImmutableStringReference(str)
                    )
                )
                instructions.add(
                    BuilderInstruction35c(
                        Opcode.INVOKE_STATIC, 1, 0, 0, 0, 0, 0,
                        ImmutableMethodReference(
                            "Landroid/os/Trace;",
                            "beginSection",
                            mutableListOf("Ljava/lang/String;"),
                            "V"
                        )
                    )
                )

                // 所有寄存器  偏移 1
                if (mutableMethodImplementation.getLocalCountCount() < addRegisterCount) {
                    // 出现寄存器增加情况,将P变量向下移动
                    println("增加寄存器触发:${method}")
/*
                    val localCountCount = mutableMethodImplementation.getLocalCountCount()

                    val off = if (isStatic) {
                        0
                    } else {
                        // 先移动p0
                        instructions.add(
                            BuilderInstruction22x(
                                Opcode.MOVE_OBJECT_FROM16,
                                localCountCount,
                                localCountCount + 1
                            )
                        )
                        1
                    }
                    for (i in parameters.indices) {
                        println(parameters[i].type)
                        if (parameters[i].type.endsWith(";")) {
                            // 对象
                            instructions.add(
                                BuilderInstruction22x(
                                    Opcode.MOVE_OBJECT_FROM16,
                                    localCountCount + i + off,
                                    localCountCount + i + 1 + off
                                )
                            )
                        } else {
                            // 基本数据类型
                            instructions.add(
                                BuilderInstruction22x(
                                    Opcode.MOVE_WIDE_FROM16,
                                    localCountCount + i + off,
                                    localCountCount + i + 1 + off
                                )
                            )
                        }

                    }
*/

                    val newBuilderInstructionUtils =
                        NewBuilderInstructionUtils(addRegisterCount - mutableMethodImplementation.getLocalCountCount())
                    mutableMethodImplementation.instructions.forEachIndexed { index, ins ->
                        mutableMethodImplementation.replaceInstruction(
                            index,
                            newBuilderInstructionUtils.offInstruction(ins)
                        )
                    }
                }



                val insertIndex = TreeSet<Int>()

                // 查找所有方法 return 指令
                mutableMethodImplementation.instructions.forEachIndexed { index, ins ->
                    if (ins.opcode == Opcode.RETURN_VOID || ins.opcode == Opcode.RETURN
                        || ins.opcode == Opcode.RETURN_OBJECT || ins.opcode == Opcode.RETURN_WIDE
                        || ins.opcode == Opcode.RETURN_VOID_BARRIER || ins.opcode == Opcode.RETURN_VOID_BARRIER
                    ) {
                        insertIndex.add(index)
                    }
                }
                insertIndex.reversed().forEach {
                    val oldReturn = mutableMethodImplementation.instructions[it]

                    // 插入结束
                    mutableMethodImplementation.replaceInstruction(
                        it,
                        BuilderInstruction35c(
                            Opcode.INVOKE_STATIC, 0, 0, 0, 0, 0, 0,
                            ImmutableMethodReference(
                                "Landroid/os/Trace;",
                                "endSection",
                                null,
                                "V"
                            )
                        )
                    )
                    // 插入原来的return
                    val buidler = NewBuilderInstructionUtils(0)
                    mutableMethodImplementation.addInstruction(it + 1, buidler.offInstruction(oldReturn))

                }
                // 如果是构造方法 跳过第一条super指令
                instructions.reversed().forEach { mutableMethodImplementation.addInstruction(0, it) }


                injectMethodImplementation = mutableMethodImplementation

                return mutableMethodImplementation
            }


            return rewriteNullable
        }


    }

}