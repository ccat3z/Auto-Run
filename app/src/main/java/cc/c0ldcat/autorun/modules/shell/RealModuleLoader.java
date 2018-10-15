package cc.c0ldcat.autorun.modules.shell;

import android.os.Build;
import cc.c0ldcat.autorun.BuildConfig;
import cc.c0ldcat.autorun.modules.Module;
import cc.c0ldcat.autorun.modules.real.FakeWalk;
import cc.c0ldcat.autorun.modules.real.GetCheckPoint;
import cc.c0ldcat.autorun.modules.real.GetMyRuningActivity;
import cc.c0ldcat.autorun.utils.LogUtils;
import cc.c0ldcat.autorun.utils.ReflectHelper;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

public class RealModuleLoader extends Module {
    private ClassLoader classLoader;
    private ClassLoader realClassLoader;

    public RealModuleLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void load() {
        super.load();
        XposedHelpers.findAndHookMethod("java.lang.ClassLoader", classLoader, "loadClass", String.class, boolean.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                if (realClassLoader != null)
                    return;

                Class<?> cls = (Class<?>) param.getResult();
                if (cls == null)
                    return;

                boolean isTargetDex = XposedHelpers.findClassIfExists(BuildConfig.CLASS_IN_REAL_MODULE_LOADER, (ClassLoader) param.thisObject) != null;

                if (isTargetDex) {
                    realClassLoader = (ClassLoader) param.thisObject;
                    LogUtils.i("get real ClassLoader: " + classLoader.hashCode());

                    GetCheckPoint getCheckPoint = new GetCheckPoint(realClassLoader);
                    getCheckPoint.load();

                    GetMyRuningActivity getMyRuningActivity = new GetMyRuningActivity(realClassLoader);
                    getMyRuningActivity.load();

                    new FakeWalk(realClassLoader, getCheckPoint, getMyRuningActivity).load();
                }
            }
        });
    }
}
