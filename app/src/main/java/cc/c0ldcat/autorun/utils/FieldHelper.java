package cc.c0ldcat.autorun.utils;

import java.lang.reflect.Field;

public class FieldHelper {
    public static Field getPrivateField(Class<?> cls, String fieldName) throws NoSuchFieldException {
        Field field = cls.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field;
    }

    public static Object getPrivateObject(Class<?> cls, String fieldName, Object obj) throws NoSuchFieldException, IllegalAccessException {
        return getPrivateField(cls, fieldName).get(obj);
    }
}
