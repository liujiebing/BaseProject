package com.droideek.util;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lib.pulltorefresh.library.PullToRefreshListView;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Droideek on 2015/12/27.
 */
public class Util {

    private static final String TAG = "Util";
    private static ActivityManager activityManager;

    /**
     * 在某些经过简洁版本的Android系统中
     * RunningAppProcessInfo.importance的值一直为IMPORTANCE_BACKGROUND.
     * 记录是否为特殊这种系统
     */
    private static boolean mIsSpecialSystem = false;

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (!mIsSpecialSystem) {
            try {
                boolean isSpecial = true;
                String packageName = context.getPackageName();
                List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
                if (null != appProcesses) {
                    for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                        if (appProcess.processName.equals(packageName)) {
                            if ((appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                                    || (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE)) {
                                KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
                                if (keyguardManager.inKeyguardRestrictedInputMode()) {
                                    return false;
                                } else {
                                    return true;
                                }
                            }
                        }
                        if (isSpecial) {
                            if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                                isSpecial = false;
                            }
                        }
                    }
                    if (isSpecial) {
                        mIsSpecialSystem = true;
                        return !isApplicationBroughtToBackgroundByTask(context, activityManager);
                    }
                }
            } catch (Exception e) {

            }
        } else {
            return !isApplicationBroughtToBackgroundByTask(context, activityManager);
        }
        return false;
    }

    /**
     * 判断当前应用程序是否处于后台，通过getRunningTasks的方式
     *
     * @return true 在后台; false 在前台
     */
    public static boolean isApplicationBroughtToBackgroundByTask(Context context, ActivityManager activityManager) {
        try {
            List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
            if (!tasks.isEmpty()) {
                ComponentName topActivity = tasks.get(0).topActivity;
                if (!topActivity.getPackageName().equals(context.getPackageName())) {
                    return true;
                }
            }
        } catch (Exception e) {


        }
        return false;
    }


    /**
     * Clear the ui resources.
     *
     * @param view
     */
    public static void unbindDrawables(View view) {
        if (view == null) {
            return;
        }
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            if (view instanceof AdapterView<?>) {
            } else {
                ((ViewGroup) view).removeAllViews();
            }
        }
    }



    public static void setListView(ListView listView) {
        listView.setFriction(ViewConfiguration.getScrollFriction() * 0.5f);

        Class clsClass = listView.getClass().getSuperclass();
        if(clsClass == null) {

        }else{
            if(clsClass == PullToRefreshListView.InternalListView.class){
                clsClass = clsClass.getSuperclass().getSuperclass();
            }

            if(clsClass==AbsListView.class){
                try{
                    Field minField = AbsListView.class.getDeclaredField("mMinimumVelocity");
                    Field maxField = AbsListView.class.getDeclaredField("mMaximumVelocity");
                    minField.setAccessible(true);
                    maxField.setAccessible(true);
                    try{

                        maxField.set(listView, 7000);
                    } catch(IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch(IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } catch(NoSuchFieldException e) {
                    e.printStackTrace();
                }
            }else{
                //
            }
        }
    }


    /**
     * 判断某个界面是否在前台
     *
     * @param context
     * @param className
     *            某个界面名称
     */
    public static boolean isForeground(Context context, String className) {
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

    /**
     * 被视图被遮挡时流动布局以显示特定的部分
     * @param root
     * @param scrollToView  需要显示在键盘上边的VIEW
     */
    public static void controlKeyboardLayout(final View scrollView, final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);

                int invisiableHeight = root.getRootView().getHeight() - rect.bottom;
                if (invisiableHeight > 100) {
                    int[] location = new int[2];
                    scrollToView.getLocationInWindow(location);

                    int scrollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;

                    scrollView.scrollTo(0, scrollHeight);
                } else {
                    scrollView.scrollTo(0, 0);
                }
            }
        });
    }

    /**
     * 判断当前市场是否允许自动更新版本
     * @param ctx
     * @return false 表示不允许自动升级
     */
    public static boolean isAutoUpgradeAvailable(Context ctx) {
        String[] disableChannels = new String[]{"xiaomi"};
        String curChannel = getChannelName(ctx);
        for (int i = 0; i < disableChannels.length; i++) {
            if (disableChannels[i].equals(curChannel)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取渠道名
     * @param ctx
     * @return 如果没有获取成功，那么返回值为空
     */
    public static String getChannelName(Context ctx) {
        return getAppMetaData(ctx, "UMENG_CHANNEL");
    }

    /**
     * 获取application中指定的meta-data
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = String.valueOf(applicationInfo.metaData.get(key));
                    }
                }

            }
        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace();
        }

        return resultData;
    }


    public static boolean inMainProcess(Context context) {
        String packageName = context.getPackageName();
        String processName = getProcessName(context);
        return packageName.equals(processName);
    }

    /**
     * 获取当前进程名
     * @param context
     * @return 进程名
     */
    public static final String getProcessName(Context context) {
        String processName = null;

        // ActivityManager
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));

        while (true) {
            for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
                if (info.pid == android.os.Process.myPid()) {
                    processName = info.processName;
                    break;
                }
            }

            if (!TextUtils.isEmpty(processName)) {
                return processName;
            }

            try {
                Thread.sleep(100L);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void moveTaskToFront(Context context) {
        if(null ==context) return;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> recentList = am.getRunningTasks(30);
            for (ActivityManager.RunningTaskInfo info : recentList) {
                if (info.topActivity.getPackageName().equals("com.xg.gj")) {
                    am.moveTaskToFront(info.id, 0);
                    return;
                }
        }

    }

}
