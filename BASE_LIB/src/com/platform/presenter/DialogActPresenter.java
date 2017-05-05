package com.platform.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;

/**
 * Created by Droideek on 2016/6/3.
 */
public class DialogActPresenter <T extends DialogActContract.View> extends BasePresenter<T> implements DialogActContract.Presenter {
    int activityCloseEnterAnimation;
    int activityCloseExitAnimation;

    public DialogActPresenter(Context context, T view) {
      super(context, view);
    }

    public DialogActPresenter(T view) {
        super(view);
    }

    @Override
    public void setMatchWidth(Activity context, float percent) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        WindowManager.LayoutParams p = context.getWindow().getAttributes();  //获取对话框当前的参数值
        p.width = (int) (metrics.widthPixels * percent);
        p.gravity = Gravity.BOTTOM;

        context.getWindow().setAttributes(p);     //设置生效
    }

    @Override
    public void onCreate(Activity context) {
        context.getWindow().setBackgroundDrawable(context.getResources().getDrawable(android.R.color.transparent));

        try {
            TypedArray activityStyle = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowAnimationStyle});
            int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);
            activityStyle.recycle();
            activityStyle = context.getTheme().obtainStyledAttributes(windowAnimationStyleResId,
                    new int[]{android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation,
                            android.R.attr.activityOpenEnterAnimation, android.R.attr.activityOpenExitAnimation});
            activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);
            activityCloseExitAnimation = activityStyle.getResourceId(1, 0);
//            activityOpenEnterAnimation = activityStyle.getResourceId(2, 0);
//            activityOpenExitAnimation = activityStyle.getResourceId(3, 0);
            activityStyle.recycle();
        } catch (Exception e) {
            //EMPTY
        }

        context.overridePendingTransition(com.xg.platform.R.anim.x_push_bottom_in, com.xg.platform.R.anim.x_push_bottom_out);
    }

    @Override
    public void onFinish(Activity context) {
        context.overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }

    @Override
    public void unSubscribe() {

    }
}
