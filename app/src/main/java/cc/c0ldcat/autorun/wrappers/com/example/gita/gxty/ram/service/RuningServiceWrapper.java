package cc.c0ldcat.autorun.wrappers.com.example.gita.gxty.ram.service;

import cc.c0ldcat.autorun.wrappers.ReflectWrapper;

public class RuningServiceWrapper extends ReflectWrapper<RuningServiceWrapper> {
    @Override
    public String getClassName() {
        return "com.example.gita.gxty.ram.service.RuningService";
    }

    public void hookGetBupin(ClassLoader classLoader, Object callback) {
        hookMethod(classLoader, "p", callback);
    }
}
