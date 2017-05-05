package com.droideek.util;

import android.app.Activity;
import android.text.TextUtils;
import android.widget.Toast;

import com.platform.BaseApplication;

public class ToastUtil {
    protected static Toast toast = null;
   // public static AppMsg msgToast;
    private static int oldMsgId;
    private static long oneTime = 0;
    private static long twoTime = 0;

    private static final int TYPE_APP = 0;
    private static final int TYPE_MSG = 1;
    private static int type = TYPE_APP;
    private static final int LENGTH = 2500;

    public static void showToast(Activity activity, int id) {
        if (null == activity) {
            return;
        }

        if (TYPE_APP == type && null ==toast) {
            displayToast(activity, id);
            oneTime = System.currentTimeMillis();
            oldMsgId = id;
        } else {
            twoTime = System.currentTimeMillis();
            if (oldMsgId == id) {
                if (twoTime - oneTime > LENGTH) {
                    display();
                    oneTime = twoTime;
                }
            } else {
                oldMsgId = id;
                setTextDisplay(id);
                oneTime = twoTime;
            }

        }

    }

    public static void showToast(Activity activity, String msg) {
        if (null == activity || TextUtils.isEmpty(msg)) {
            return;
        }
        if (TYPE_APP == type && null ==toast) {
            displayToast(activity, msg);
            oneTime = System.currentTimeMillis();
            oldMsgId = msg.hashCode();
        } else {
            twoTime = System.currentTimeMillis();
            if (oldMsgId == msg.hashCode()) {
                if (twoTime - oneTime > LENGTH) {
                    display();
                    oneTime = twoTime;
                }
            } else {
                oldMsgId = msg.hashCode();
                setTextDisplay(msg);
                oneTime = twoTime;
            }
        }
    }


    private static void displayToast(Activity activity, int id) {
        if (TYPE_APP == type) {
            toast = Toast.makeText(activity, id, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private static void display() {
        if (TYPE_APP == type) {
            if (null != toast) {
                toast.show();
            }
        }
    }

    private static void setTextDisplay(int resId) {
        if (TYPE_APP == type) {
            if (null != toast) {
                toast.setText(resId);
                toast.show();
            }

        }
    }

    private static void setTextDisplay(String msg) {
        if (TYPE_APP == type) {
            if (null != toast) {
                toast.setText(msg);
                toast.show();
            }

        }
    }

    private static void displayToast(Activity activity, String msg) {
        if (TYPE_APP == type) {
            toast = Toast.makeText(activity, msg, Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public static void cancelAll() {
        toast = null;
    }

    public static void showAppToast(String msg) {
        Toast.makeText(BaseApplication.context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showAppToast(int resId) {
        Toast.makeText(BaseApplication.context, resId, Toast.LENGTH_LONG).show();
    }
}
