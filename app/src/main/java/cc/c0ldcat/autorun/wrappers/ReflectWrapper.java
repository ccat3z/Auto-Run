package cc.c0ldcat.autorun.wrappers;

import cc.c0ldcat.autorun.utils.CommonUtils;
import cc.c0ldcat.autorun.utils.LogUtils;
import cc.c0ldcat.autorun.utils.ReflectHelper;
import de.robv.android.xposed.XposedHelpers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class ReflectWrapper {
    private Object object;

    abstract public String getClassName();

    public void setObject(Object object) {
        this.object = object;
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

    public Object invokeMethodIfAccessable(String methodName, Object ...params) {
        try {
            return invokeMethod(methodName, params);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            LogUtils.e(CommonUtils.exceptionStacktraceToString(e));
            return null;
        }
    }

    public Object invokeMethod(String methodName, Object ...params) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Class<?>> paramsClassList = new ArrayList<>();

        for (Object param : params) {
            paramsClassList.add(param.getClass());
        }

        return invokeMethod(getMethod(methodName, paramsClassList.toArray(new Class[paramsClassList.size()])));
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

}
