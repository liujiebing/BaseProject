package com.lib.swipetoloadlayout.seagoor;

import android.util.Log;

/**
 * Log utility class to handle the log tag and DEBUG-only logging.
 */
public final class Logr {
    static boolean DEBUG = false; //TODO CLOSE THIS DEBUG SWITCH;

    public static void d(String message) {
        if (DEBUG) { //TODO 测试BuildConfig.DEBUG;
            Log.d("=下拉刷新=", message);
        }
    }

    public static void d(String format, Object... args) {
        d(String.format(format, args));
    }

    public static void d(String tag, String message) {
        if (DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void d(String tag, String format, Object... args) {
        d(tag, String.format(format, args));
    }

}
