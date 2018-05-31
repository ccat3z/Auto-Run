package cc.c0ldcat.autorun.modules.real;

import android.app.Activity;
import android.os.Bundle;
import cc.c0ldcat.autorun.modules.Module;
import cc.c0ldcat.autorun.utils.LogUtils;
import cc.c0ldcat.autorun.wrappers.com.example.gita.gxty.ram.MyRuningActivityWrapper;
import de.robv.android.xposed.XC_MethodHook;

import java.util.ArrayList;
import java.util.Arrays;

public class GetMyRuningActivity extends Module {
    private ClassLoader classLoader;
    private Activity myRuningActivity;

    public GetMyRuningActivity(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public Activity getMyRuningActivity() {
        return myRuningActivity;
    }

    @Override
    public void load() {
        super.load();
        new MyRuningActivityWrapper().hookMethod(classLoader, "onCreate", Arrays.asList(new Class[]{Bundle.class}), new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                myRuningActivity = (Activity) param.thisObject;
                LogUtils.d("get activity");
                LogUtils.setContext(myRuningActivity);
            }
        });
    }
}
