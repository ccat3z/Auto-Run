package cc.c0ldcat.autorun.modules.shell;

import android.os.Environment;
import cc.c0ldcat.autorun.modules.Module;
import cc.c0ldcat.autorun.utils.CommonUtils;
import cc.c0ldcat.autorun.utils.LogUtils;
import cc.c0ldcat.autorun.utils.RefectHelper;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashSet;
import java.util.Set;

// 方法出自: https://bbs.pediy.com/thread-224105.htm

public class SaveRealDex extends Module {
    private Set<Object> realDexs = new HashSet<>();
    private ClassLoader classLoader;

    private static final String DEX_SAVE_DIR = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();

    public SaveRealDex(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void load() {
        super.load();
        XposedHelpers.findAndHookMethod("java.lang.ClassLoader", classLoader, "loadClass", String.class, boolean.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                Class<?> cls = (Class<?>) param.getResult();
                Object dex = RefectHelper.getPrivateMethod(Class.forName("java.lang.Class"), "getDex").invoke(cls);
                byte[] dexBytes = (byte[]) RefectHelper.getPrivateMethod(Class.forName("com.android.dex.Dex"), "getBytes").invoke(dex);

                StringBuilder logSB = new StringBuilder();
                logSB.append("load class: ").append(cls.getName()).append(" from dex: ").append(dex.hashCode())
                        .append(" (length ").append(dexBytes.length).append(")");
                LogUtils.i(logSB.toString());

                if (realDexs.add(dex)) {
                    String name = dex.hashCode() + "-" + dexBytes.length + ".dex";
                    File save = new File(CommonUtils.concatPath(DEX_SAVE_DIR, name));

                    try (FileOutputStream os = new FileOutputStream(save)) {
                        os.write(dexBytes);
                        LogUtils.i("saved dex as " + name);
                    }

                }
            }
        });
    }
}
