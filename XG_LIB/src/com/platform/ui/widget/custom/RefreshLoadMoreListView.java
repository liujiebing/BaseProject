package com.platform.ui.widget.custom;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.platform.ui.ScrollListener;
import com.lib.pulltorefresh.library.PullToRefreshBase;
import com.lib.pulltorefresh.library.PullToRefreshListView;
import com.xg.platform.R;
import com.platform.data.MsgTO;
import com.droideek.util.Util;

/**
 * Created by Droideek on 2016/5/9.
 */
public class RefreshLoadMoreListView extends FrameLayout {
    boolean canLoadMore = true;
    PullToRefreshListView mRefreshListView;
    ListView mLv;
    int pageIndex = 1; // 页数
    boolean isRefreshing = false;
    View mFooter;


    TextView tv_footer;
    ProgressBar footer_progress;
    LoadMoreListener mLoadMoreListener;
    private View upView;
    private int loadFinishId=R.string.x_cap_no_more;

    public RefreshLoadMoreListView(Context context) {
        super(context);
        initView();
    }

    public RefreshLoadMoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RefreshLoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    ScrollListener mScrollListener;

    public void setScrollListener(ScrollListener scrollListener) {
        this.mScrollListener = scrollListener;
    }

    private void initView() {
        if (isInEditMode()) {
            return;
        }

        inflate(getContext(), R.layout.xg_loadmore_default, this);

        mRefreshListView = (PullToRefreshListView) findViewById(R.id.lv);
        mRefreshListView.getRefreshableView().setDivider(null);
        mRefreshListView.getRefreshableView().setSelector(android.R.color.transparent);
        mRefreshListView.setShowIndicator(false);

        mLv = mRefreshListView.getRefreshableView();

        Util.setListView(mLv);


        mRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isRefreshing) {
                    mRefreshListView.onRefreshComplete();
                    return;
                }
                if (mRefreshListView.isHeaderShown()) {
                    refresh();
                }
            }
        });

        mLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            boolean isShow;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(AbsListView.OnScrollListener.SCROLL_STATE_IDLE==scrollState && mLv.getLastVisiblePosition()== mLv.getCount()-1 ){
                    if (!canLoadMore) {
                        if (null != mFooter) {
                            onLoadMoreFinish();
                        }

                    } else if (!isRefreshing) {
                        if (null != tv_footer && null !=mFooter) {
                            mFooter.setVisibility(View.VISIBLE);
                            tv_footer.setText(R.string.x_cap_is_loading);
                            footer_progress.setVisibility(View.VISIBLE);
                        }

                        ++pageIndex;
                        if (null != mLoadMoreListener) {
                            mLoadMoreListener.loadMore(pageIndex);
                        }
                    }
                }
                if (null != mScrollListener) {
                    mScrollListener.onScrollStateChanged(view, scrollState);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (null != mScrollListener) {
                    mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }


                if(upView!=null){

                    if(firstVisibleItem>getChildCount()*2&&!isShow){
                        ObjectAnimator.ofFloat(upView,"Alpha",0,1).setDuration(400).start();
                        isShow=true;
                    }
                    if(firstVisibleItem<getChildCount()*2&&isShow){
                        ObjectAnimator.ofFloat(upView,"Alpha",1,0).setDuration(400).start();
                        isShow=false;
                    }

                }
            }
        });
    }


    public void setPullEventListener(PullToRefreshBase.OnPullEventListener listener) {
        mRefreshListView.setOnPullEventListener(listener);
    }

    public ListView getRefreshView() {
        return mLv;
    }

    public void setDivider(int heightRes, int colorRes) {
        if (null != mLv) {
            mLv.setDivider(getResources().getDrawable(colorRes));
            mLv.setDividerHeight(getResources().getDimensionPixelSize(heightRes));
        }
    }


    /**
     * 开始刷新
     */
    public void refresh() {
        if(!isRefreshing){
            pageIndex = 1;
            showLoadMoreFooter(false);
            if (null != mLoadMoreListener) {
                mLoadMoreListener.refresh(pageIndex);
            }
        }
    }

    public int getCurPage() {
        return pageIndex;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mLoadMoreListener = null;
    }

    public void disablePullToRefresh() {
        mRefreshListView.setMode(PullToRefreshBase.Mode.MANUAL_REFRESH_ONLY);
    }


    public void setIsLoading(boolean isRefreshing) {
        this.isRefreshing = isRefreshing;
    }

    public boolean getIsLoading() {
        return isRefreshing;
    }

    public void setWithFooter(boolean withFooter) {
        if (withFooter) {
            View footer= LayoutInflater.from(getContext()).inflate(R.layout.xg_listview_no_more, null);
            footer_progress = (ProgressBar) footer.findViewById(R.id.footer_progress);
            tv_footer = (TextView)footer.findViewById(R.id.tv_footer);
            footer.setVisibility(View.GONE);
            mLv.addFooterView(footer);

            this.mFooter = footer;
        }

    }
    public void setLoadFinishText(int loadFinishId){
       this.loadFinishId=loadFinishId;
    }
    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    private void onLoadMoreFinish() {
        onLoadMoreFinish(loadFinishId);
    }

    private void onLoadMoreFinish(int msgId) {
        isRefreshing = false;
        if (null != mFooter) {

            if (mLv.getFirstVisiblePosition() > 0) {
                tv_footer.setText(msgId);
                mFooter.setVisibility(View.VISIBLE);
                footer_progress.setVisibility(View.GONE);
            } else {
                mFooter.setVisibility(View.GONE);
            }
        }
    }

    public void onLoadMoreError(MsgTO data) {
        if(data!=null){
            onLoadMoreFinish(data.msgId);
        }

    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.mLoadMoreListener = loadMoreListener;
    }

    public void setUpView(View upView) {
        this.upView=upView;
    }

    public void setFootClickListener(OnClickListener listener) {
        if(mFooter!=null){
            mFooter.setOnClickListener(listener);
        }
    }

    public void setPullDownListener(PullToRefreshBase.PullDownListener pullDownListener) {
        mRefreshListView.setPullDownListener(pullDownListener);
    }


    public interface LoadMoreListener{
        void loadMore(int pageIndex);

        void refresh(int pageIndex);
    }

    /**
     * 重置布局
     */
    public void reset() {
        hideProgress();
        mRefreshListView.onRefreshComplete();
        resetLoadMore();
    }

    private void resetLoadMore() {
        if (isRefreshing && (null != mFooter)) {
            footer_progress.setVisibility(View.GONE);
            mFooter.setVisibility(GONE);
        }
        isRefreshing = false;
    }

    public void showLoadMoreFooter(boolean enable) {
        if (null != mFooter) {
            mFooter.setVisibility(enable? View.VISIBLE: View.GONE);
        }
    }

    /**
     * 隐藏布局内的加载动画
     */
    public void hideProgress() {
        View progress = findViewById(R.id.v_progress);
        if (null != progress) {
            progress.setVisibility(View.GONE);
        }

        View progressBg = findViewById(R.id.iv_pb_bg);
        if (null !=progressBg) {
            progressBg.setVisibility(View.GONE);
        }
    }

    /**
     *
     * @param hasMore
     */
    public void afterResponse(boolean hasMore) {
        if (!hasMore) {
            onLoadMoreFinish();
        }
    }


    public void clearEmptyView(ListView mLv) {
        View v = mLv.getEmptyView();
        if (null != v) {
            v.setVisibility(View.GONE);
        }
        mLv.setEmptyView(null);
    }

}
