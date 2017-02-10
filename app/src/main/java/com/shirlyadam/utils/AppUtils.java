package com.shirlyadam.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.reflect.Method;
import java.util.List;

/**
 * AppUtils
 */
public class AppUtils {
    /**
     * 判断某个activity是否正在前台显示
     */
    public static boolean isActivityForground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static int getWindowWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    public static int getWindowHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    @NonNull
    private static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    /**
     * 生成状态选择器
     *
     * @param stateNormal
     * @param statePress
     * @return
     */
    public static StateListDrawable createStateDrawable(int stateNormal, int statePress) {
        /*没有选中的时候*/
        GradientDrawable normalBg = new GradientDrawable();
        normalBg.setColor(stateNormal);
        /*选中的时候*/
        GradientDrawable pressBg = new GradientDrawable();
        pressBg.setColor(statePress);
        /*生成选择器*/
        StateListDrawable selectorBg = new StateListDrawable();
        selectorBg.addState(new int[]{android.R.attr.state_pressed}, pressBg);
        selectorBg.addState(new int[]{}, normalBg);
        return selectorBg;
    }

    /**
     * 判断app是否在运行
     *
     * @param context
     * @return
     */
    public static boolean isAppAlive(Context context, String packageName) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for (int i = 0; i < processInfos.size(); i++) {
            if (processInfos.get(i).processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断Application栈是否为空
     */
    public static boolean isApplicationRunningTaskEmpty(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        return tasks.isEmpty();
    }


    /**
     * 判断设备是否有虚拟键盘
     *
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
        }
        return hasNavigationBar;
    }

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

}
