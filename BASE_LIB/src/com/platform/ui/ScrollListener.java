package com.platform.ui;

import android.widget.AbsListView;

/**
 * Created by Droideek on 2016/6/2.
 */
public interface ScrollListener {

    void onScrollStateChanged(AbsListView view, int scrollState);

    void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
}
