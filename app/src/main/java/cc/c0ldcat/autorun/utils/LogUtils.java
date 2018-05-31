package cc.c0ldcat.autorun.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import cc.c0ldcat.autorun.Common;
import de.robv.android.xposed.XposedBridge;

public class LogUtils {
    private static Context context;

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        LogUtils.context = context;
    }

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

    public static void toast(String msg) {
        if (context != null)
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void it(String msg) {
        i(msg);
        toast(msg);
    }
}
