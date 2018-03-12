package cc.c0ldcat.autorun.utils;

import android.util.Log;
import cc.c0ldcat.autorun.Common;
import de.robv.android.xposed.XposedBridge;

public class LogUtils {
    public static void i(String msg) {
        Log.i(Common.DEBUG_TAG, msg);
        XposedBridge.log(msg);
    }

    public static void d(String msg) {
        Log.d(Common.DEBUG_TAG, msg);
        XposedBridge.log(msg);
    }

    public static void e(String msg) {
        Log.e(Common.DEBUG_TAG, msg);
        XposedBridge.log(msg);
    }

    public static void e(Throwable e) {
        String msg = CommonUtils.exceptionStacktraceToString(e);
        Log.e(Common.DEBUG_TAG, msg);
        XposedBridge.log(msg);
    }
}
