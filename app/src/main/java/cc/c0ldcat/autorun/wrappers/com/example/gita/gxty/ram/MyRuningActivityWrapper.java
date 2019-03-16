package cc.c0ldcat.autorun.wrappers.com.example.gita.gxty.ram;

import cc.c0ldcat.autorun.wrappers.ReflectWrapper;

public class MyRuningActivityWrapper extends ReflectWrapper<MyRuningActivityWrapper> {
    @Override
    public String getClassName() {
        return "com.example.gita.gxty.ram.MyRuningActivity";
    }

    public void hookStart(ClassLoader classLoader, Object callback) {
        hookMethod(classLoader, "v", callback);
    }

    public void hookPause(ClassLoader classLoader, Object callback) {
        hookMethod(classLoader, "u", callback);
    }
}
