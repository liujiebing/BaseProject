package com.droideek.util;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.xg.platform.R;


public class SnackbarUtil {

    private static final int infoColor = 0xc6333333;
    private static final int warningColor = 0xc6CC0000;
    private static final int alertColor = 0xc6CC0000;
    private static final int confirmColor = 0xc6333333;

    // android-support-design兼容包中新添加的一个类似Toast的控件。
    // make()中的第一个参数，可以写当前界面中的任意一个view对象。
    //private static Snackbar mSnackbar;

    public static Snackbar getSnackbar(View view, String msg, int flag) {
        final Snackbar mSnackbar;
        if (flag == 0) { // 短时显示
            mSnackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        } else { // 长时显示
            mSnackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        }

        // Snackbar中有一个可点击的文字，这里设置点击所触发的操作。
        mSnackbar.setAction(R.string.x_close, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Snackbar在点击“关闭”后消失
                mSnackbar.dismiss();
            }
        });

        return mSnackbar;
    }


    private static View getSnackBarLayout(Snackbar snackbar) {
        if (snackbar != null) {
            return snackbar.getView();
        }
        return null;
    }

    private static Snackbar colorSnackBar(Snackbar snackbar, int colorId) {
        View snackBarView = getSnackBarLayout(snackbar);
        if (snackBarView != null) {
            snackBarView.setBackgroundColor(colorId);
        }

        return snackbar;
    }


    public static void info(View view, String msg, int flag) {

        colorSnackBar(getSnackbar(view, msg, flag), infoColor).show();
    }

    public static void info(View view, String msg) {

        colorSnackBar(getSnackbar(view, msg, 0), infoColor).show();
    }

    public static void info(View view, int resId) {
        if(null == view) return;

        colorSnackBar(getSnackbar(view, view.getResources().getString(resId), 0), infoColor).show();
    }


    public static void alert(View view, String msg, int flag) {

        colorSnackBar(getSnackbar(view, msg, flag), alertColor).show();
    }

    public static void alert(View view, String msg) {

        colorSnackBar(getSnackbar(view, msg, 0), alertColor).show();
    }

    public static void alert(View view, int resId) {
        if(null == view) return;

        colorSnackBar(getSnackbar(view, view.getResources().getString(resId), 0), alertColor).show();
    }


    public static void warning(View view, String msg, int flag) {

        colorSnackBar(getSnackbar(view, msg, flag), warningColor).show();
    }


    public static void confirm(View view, String msg, int flag) {

        colorSnackBar(getSnackbar(view, msg, flag), confirmColor).show();
    }

}
