package cc.c0ldcat.autorun.utils;

import android.content.Context;
import de.robv.android.xposed.XposedHelpers;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;

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
