package cc.c0ldcat.autorun.modules.shell;

import cc.c0ldcat.autorun.BuildConfig;
import cc.c0ldcat.autorun.modules.Module;
import cc.c0ldcat.autorun.modules.real.GetCheckPoint;
import cc.c0ldcat.autorun.utils.LogUtils;
import cc.c0ldcat.autorun.utils.RefectHelper;
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

                Object dex = RefectHelper.getPrivateMethod(Class.forName("java.lang.Class"), "getDex").invoke(cls);
                byte[] dexBytes = (byte[]) RefectHelper.getPrivateMethod(Class.forName("com.android.dex.Dex"), "getBytes").invoke(dex);


                if (dexBytes.length == BuildConfig.REAL_DEX_LENGTH) {
                    realClassLoader = (ClassLoader) param.thisObject;
                    LogUtils.i("get real ClassLoader: " + classLoader.hashCode());

                    new GetCheckPoint(realClassLoader).load();
                }
            }
        });
    }
}
