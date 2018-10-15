package cc.c0ldcat.autorun.wrappers.com.example.gita.gxty.utils;

import android.content.Context;
import cc.c0ldcat.autorun.wrappers.ReflectWrapper;

public class CheckHookWrapper extends ReflectWrapper<CheckHookWrapper> {
    @Override
    public String getClassName() {
        return "com.example.gita.gxty.utils.a.a";
    }

    public void hookCheckHook(ClassLoader classLoader, Object callback) {
        hookMethod(classLoader, "a", Context.class, callback);
    }
}
