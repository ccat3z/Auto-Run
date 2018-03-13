package cc.c0ldcat.autorun;

import cc.c0ldcat.autorun.modules.shell.RealModuleLoader;
import cc.c0ldcat.autorun.modules.shell.SaveRealDex;
import cc.c0ldcat.autorun.utils.LogUtils;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class Hook implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        if (loadPackageParam.packageName.equals("com.example.gita.gxty")) {
            LogUtils.i("in 高校体育 app");

            if (BuildConfig.SAVE_REAL_DEX) new SaveRealDex(loadPackageParam.classLoader).load();
            if (BuildConfig.REAL_MODULE_LOADER) new RealModuleLoader(loadPackageParam.classLoader).load();
        }
    }
}
