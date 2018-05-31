package cc.c0ldcat.autorun.utils;

import de.robv.android.xposed.XposedHelpers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectHelper {
    public static Field getPrivateField(Class<?> cls, String fieldName) throws NoSuchFieldError {
        return XposedHelpers.findField(cls, fieldName);
    }

    public static Object getPrivateObject(Class<?> cls, String fieldName, Object obj) throws NoSuchFieldError, IllegalAccessException {
        return getPrivateField(cls, fieldName).get(obj);
    }

    public static Method getPrivateMethod(Class<?> cls, String methodName, Class<?> ...params) throws NoSuchMethodError {
        return XposedHelpers.findMethodBestMatch(cls, methodName, params);
    }
}
