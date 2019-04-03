package cc.c0ldcat.autorun.modules.real;

import cc.c0ldcat.autorun.modules.Module;
import cc.c0ldcat.autorun.wrappers.com.example.gita.gxty.utils.EmulatorDetectorWrapper;
import de.robv.android.xposed.XC_MethodReplacement;

public class NoCheckEmulator extends Module {
    private ClassLoader classLoader;

    public NoCheckEmulator(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void load() {
        super.load();
        new EmulatorDetectorWrapper().hookCheckEmulator(classLoader, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                return false;
            }
        });
    }
}
