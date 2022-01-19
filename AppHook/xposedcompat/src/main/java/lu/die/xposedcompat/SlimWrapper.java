package lu.die.xposedcompat;

import com.swift.sandhook.wrapper.HookWrapper;

import java.lang.reflect.Member;
import java.lang.reflect.Method;

import de.robv.android.xposed.XposedBridge;

public class SlimWrapper {
    public static Boolean debugModeEnabled = Boolean.FALSE;
    public static boolean hookMethod(HookWrapper.HookEntity entity)
    {
        try{
            // Here, change to your project debuggable
            Boolean isDebug = debugModeEnabled;
            Class<?> slimHookClazz = XposedBridge.class.getClassLoader()
                    .loadClass("lu.die.shook.SlimHook");
            Method slimHookSetDebug = slimHookClazz.getMethod("setDebug", Boolean.class);
            slimHookSetDebug.invoke(null, isDebug);
        }catch (Exception ignored)
        {
        }
        try{
            final Class<?> clazzMember = Member.class;
            final Class<?> clazzMethod = Method.class;
            Class<?> slimHookClazz = XposedBridge.class.getClassLoader()
                    .loadClass("lu.die.shook.SlimHook");
            Method slimHookHookMethod = slimHookClazz.getMethod("hookMethod", clazzMember, clazzMethod, clazzMethod);
            Object result = slimHookHookMethod.invoke(null, entity.target, entity.hook, entity.backup);
            if(Boolean.class.isInstance(result))
            {
                return (Boolean)result;
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
}
