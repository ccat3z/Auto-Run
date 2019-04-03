package cc.c0ldcat.autorun.wrappers.com.example.gita.gxty.utils;

import android.content.Context;

import cc.c0ldcat.autorun.wrappers.ReflectWrapper;

public class EmulatorDetectorWrapper extends ReflectWrapper<EmulatorDetectorWrapper> {
    @Override
    public String getClassName() {
        return "com.example.gita.gxty.utils.a.d";
    }

    public void hookCheckEmulator(ClassLoader classLoader, Object callback) {
        hookMethod(classLoader, "b", Context.class, callback);
    }
}
