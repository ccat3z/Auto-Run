package cc.c0ldcat.autorun.modules.real;

import android.os.Bundle;
import cc.c0ldcat.autorun.modules.Module;
import cc.c0ldcat.autorun.utils.CommonUtils;
import cc.c0ldcat.autorun.utils.LogUtils;
import cc.c0ldcat.autorun.wrappers.com.amap.api.maps.AMapWrapper;
import cc.c0ldcat.autorun.wrappers.com.example.gita.gxty.ram.MyRuningActivityWrapper;
import cc.c0ldcat.autorun.wrappers.com.example.gita.gxty.ram.service.RuningServiceWrapper;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import org.apache.commons.lang3.RandomUtils;

import java.util.*;

// TODO: blindly walk if distance is not enough

public class FakeWalk extends Module {
    private ClassLoader classLoader;
    private WalkingPlan walkingPlan;
    private FakeLocation fakeLocation;
    private CheckPointPlan checkPointPlan;

    private int step = 0;
    private double speedMax = 0.00007;
    private double speedMin = 0.00002;
    private Timer taskTimer;

    class FakeWalkTask extends TimerTask {
        private WalkingPlan walkingPlan;
        private FakeLocation fakeLocation;
        private CheckPointPlan checkPointPlan;

        public FakeWalkTask(WalkingPlan walkingPlan, FakeLocation fakeLocation, CheckPointPlan checkPointPlan) {
            super();
            this.walkingPlan = walkingPlan;
            this.fakeLocation = fakeLocation;
            this.checkPointPlan = checkPointPlan;
        }

        @Override
        public void run() {
            try {
                fakeLocation.move(CommonUtils.vector(fakeLocation, walkingPlan.next(), RandomUtils.nextDouble(speedMin, speedMax)));
                step += RandomUtils.nextInt(1, 2);
                checkPointPlan.destroyRecentCheckPoint(fakeLocation);
            } catch (NoSuchElementException e) {
            }
        }
    }

    public FakeWalk(ClassLoader classLoader, final WalkingPlan walkingPlan, final FakeLocation fakeLocation, CheckPointPlan checkPointPlan, GetRunningMap getRunningMap) {
        this.classLoader = classLoader;
        this.walkingPlan = walkingPlan;
        this.fakeLocation = fakeLocation;
        this.checkPointPlan = checkPointPlan;

        getRunningMap.addOnRunningMapChangeListener(new GetRunningMap.OnRunningMapChangeListener() {
            @Override
            public void onRunningMapChange(AMapWrapper aMap) {
                step = 0;
            }
        });
    }

    public void start() {
        if (taskTimer == null) {
            LogUtils.it("Fake walk start");
            taskTimer = new Timer();
            taskTimer.schedule(new FakeWalkTask(walkingPlan, fakeLocation, checkPointPlan), 1000, 1000);
        }
    }

    public void stop() {
        if (taskTimer != null) {
            LogUtils.it("Fake walk stop");
            taskTimer.cancel();
            taskTimer = null;
        }
    }

    @Override
    public void load() {
        super.load();

        new RuningServiceWrapper().hookGetBupin(classLoader, new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam methodHookParam) throws Throwable {
                return step;
            }
        });

        MyRuningActivityWrapper myRuningActivityHelper = new MyRuningActivityWrapper();
        myRuningActivityHelper.hookMethod(classLoader, "onCreate", Arrays.asList(new Class[]{Bundle.class}), new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                start();
            }
        });
        myRuningActivityHelper.hookMethod(classLoader, "onResume", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                start();
            }
        });
        myRuningActivityHelper.hookMethod(classLoader, "onPause", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                stop();
            }
        });
        myRuningActivityHelper.hookStart(classLoader, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                start();
            }
        });
        myRuningActivityHelper.hookPause(classLoader, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                stop();
            }
        });
    }
}
