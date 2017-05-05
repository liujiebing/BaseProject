package com.droideek.util;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Droideek on 2016/9/23.
 */
public class UIUtil {

    /**
     * //status bar 全透明效果, 必须在set contentView之前
     * @param activity
     */
    public static void setTransparentStatusBar(Activity activity) {
        if(null == activity) return;

        activity.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /**
     *
     * @param view
     * @param listener
     * @return int[2] int[0]= width, int[1] = heigth;
     */
    public static int[] getSizeWhenLayout(View view, ViewTreeObserver.OnGlobalLayoutListener listener) {
        int[] size = new int[2];

        int mWidth = view.getMeasuredWidth();
        int mHeight = view.getMeasuredHeight();
        if (0 == mWidth || 0 == mHeight) {
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            mWidth = view.getMeasuredWidth();
            mHeight = view.getMeasuredHeight();
        }

        size[0] = mWidth;
        size[1] = mHeight;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        } else {
            ViewTreeObserver observer1 = view.getViewTreeObserver();
            observer1.removeGlobalOnLayoutListener(listener);
        }
        return size;
    }

    public static void setAlpha(View view, float alpha) {
        if (alpha <= 0) {
            view.setAlpha(0);
        }else if(alpha > 1){
            view.setAlpha(1);
        } else if(alpha != view.getAlpha()){
            view.setAlpha(alpha);
            //view.setVisibility(View.VISIBLE);
        }
    }
}

