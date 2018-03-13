package cc.c0ldcat.autorun.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectHelper {
    public static Field getPrivateField(Class<?> cls, String fieldName) throws NoSuchFieldException {
        Field field = cls.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    public static Object getPrivateObject(Class<?> cls, String fieldName, Object obj) throws NoSuchFieldException, IllegalAccessException {
        return getPrivateField(cls, fieldName).get(obj);
    }

    public static Method getPrivateMethod(Class<?> cls, String methodName, Class<?> ...params) throws NoSuchMethodException {
        Method method = cls.getMethod(methodName, params);
        method.setAccessible(true);
        return method;
    }
}
