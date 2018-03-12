package cc.c0ldcat.autorun;

import cc.c0ldcat.autorun.utils.LogUtils;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hook implements IXposedHookLoadPackage {
    private static Class<?>[] moduleClasses = new Class<?>[] {};

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals("com.example.gita.gxty")) {
            LogUtils.i("in 高校体育 app");

            for (Class<?> c : moduleClasses) {
                if (IXposedHookLoadPackage.class.isAssignableFrom(c)) {
                    try {
                        LogUtils.i("load module " + c.getName());

                        IXposedHookLoadPackage m = (IXposedHookLoadPackage) c.newInstance();
                        m.handleLoadPackage(loadPackageParam);
                    } catch (Throwable e) {
                        LogUtils.e(e);
                    }
                }
            }
        }
    }
}
