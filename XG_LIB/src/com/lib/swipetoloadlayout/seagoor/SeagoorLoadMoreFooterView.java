package com.lib.swipetoloadlayout.seagoor;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lib.swipetoloadlayout.SwipeLoadMoreFooterLayout;
import com.xg.platform.R;

/**
 * Created by Droideek on 2016/11/3.
 */

public class SeagoorLoadMoreFooterView extends SwipeLoadMoreFooterLayout {
    TextView tv_footer;
    ProgressBar footer_progress;
    boolean canLoadMore;

    public SeagoorLoadMoreFooterView(Context context) {
        super(context);
        init();
    }

    public SeagoorLoadMoreFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SeagoorLoadMoreFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        tv_footer = (TextView) findViewById(R.id.tv_footer);
        footer_progress = (ProgressBar) findViewById(R.id.footer_progress);
    }

    @Override
    public void onLoadMore() {
        Logr.d("== onLoadMore");
        if (canLoadMore) {
            footer_progress.setVisibility(VISIBLE);
        }else{
            setNoMore();
        }

    }

    private void setNoMore() {
        footer_progress.setVisibility(GONE);
        tv_footer.setText(R.string.x_cap_no_more);
    }

    @Override
    public void onPrepare() {
        Logr.d("== onPrepare");
        if (canLoadMore) {
            tv_footer.setText(R.string.x_cap_pull_to_load_more);
        }else{
            setNoMore();
        }
    }

    @Override
    public void onMove(int y, boolean isComplete, boolean automatic) {
        Logr.d("== onMove y: %d, isComplete: %b automatic %b", y, isComplete, automatic);
        if (canLoadMore) {
            tv_footer.setText(R.string.x_cap_is_loading);
        }else{
            setNoMore();
        }
    }

    @Override
    public void onRelease() {
        Logr.d("== onRelease");
        if (canLoadMore) {
            tv_footer.setText(R.string.x_cap_is_loading);
        }
    }

    @Override
    public void onComplete() {
        Logr.d("== onComplete");
        if (canLoadMore) {
            tv_footer.setText(R.string.x_cap_completed);
            tv_footer.setVisibility(VISIBLE);
            footer_progress.setVisibility(GONE);
        }

    }

    @Override
    public void onReset() {
        Logr.d("== onReset");
//        footer_progress.setVisibility(VISIBLE);
//        tv_footer.setVisibility(VISIBLE);
        if (canLoadMore) {
            tv_footer.setText("");
            footer_progress.setVisibility(GONE);
        }else{
            setNoMore();
        }

    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }
}
