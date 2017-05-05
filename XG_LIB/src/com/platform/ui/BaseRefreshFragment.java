package com.platform.ui;


import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.platform.data.MsgTO;
import com.xg.platform.R;
import com.platform.presenter.BaseContract;
import com.droideek.ui.adapter.BaseItemAdapter;
import com.droideek.util.Util;

import java.util.List;

/**
 * Created by Droideek on 2015/12/27.
 */
public abstract class BaseRefreshFragment<T extends BaseContract.Presenter> extends BaseFragment<T> implements SwipeRefreshLayout.OnRefreshListener {
    private static final int REFRESH_COMPLETE=0x1;
    protected SwipeRefreshLayout mSwipeLayout;
    protected ListView mLv;
    protected BaseItemAdapter mAdapterList;
    private TextView tv_footer;
    boolean canLoadMore;
    int pageIndex = 1; // 页数
    boolean isRefreshing = false;
    View mFooter;
    int qtyLeftForLoad = 0; //定义加载更多的临界值 (LV剩下item数)



    private void onLoadMoreFinish() {
        isRefreshing = false;
        if (null != mFooter) {

            if (mLv.getFirstVisiblePosition() > 0) {
                tv_footer.setText(R.string.x_cap_no_more);
                mFooter.setVisibility(View.VISIBLE);
            } else {
                mFooter.setVisibility(View.GONE);
            }
        }

    }

    /**
     * 开始刷新
     */
    public void refresh() {
        if(!isRefreshing){
            pageIndex = 1;
            showLoadMoreFooter(false);

            refresh(1);
        }
    }


    public void refresh(int pageIndex){

            isRefreshing = true;
            requestList(pageIndex, 20);

    }


    public void loadMore(int pageIndex) {
        refresh(pageIndex);
    }

    public void showLoadMoreFooter(boolean enable) {
        if (null != mFooter) {
            mFooter.setVisibility(enable ? View.VISIBLE : View.GONE);
        }
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    @Override
    protected void initView() {
        initView(true, true);
    }

    protected void initView(boolean autoRequest, boolean withFooter) {

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_root);
        mLv = (ListView) findViewById(R.id.lv);

        if (mSwipeLayout != null) {
            mSwipeLayout.setOnRefreshListener(this);
            mSwipeLayout.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.black,
                    android.R.color.holo_red_light, android.R.color.holo_orange_light);
        }

        Util.setListView(mLv);

        setWithFooter(withFooter);
        addHeaderFooter();

        setAdapter();


        mLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!canLoadMore) {
                    if (null != mFooter) {
                        onLoadMoreFinish();
                    }
                } else if ((totalItemCount - firstVisibleItem - visibleItemCount) <= qtyLeftForLoad && !isRefreshing) {

                    if (null != tv_footer) {
                        mFooter.setVisibility(View.VISIBLE);
                        tv_footer.setText(R.string.x_cap_is_loading);

                    }

                    ++pageIndex;
                    loadMore(pageIndex);
                }
            }
        });


        if (autoRequest) {
            onRefresh();
        }
    }

    public void onResponse(BaseItemAdapter mAdapterList, boolean hasMore) {
        onResponse(mAdapterList, hasMore, null);
    }

    public void onResponse(BaseItemAdapter mAdapterList, boolean hasMore, ListDataOnListener mListDataOnListener) {
        // 因为每次都取全部数据，所以每次都clear()

        loadFinish(hasMore);

        List data = mAdapterList.getData();
        if (mListDataOnListener != null) {
            mListDataOnListener.onSetListData(data);
        } else {
            if (null != data && data.size() > 0) {
                afterResponse(hasMore);
            } else {
                showEmptyView(mAdapterList);
            }
        }

        mAdapterList.notifyDataSetChanged();
    }


    public void setWithFooter(boolean withFooter) {
        if (withFooter) {
            View footer= LayoutInflater.from(getContext()).inflate(R.layout.xg_listview_no_more, null);
           // footer_progress = (ProgressBar) footer.findViewById(R.id.footer_progress);
            tv_footer = (TextView)footer.findViewById(R.id.tv_footer);
            footer.setVisibility(View.GONE);
            mLv.addFooterView(footer);

            this.mFooter = footer;
        }

    }

    protected void showEmptyView(BaseItemAdapter mAdapterList) {
        afterResponse(false);
    }

    protected void afterResponse(boolean hasMore) {
        if (!hasMore) {
            onLoadMoreFinish();
        }
    }

    protected void loadFinish() {
        refreshFinish();
        mPresenter.hideDialogProgress();
    }

    protected void loadFinish(boolean canLoadMore) {
        loadFinish();
        setCanLoadMore(canLoadMore);
    }


    protected void refreshFinish() {
        isRefreshing =false;
        mSwipeLayout.setRefreshing(false);
    }

    protected abstract boolean getCanLoadMore();

    abstract protected void requestList(int pageIndex, int pageSize);

    protected void addHeaderFooter() {

    }

    protected void setAdapter() {

    }


    @Override
    public void onRefresh() {
        refresh();
    }

    @Override
    public void onPause() {
        super.onPause();
        loadFinish();
    }

    @Override
    public void onHttpError(boolean showPageError, MsgTO data, boolean isRefresh) {
        super.onHttpError(showPageError,data, isRefresh);
        loadFinish();
    }
}