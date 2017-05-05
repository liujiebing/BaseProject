package com.droideek.util;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Droideek on 2016/8/15.
 */
public class AppStatusTracker {
    private static final String TAG = "AppStatusTracker";

    public static int activityCount;
    public static Activity mCurrentActivity;
    public static ArrayList<String> activityList;
    public static AppStatusListener mListener;

    private static volatile AppStatusTracker INSTANCE;

    private AppStatusTracker() {
    }

    public static AppStatusTracker getInstance() {
        if (null == INSTANCE) {
            synchronized (AppStatusTracker.class) {
                if (null == INSTANCE) {
                    return new AppStatusTracker();
                }
            }
        }

        return INSTANCE;
    }

    public void init(Application app) {
        app.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacksImpl());
        activityList = new ArrayList<>();
    }

    public void init(Application app, AppStatusListener listener){
        init(app);
        mListener = listener;
    }

    public boolean isAppForeground() {
        return (activityCount != 0);
    }

    public class ActivityLifecycleCallbacksImpl implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            String name =activity.getClass().getSimpleName();

            if (!activityList.contains(name)) {
                activityList.add(name);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {

            activityCount++;
            if (null != mListener) {
                mListener.onActivityStarted(activity);
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
            if(LogUtil.isDebug) Log.d(TAG, "== TRACKER onActivityResumed:"+activity);
            mCurrentActivity = activity;
        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

            activityCount--;
            if (null != mListener) {
                mListener.onActivityStopped(activity);
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            activityList.remove(activity.getClass().getSimpleName());
        }
    }

    public boolean contains(String activityName) {
        return activityList.contains(activityName);
    }

    public boolean isTaskBottom() {
        if(null == activityList) return true;
        return activityList.size() == 1;
    }

    public interface AppStatusListener{
        void onActivityStarted(Activity activity);

        void onActivityStopped(Activity activity);
    }

}
