package com.platform.ui;

import android.widget.ListView;

import com.droideek.inject.InjectData;
import com.platform.ui.widget.custom.RefreshLoadMoreListView;
import com.xg.platform.R;
import com.platform.data.MsgTO;
import com.platform.helper.UIHelper;
import com.platform.presenter.BaseContract;
import com.droideek.ui.adapter.BaseItemAdapter;

import java.util.List;

/**
 * Created by Droideek on 2016/1/6.
 */
public abstract class BaseLoadMoreFragment<T extends BaseContract.Presenter> extends BaseFragment<T> implements ReloadListener,RefreshLoadMoreListView.LoadMoreListener {
    protected BaseItemAdapter mAdapterList;
    protected RefreshLoadMoreListView mRefreshView;
    protected ListView mLv;

    @InjectData
    protected boolean hasMore;

    public void initView(Boolean autoRequest, boolean withFooter) {
        mRefreshView = (RefreshLoadMoreListView) getView().findViewById(R.id.v_refresh);
        if (null != mRefreshView) {
            mRefreshView.setWithFooter(withFooter);
            mRefreshView.setLoadMoreListener(this);
            mLv = mRefreshView.getRefreshView();
        }

        addHeaderFooter();
        setAdapter();

        if (autoRequest) {
            onRefresh();
        }
    }

    protected void disablePulltoRefresh() {
        if (null != mRefreshView) {
            mRefreshView.disablePullToRefresh();
        }
    }

    protected void addHeaderFooter() {

    }

    protected abstract void setAdapter();


    public void hideProgress() {
        if (null != mRefreshView) {
            loadFinish();
        }
    }

    @Override
    public void initView() {
        initView(true, true);
    }


    /**
     * 发起从第一页开始的刷新请求
     */
    public void onRefresh() {
        if (null != mRefreshView) {
            mRefreshView.refresh();
        }
    }

    /**
     * 请求订单列表
     *
     * @param pageIndex
     * @param pageSize
     */
    abstract protected void requestList(int pageIndex, int pageSize);

    @Override
    public void onPause() {
        super.onPause();
        loadFinish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapterList != null) {
            mAdapterList.clear();
            mAdapterList.notifyDataSetChanged();
        }
    }


    public void onResponse(BaseItemAdapter mAdapterList, List<?> list, boolean isRefresh, boolean hasMore) {
        if (isRefresh) {
            mAdapterList.clear();
        }
        mAdapterList.addAll(list);
        onResponse(mAdapterList, hasMore);
    }


    public void onResponse(BaseItemAdapter mAdapterList, boolean hasMore) {
        onResponse(mAdapterList, hasMore, null);
    }

    public void onResponse(BaseItemAdapter mAdapterList, boolean hasMore, ListDataOnListener mListDataOnListener) {
        // 因为每次都取全部数据，所以每次都clear()


//        if (1 == mRefreshView.getCurPage()) {
//            mAdapterList.notifyDataSetChanged();
//        }

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

    protected void showEmptyView(BaseItemAdapter mAdapterList) {
        afterResponse(false);
    }

    protected void afterResponse(boolean hasMore) {
        if (null != mRefreshView) {
            mRefreshView.afterResponse(hasMore);
        }
    }

    protected void loadFinish() {
        if (null != mRefreshView) {
            mRefreshView.reset();
        }
        mPresenter.hideDialogProgress();
    }

    protected void loadFinish(boolean canLoadMore) {
        loadFinish();
        if (null != mRefreshView) {
            mRefreshView.setCanLoadMore(canLoadMore);
        }

    }

    @Override
    public void refresh(int pageIndex) {

        if (null != mRefreshView) {
            mRefreshView.setIsLoading(true);
        }
        requestList(pageIndex, 20);
    }

    @Override
    public void loadMore(int pageIndex) {
        refresh(pageIndex);
    }

    @Override
    public void onHttpError(boolean showPageError, MsgTO data, boolean isRefresh) {
        loadFinish();
        if (showPageError) {
            if (isRefresh && mAdapterList.isEmpty()) {
                UIHelper.showErrorView(getActivity(), mLv, data, this);
            } else if (!isRefresh && !mAdapterList.isEmpty()) {
                if (null != mRefreshView) {
                    mRefreshView.onLoadMoreError(data);
                }
            } else {
                super.onHttpError(showPageError, data, isRefresh);
            }
        }


    }

    @Override
    public void reload() {
        showDialogProgress();
        refresh(1);
    }

}
