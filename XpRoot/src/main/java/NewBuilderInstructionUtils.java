import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.MethodLocation;
import org.jf.dexlib2.builder.SwitchLabelElement;
import org.jf.dexlib2.builder.instruction.*;
import org.jf.dexlib2.iface.instruction.SwitchElement;
import org.jf.dexlib2.iface.instruction.formats.*;
import util.Log;

import java.lang.reflect.Field;
import java.util.List;

public class NewBuilderInstructionUtils {
    /**
     * 参数偏移量
     */
    private final int off;

    public NewBuilderInstructionUtils(int off) {
        this.off = off;
    }

    public BuilderInstruction offInstruction(BuilderInstruction instruction) {
        switch (instruction.getOpcode().format) {
            case Format10t:
                return newBuilderInstruction10t((BuilderInstruction10t) instruction);
            case Format10x:
                return newBuilderInstruction10x((BuilderInstruction10x) instruction);
            case Format11n:
                return newBuilderInstruction11n((BuilderInstruction11n) instruction);
            case Format11x:
                return newBuilderInstruction11x((BuilderInstruction11x) instruction);
            case Format12x:
                return newBuilderInstruction12x((BuilderInstruction12x) instruction);
            case Format20bc:
                return newBuilderInstruction20bc((BuilderInstruction20bc) instruction);
            case Format20t:
                return newBuilderInstruction20t((BuilderInstruction20t) instruction);
            case Format21c:
                return newBuilderInstruction21c((BuilderInstruction21c) instruction);
            case Format21ih:
                return newBuilderInstruction21ih((BuilderInstruction21ih) instruction);
            case Format21lh:
                return newBuilderInstruction21lh((BuilderInstruction21lh) instruction);
            case Format21s:
                return newBuilderInstruction21s((BuilderInstruction21s) instruction);
            case Format21t:
                return newBuilderInstruction21t((BuilderInstruction21t) instruction);
            case Format22b:
                return newBuilderInstruction22b((BuilderInstruction22b) instruction);
            case Format22c:
                return newBuilderInstruction22c((BuilderInstruction22c) instruction);
            case Format22cs:
                return newBuilderInstruction22cs((BuilderInstruction22cs) instruction);
            case Format22s:
                return newBuilderInstruction22s((BuilderInstruction22s) instruction);
            case Format22t:
                return newBuilderInstruction22t((BuilderInstruction22t) instruction);
            case Format22x:
                return newBuilderInstruction22x((BuilderInstruction22x) instruction);
            case Format23x:
                return newBuilderInstruction23x((BuilderInstruction23x) instruction);
            case Format30t:
                return newBuilderInstruction30t((BuilderInstruction30t) instruction);
            case Format31c:
                return newBuilderInstruction31c((BuilderInstruction31c) instruction);
            case Format31i:
                return newBuilderInstruction31i((BuilderInstruction31i) instruction);
            case Format31t:
                return newBuilderInstruction31t((BuilderInstruction31t) instruction);
            case Format32x:
                return newBuilderInstruction32x((BuilderInstruction32x) instruction);
            case Format35c:
                return newBuilderInstruction35c((BuilderInstruction35c) instruction);
            case Format35mi:
                return newBuilderInstruction35mi((BuilderInstruction35mi) instruction);
            case Format35ms:
                return newBuilderInstruction35ms((BuilderInstruction35ms) instruction);
            case Format3rc:
                return newBuilderInstruction3rc((BuilderInstruction3rc) instruction);
            case Format3rmi:
                return newBuilderInstruction3rmi((BuilderInstruction3rmi) instruction);
            case Format3rms:
                return newBuilderInstruction3rms((BuilderInstruction3rms) instruction);
            case Format51l:
                return newBuilderInstruction51l((BuilderInstruction51l) instruction);
            case PackedSwitchPayload:
                return newBuilderPackedSwitchPayload((BuilderPackedSwitchPayload) instruction);
            case SparseSwitchPayload:
                return newBuilderSparseSwitchPayload((BuilderSparseSwitchPayload) instruction);
            case ArrayPayload:
                return newBuilderArrayPayload((ArrayPayload) instruction);
            default:
                Log.d("修改指令", "Instruction format %s not supported" + instruction.getOpcode().format);
                return instruction;
        }
    }


    private BuilderInstruction10t newBuilderInstruction10t(BuilderInstruction10t instruction) {
        return new BuilderInstruction10t(instruction.getOpcode(), instruction.getTarget());
    }


    private BuilderInstruction10x newBuilderInstruction10x(BuilderInstruction10x instruction) {
        return new BuilderInstruction10x(instruction.getOpcode());
    }


    private BuilderInstruction11n newBuilderInstruction11n(Instruction11n instruction) {
        return new BuilderInstruction11n(
                instruction.getOpcode(),
                instruction.getRegisterA() + off,
                instruction.getNarrowLiteral());
    }


    private BuilderInstruction11x newBuilderInstruction11x(Instruction11x instruction) {
        return new BuilderInstruction11x(
                instruction.getOpcode(),
                instruction.getRegisterA() + off);
    }


    private BuilderInstruction12x newBuilderInstruction12x(Instruction12x instruction) {
        return new BuilderInstruction12x(
                instruction.getOpcode(),
                instruction.getRegisterA() + off,
                instruction.getRegisterB() + off);
    }


    private BuilderInstruction20bc newBuilderInstruction20bc(BuilderInstruction20bc instruction) {
        return new BuilderInstruction20bc(instruction.getOpcode(), instruction.getVerificationError(), instruction.getReference());
    }


    private BuilderInstruction20t newBuilderInstruction20t(BuilderInstruction20t instruction) {
        return new BuilderInstruction20t(instruction.getOpcode(), instruction.getTarget());
    }


    private BuilderInstruction21c newBuilderInstruction21c(Instruction21c instruction) {
        return new BuilderInstruction21c(
                instruction.getOpcode(),
                instruction.getRegisterA() + off,
                instruction.getReference());
    }


    private BuilderInstruction21ih newBuilderInstruction21ih(Instruction21ih instruction) {
        return new BuilderInstruction21ih(
                instruction.getOpcode(),
                instruction.getRegisterA() + off,
                instruction.getNarrowLiteral());
    }


    private BuilderInstruction21lh newBuilderInstruction21lh(Instruction21lh instruction) {
        return new BuilderInstruction21lh(
                instruction.getOpcode(),
                instruction.getRegisterA() + off,
                instruction.getWideLiteral());
    }


    private BuilderInstruction21s newBuilderInstruction21s(Instruction21s instruction) {
        return new BuilderInstruction21s(
                instruction.getOpcode(),
                instruction.getRegisterA() + off,
                instruction.getNarrowLiteral());
    }


    private BuilderInstruction21t newBuilderInstruction21t(BuilderInstruction21t instruction) {
        return new BuilderInstruction21t(
                instruction.getOpcode(),
                instruction.getRegisterA() + off,
                instruction.getTarget());
    }


    private BuilderInstruction22b newBuilderInstruction22b(Instruction22b instruction) {
        return new BuilderInstruction22b(
                instruction.getOpcode(),
                instruction.getRegisterA() + off,
                instruction.getRegisterB() + off,
                instruction.getNarrowLiteral());
    }


    private BuilderInstruction22c newBuilderInstruction22c(Instruction22c instruction) {
        return new BuilderInstruction22c(
                instruction.getOpcode(),
                instruction.getRegisterA() + off,
                instruction.getRegisterB() + off,
                instruction.getReference());
    }


    private BuilderInstruction22cs newBuilderInstruction22cs(Instruction22cs instruction) {
        return new BuilderInstruction22cs(
                instruction.getOpcode(),
                instruction.getRegisterA() + off,
                instruction.getRegisterB() + off,
                instruction.getFieldOffset());
    }


    private BuilderInstruction22s newBuilderInstruction22s(Instruction22s instruction) {
        return new BuilderInstruction22s(
                instruction.getOpcode(),
                instruction.getRegisterA() + off,
                instruction.getRegisterB() + off,
                instruction.getNarrowLiteral());
    }


    private BuilderInstruction22t newBuilderInstruction22t(BuilderInstruction22t instruction) {
        return new BuilderInstruction22t(
                instruction.getOpcode(),
                instruction.getRegisterA() + off,
                instruction.getRegisterB() + off,
                instruction.getTarget());
    }


    private BuilderInstruction22x newBuilderInstruction22x(Instruction22x instruction) {
        return new BuilderInstruction22x(
                instruction.getOpcode(),
                instruction.getRegisterA() + off,
                instruction.getRegisterB() + off);
    }


    private BuilderInstruction23x newBuilderInstruction23x(Instruction23x instruction) {
        return new BuilderInstruction23x(
                instruction.getOpcode(),
                instruction.getRegisterA() + off,
                instruction.getRegisterB() + off,
                instruction.getRegisterC() + off);
    }


    private BuilderInstruction30t newBuilderInstruction30t(BuilderInstruction30t instruction) {
        return new BuilderInstruction30t(instruction.getOpcode(), instruction.getTarget());
    }


    private BuilderInstruction31c newBuilderInstruction31c(Instruction31c instruction) {
        return new BuilderInstruction31c(
                instruction.getOpcode(),
                instruction.getRegisterA() + off,
                instruction.getReference());
    }


    private BuilderInstruction31i newBuilderInstruction31i(Instruction31i instruction) {
        return new BuilderInstruction31i(
                instruction.getOpcode(),
                instruction.getRegisterA() + off,
                instruction.getNarrowLiteral());
    }


    private BuilderInstruction31t newBuilderInstruction31t(BuilderInstruction31t instruction) {
        return new BuilderInstruction31t(
                instruction.getOpcode(),
                instruction.getRegisterA() + off,
                instruction.getTarget());
    }


    private BuilderInstruction32x newBuilderInstruction32x(Instruction32x instruction) {
        return new BuilderInstruction32x(
                instruction.getOpcode(),
                instruction.getRegisterA() + off,
                instruction.getRegisterB() + off);
    }


    private BuilderInstruction35c newBuilderInstruction35c(Instruction35c instruction) {
        return new BuilderInstruction35c(
                instruction.getOpcode(),
                instruction.getRegisterCount(),
                instruction.getRegisterC() + off,
                instruction.getRegisterD() + off,
                instruction.getRegisterE() + off,
                instruction.getRegisterF() + off,
                instruction.getRegisterG() + off,
                instruction.getReference());
    }


    private BuilderInstruction35mi newBuilderInstruction35mi(Instruction35mi instruction) {
        return new BuilderInstruction35mi(
                instruction.getOpcode(),
                instruction.getRegisterCount(),
                instruction.getRegisterC() + off,
                instruction.getRegisterD() + off,
                instruction.getRegisterE() + off,
                instruction.getRegisterF() + off,
                instruction.getRegisterG() + off,
                instruction.getInlineIndex());
    }


    private BuilderInstruction35ms newBuilderInstruction35ms(Instruction35ms instruction) {
        return new BuilderInstruction35ms(
                instruction.getOpcode(),
                instruction.getRegisterCount(),
                instruction.getRegisterC() + off,
                instruction.getRegisterD() + off,
                instruction.getRegisterE() + off,
                instruction.getRegisterF() + off,
                instruction.getRegisterG() + off,
                instruction.getVtableIndex());
    }


    private BuilderInstruction3rc newBuilderInstruction3rc(Instruction3rc instruction) {
        return new BuilderInstruction3rc(
                instruction.getOpcode(),
                instruction.getStartRegister() + off,
                instruction.getRegisterCount(),
                instruction.getReference());
    }


    private BuilderInstruction3rmi newBuilderInstruction3rmi(Instruction3rmi instruction) {
        return new BuilderInstruction3rmi(
                instruction.getOpcode(),
                instruction.getStartRegister() + off,
                instruction.getRegisterCount(),
                instruction.getInlineIndex());
    }


    private BuilderInstruction3rms newBuilderInstruction3rms(Instruction3rms instruction) {
        return new BuilderInstruction3rms(
                instruction.getOpcode(),
                instruction.getStartRegister() + off,
                instruction.getRegisterCount(),
                instruction.getVtableIndex());
    }


    private BuilderInstruction51l newBuilderInstruction51l(Instruction51l instruction) {
        return new BuilderInstruction51l(
                instruction.getOpcode(),
                instruction.getRegisterA() + off,
                instruction.getWideLiteral());
    }


    private BuilderSparseSwitchPayload newBuilderSparseSwitchPayload(BuilderSparseSwitchPayload instruction) {
        BuilderSparseSwitchPayload builderSparseSwitchPayload = new BuilderSparseSwitchPayload(null);
        try {
            Field switchElements = BuilderSparseSwitchPayload.class.getDeclaredField("switchElements");
            switchElements.setAccessible(true);
            switchElements.set(builderSparseSwitchPayload, instruction.getSwitchElements());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builderSparseSwitchPayload;
    }

    private BuilderArrayPayload newBuilderArrayPayload(ArrayPayload instruction) {
        return new BuilderArrayPayload(instruction.getElementWidth(), instruction.getArrayElements());
    }

    private BuilderPackedSwitchPayload newBuilderPackedSwitchPayload(BuilderPackedSwitchPayload instruction) {
        BuilderPackedSwitchPayload builderPackedSwitchPayload = new BuilderPackedSwitchPayload(instruction.getSwitchElements().get(0).getKey(), null);
        try {
            Field switchElements = BuilderPackedSwitchPayload.class.getDeclaredField("switchElements");
            switchElements.setAccessible(true);
            switchElements.set(builderPackedSwitchPayload, instruction.getSwitchElements());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builderPackedSwitchPayload;
    }
}
