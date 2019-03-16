package cc.c0ldcat.autorun.wrappers;

import cc.c0ldcat.autorun.utils.CommonUtils;
import cc.c0ldcat.autorun.utils.LogUtils;
import cc.c0ldcat.autorun.utils.ReflectHelper;
import de.robv.android.xposed.XposedHelpers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class ReflectWrapper<T extends ReflectWrapper> {
    private Object object;

    abstract public String getClassName();

    public T setObject(Object object) {
        this.object = object;
        return (T) this;
    }

    public Object getAttribute(String name) throws NoSuchFieldException, IllegalAccessException {
        return ReflectHelper.getPrivateObject(getObjectClass(), name, object);
    }

    public Object getAttributeIfExist(String name) {
        try {
            return getAttribute(name);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }

    public Method getMethod(String methodName, Class<?> ...params) throws NoSuchMethodException {
        return ReflectHelper.getPrivateMethod(getObjectClass(), methodName, params);
    }

    public Method getMethod(ClassLoader classLoader, String methodName, Class<?> ...params) throws NoSuchMethodException {
        return ReflectHelper.getPrivateMethod(getObjectClass(classLoader), methodName, params);
    }

    public Object invokeMethodIfAccessable(String methodName, Object ...params) {
        try {
            return invokeMethod(methodName, params);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            LogUtils.e(CommonUtils.exceptionStacktraceToString(e));
            return null;
        }
    }

    public Object invokeMethodIfAccessable(String methodName, Class[] types, Object ...params) {
        try {
            return invokeMethod(methodName, types, params);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            LogUtils.e(CommonUtils.exceptionStacktraceToString(e));
            return null;
        }
    }

    public Object invokeMethod(String methodName, Class[] types, Object ...params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invokeMethod(getMethod(methodName, types), params);
    }

    public Object invokeMethod(String methodName, Object ...params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return invokeMethod(getMethod(methodName, guessTypesClass(params)), params);
    }

    public Object invokeMethod(Method method, Object ...params) throws IllegalAccessException, InvocationTargetException {
        return method.invoke(object, params);
    }

    public Class<?> getObjectClass() {
        return object.getClass();
    }

    public Class<?> getObjectClass(ClassLoader classLoader) {
        return XposedHelpers.findClassIfExists(getClassName(), classLoader);
    }

    public void hookMethod(ClassLoader classLoader, String methodName, Object ...parameterTypesAndCallback) {
        XposedHelpers.findAndHookMethod(getObjectClass(classLoader), methodName, parameterTypesAndCallback);
    }

    public void hookMethod(ClassLoader classLoader, String methodName, List<Class> parameterTypes, Object callback) {
        ArrayList<Object> parameterTypesAndCallback = new ArrayList<Object>(parameterTypes);
        parameterTypesAndCallback.add(callback);

        XposedHelpers.findAndHookMethod(getObjectClass(classLoader), methodName, parameterTypesAndCallback.toArray());
    }

    public Object getObject() {
        return object;
    }

    private Class[] guessTypesClass(Object ...params) {
        List<Class<?>> paramsClassList = new ArrayList<>();

        for (Object param : params) {
            paramsClassList.add(param.getClass());
        }

        return paramsClassList.toArray(new Class[paramsClassList.size()]);
    }

    public T newInstance(ClassLoader classLoader, Object ...params) {
        setObject(XposedHelpers.newInstance(getObjectClass(classLoader), params));
        return (T) this;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ReflectWrapper) {
            return getObject().equals(((ReflectWrapper) obj).getObject());
        } else {
            return false;
        }
    }
}
