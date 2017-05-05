package com.lib.swipetoloadlayout.seagoor;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lib.swipetoloadlayout.SwipeRefreshTrigger;
import com.lib.swipetoloadlayout.SwipeTrigger;
import com.xg.platform.R;

/**
 * Created by Droideek on 2016/11/2.
 */
public class SeagoorRefreshHeaderView extends LinearLayout implements SwipeTrigger, SwipeRefreshTrigger {
    ImageView ivRefresh;
    ImageView ivRunning;
    BaseRefreshDrawable mDrawable;
    int mDragHeight;
    boolean isRefreshing;

    AnimationDrawable mLoadingDrawable;

    TextView tv_status;

    public SeagoorRefreshHeaderView(Context context) {
        super(context);
        init();
    }

    public SeagoorRefreshHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public void init() {
        mLoadingDrawable = new AnimationDrawable();
        mLoadingDrawable.addFrame(getResources().getDrawable(R.drawable.xg_ptr_loading_1), 100);
        mLoadingDrawable.addFrame(getResources().getDrawable(R.drawable.xg_ptr_loading_2), 100);
        mLoadingDrawable.addFrame(getResources().getDrawable(R.drawable.xg_ptr_loading_3), 100);
        mLoadingDrawable.addFrame(getResources().getDrawable(R.drawable.xg_ptr_loading_4), 100);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Logr.d("== onFinishInflate();");

        tv_status = (TextView) findViewById(R.id.tv_status);

        ivRefresh = (ImageView) findViewById(R.id.iv_icon);
        ivRunning= (ImageView) findViewById(R.id.iv_running);
        mDragHeight = dp2px(80);
        mDrawable = new SeagoorRefreshDrawable(getContext(), this, mDragHeight);

        ivRefresh.setBackgroundDrawable(mDrawable);
        ivRefresh.setVisibility(VISIBLE);
        ivRunning.setImageDrawable(mLoadingDrawable);
        ivRunning.setVisibility(VISIBLE);

        //requestLayout();
    }

    @Override
    public void onRefresh() {
        Logr.d("== onRefresh();");
        isRefreshing = true;
        ivRefresh.setVisibility(GONE);
        ivRunning.setVisibility(VISIBLE);
        if (!mLoadingDrawable.isRunning()) {
            mLoadingDrawable.start();
        }
        tv_status.setText(R.string.pull_to_refresh_refreshing_label);
    }

    @Override
    public void onPrepare() {
        Logr.d("== onPrepare();");
        tv_status.setText(R.string.pull_to_refresh_pull_label);
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        Logr.d("== onMove(); isComplete : %b, automatic: %b, y: %d, isRefresh: %b", isComplete, automatic, y, isRefreshing);
        float percent = y*1.0f/mDragHeight;
        mDrawable.setPercent(percent, true);

        if (!isComplete) {
            if (y > mDragHeight) {
                ivRefresh.setVisibility(GONE);
                ivRunning.setVisibility(VISIBLE);
                if (!mLoadingDrawable.isRunning()) {
                    mLoadingDrawable.start();
                }
                tv_status.setText(R.string.pull_to_refresh_release_label);
            }else{

                if (!isRefreshing) {
                    tv_status.setText(R.string.pull_to_refresh_pull_label);
                    ivRefresh.setVisibility(VISIBLE);
                    ivRunning.setVisibility(GONE);
                }else{
                    tv_status.setText(R.string.pull_to_refresh_refreshing_label);
                    ivRefresh.setVisibility(GONE);
                    ivRunning.setVisibility(VISIBLE);
                    if (!mLoadingDrawable.isRunning()) {
                        mLoadingDrawable.start();
                    }
                }
            }
        }else{
            ivRefresh.setVisibility(GONE);
            ivRunning.setVisibility(VISIBLE);
            if (mLoadingDrawable.isRunning()) {
                mLoadingDrawable.stop();
            }
        }

        Logr.d("== onMove(); ivRefresh>Visible: %d runningVisiable: %d, height",ivRefresh.getVisibility(), ivRunning.getVisibility(), ivRunning.getHeight());
    }

    @Override
    public void onRelease() {
        Logr.d("== onRelease()");
        tv_status.setText(R.string.pull_to_refresh_refreshing_label);
    }

    @Override
    public void onComplete() {
        Logr.d("== onComplete()");
        tv_status.setText(R.string.x_cap_completed);

        if (mLoadingDrawable.isRunning()) {
            mLoadingDrawable.stop();
        }
        isRefreshing = false;
    }

    @Override
    public void onReset() {
        Logr.d("== onReset()");
        ivRefresh.setVisibility(VISIBLE);
        ivRunning.setVisibility(GONE);
    }

    public int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }
}
