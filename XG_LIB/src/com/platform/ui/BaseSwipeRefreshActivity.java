package com.platform.ui;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.lib.swipetoloadlayout.OnLoadMoreListener;
import com.lib.swipetoloadlayout.OnRefreshListener;
import com.lib.swipetoloadlayout.SwipeToLoadLayout;
import com.lib.swipetoloadlayout.seagoor.SeagoorLoadMoreFooterView;
import com.platform.data.MsgTO;
import com.xg.platform.R;
import com.platform.presenter.BaseContract;
import com.droideek.ui.adapter.HeaderRecyclerAdapter;

import java.util.List;

/**
 * Created by Droideek on 2016/11/3.
 */

public abstract class BaseSwipeRefreshActivity<V extends View, T extends BaseContract.Presenter> extends BaseFragmentActivity<T>
        implements OnLoadMoreListener, OnRefreshListener {
    protected V mTargetView;
    private SwipeToLoadLayout swipeToLoadLayout;
    private int pageIndex = 1; // 页数
    private boolean canLoadMore;
    private SeagoorLoadMoreFooterView footerView;

    @Override
    public void initView() {
        initView(true, true);
    }

    public void initView(Boolean autoRequest, boolean withFooter){
        mTargetView = (V) findViewById(R.id.swipe_target);

        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoadLayout);
        swipeToLoadLayout.setOnRefreshListener(this);
        swipeToLoadLayout.setOnLoadMoreListener(this);

        swipeToLoadLayout.setSwipeStyle(SwipeToLoadLayout.STYLE.CLASSIC);
        swipeToLoadLayout.setRefreshHeaderView(LayoutInflater.from(this).inflate(R.layout.xg_swipe_refresh_header, swipeToLoadLayout, false));
        footerView = (SeagoorLoadMoreFooterView) LayoutInflater.from(this).inflate(R.layout.xg_swipe_refresh_footer, swipeToLoadLayout, false);
        swipeToLoadLayout.setLoadMoreFooterView(footerView);
        if (mTargetView instanceof RecyclerView) {
            ((RecyclerView)mTargetView).setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE ){
                        if (!ViewCompat.canScrollVertically(recyclerView, 1)){
                            swipeToLoadLayout.setLoadingMore(true);
                        }
                    }
                }
            });
        }

        setAdapter(); //这里顺序与listview不一样
        addHeaderFooter();

        if (autoRequest) {
            onRefresh();
        }
    }


    @Override
    public void onRefresh() {
        pageIndex =1;
        refresh(pageIndex);
        swipeToLoadLayout.setRefreshing(true);
    }

    public void onResponse(HeaderRecyclerAdapter mAdapterList, List<?> list, boolean isRefresh, boolean hasMore) {
        if (isRefresh) {
            mAdapterList.clear();
        }
        mAdapterList.addAll(list);
        onResponse(mAdapterList, hasMore);
    }


    public void onResponse(HeaderRecyclerAdapter mAdapterList, boolean hasMore) {
        onResponse(mAdapterList, hasMore, null);
    }

    public void onResponse(HeaderRecyclerAdapter mAdapterList, boolean hasMore, ListDataOnListener mListDataOnListener) {
        loadFinish(hasMore);

        List data = mAdapterList.getData();
        if (mListDataOnListener != null) {
            mListDataOnListener.onSetListData(data);
        } else {
            if (null != data && data.size() > 0) {
                afterResponse(hasMore);
            } else {
                showEmptyView();
            }
        }

        mAdapterList.notifyDataOnly();
    }

    protected void loadFinish() {
        swipeToLoadLayout.setLoadingMore(false);
        swipeToLoadLayout.setRefreshing(false);

    }

    public void loadFinish(boolean canLoadMore){
        loadFinish();
        this.canLoadMore = canLoadMore;
        footerView.setCanLoadMore(canLoadMore);
    }

    protected void showEmptyView() {

    }

    protected void afterResponse(boolean hasMore) {

    }


    protected abstract void requestList(int pageIndex, int pageSize);

    protected void addHeaderFooter(){

    }

    protected void setAdapter(){

    }

    @Override
    public void onLoadMore() {
        if (canLoadMore) {
            pageIndex++;
            refresh(pageIndex);
            swipeToLoadLayout.setLoadingMore(true);
        }else{
            loadFinish();
        }
    }


    public void refresh(int pageIndex) {
        requestList(pageIndex, 20);
    }


    @Override
    public void onHttpError(boolean showPageError, MsgTO data, boolean isRefresh) {
        super.onHttpError(showPageError, data, isRefresh);
        loadFinish();
    }
}
