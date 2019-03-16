package cc.c0ldcat.autorun.modules.real;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import cc.c0ldcat.autorun.BuildConfig;
import cc.c0ldcat.autorun.Common;
import cc.c0ldcat.autorun.models.Location;
import cc.c0ldcat.autorun.models.SimpleLocation;
import cc.c0ldcat.autorun.models.SimpleVector;
import cc.c0ldcat.autorun.modules.Module;
import cc.c0ldcat.autorun.utils.CommonUtils;
import cc.c0ldcat.autorun.utils.LogUtils;
import cc.c0ldcat.autorun.wrappers.com.amap.api.location.AMapLocationWrapper;
import cc.c0ldcat.autorun.wrappers.com.example.gita.gxty.ram.MyRuningActivityWrapper;
import cc.c0ldcat.autorun.wrappers.com.example.gita.gxty.ram.service.BaseService.AMapLocationListenerWrapper;
import cc.c0ldcat.autorun.wrappers.com.example.gita.gxty.ram.service.RuningServiceWrapper;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import org.apache.commons.lang3.RandomUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

// TODO: blindly walk if distance is not enough

public class FakeWalk extends Module {
    private ClassLoader classLoader;
    private WalkingPlan walkingPlan;
    private FakeLocation fakeLocation;

    private int step = 0;
    private double speed = 0.0001;
    private double speedMax = 0.00007;
    private double speedMin = 0.00002;
    private Location next;
    private Timer taskTimer;

    class FakeWalkTask extends TimerTask {
        private WalkingPlan walkingPlan;
        private FakeLocation fakeLocation;

        public FakeWalkTask(WalkingPlan walkingPlan, FakeLocation fakeLocation) {
            super();
            this.walkingPlan = walkingPlan;
            this.fakeLocation = fakeLocation;
        }

        @Override
        public void run() {
            try {
                if (next == null) next = walkingPlan.next();

                speed = RandomUtils.nextDouble(speedMin, speedMax);
                fakeLocation.move(CommonUtils.vector(fakeLocation, next, speed));

                if (CommonUtils.distance(fakeLocation, next) < speed) {
                    next = null;
                }
                step += RandomUtils.nextInt(1, 2);
            } catch (NoSuchElementException e) {
            }
        }
    }

    public FakeWalk(ClassLoader classLoader, final WalkingPlan walkingPlan, final FakeLocation fakeLocation) {
        this.classLoader = classLoader;
        this.walkingPlan = walkingPlan;
        this.fakeLocation = fakeLocation;
    }

    public void start() {
        if (taskTimer == null) {
            LogUtils.it("Fake walk start");
            taskTimer = new Timer();
            taskTimer.schedule(new FakeWalkTask(walkingPlan, fakeLocation), 1000, 1000);
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
