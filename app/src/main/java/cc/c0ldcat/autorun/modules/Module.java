package cc.c0ldcat.autorun.modules;

import cc.c0ldcat.autorun.utils.LogUtils;

public class Module {
    public void load() {
        LogUtils.i("load module " + this.getClass().getName());
    }
}
