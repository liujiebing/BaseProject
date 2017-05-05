package com.droideek.util;


import android.util.Log;

import com.droideek.net.ApiClient;
import com.orhanobut.logger.Logger;


public final class LogUtil {
    public static boolean isDebug = false;

    /**
     * 开关log
     *
     * @param debug ：true开， false关
     */
    public static void init(String debug) {
        if ("none".equalsIgnoreCase(debug)) {
            isDebug = false;
        } else {
            isDebug = true;
            if ("file".equalsIgnoreCase(debug)) {
                //Logcat2File.getInstance().start(context);
            }
        }

        if (isDebug) { //TODO DELETE TEST
            Logger.init(ApiClient.TAG);
        }
    }


    public static void d(String tag, String str) {
        if (isDebug) {
            try {
                Log.d(tag, str);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static void e(String tag, String str) {
        if (isDebug) {
            try {
                Log.e(tag, str);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static void i(String tag, String str) {
        if (isDebug) {
            try {
                Log.i(tag, str);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static void v(String tag, String str) {
        if (isDebug) {
            try {
                Log.v(tag, str);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static void d(String str) {
        if (isDebug) {
            try {
                v("LogUtil", str);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static void json(String json) {
        if (isDebug) {
			Logger.json(json);
        }
    }

}
