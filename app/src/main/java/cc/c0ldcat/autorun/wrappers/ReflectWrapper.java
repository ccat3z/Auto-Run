package cc.c0ldcat.autorun.wrappers;

import cc.c0ldcat.autorun.utils.ReflectHelper;

import java.lang.reflect.Method;

public class ReflectWrapper {
    private Object object;

    public ReflectWrapper(Object object) {
        this.object = object;
    }

    public Object getAttribute(String name) throws NoSuchFieldException, IllegalAccessException {
        return ReflectHelper.getPrivateObject(getObjectClass(), name, object);
    }

    public Method getMethod(String methodName, Class<?> ...params) throws NoSuchMethodException {
        return ReflectHelper.getPrivateMethod(getObjectClass(), methodName, params);
    }

    public Class<?> getObjectClass() {
        return object.getClass();
    }

    public Object getObject() {
        return object;
    }

}
