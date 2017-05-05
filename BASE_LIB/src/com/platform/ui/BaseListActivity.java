package com.platform.ui;

import android.view.View;
import android.widget.ListView;

import com.xg.platform.R;
import com.platform.data.MsgTO;
import com.platform.helper.UIHelper;
import com.platform.presenter.BaseContract;
import com.droideek.ui.adapter.BaseItemAdapter;
import com.droideek.util.Util;

import java.util.List;

/**
 * Created by Droideek on 2016/1/11.
 */
public abstract class BaseListActivity<T extends BaseContract.Presenter> extends BaseFragmentActivity<T> implements ReloadListener {
    protected ListView mLv;
    protected BaseItemAdapter mAdapterList;

    public void initView(boolean autoRequest) {
        mLv = (ListView) findViewById(R.id.lv);
        //mLv.setVisibility(View.GONE); //这里先隐藏，是因为可能会增加header和footer.当还没有内容的时候应该隐藏它们
        Util.setListView(mLv);
        addHeaderFooter();
        setAdapter();

        if (autoRequest) {
            requestList();
        }
    }

    protected void setDivider(int heightRes, int colorRes) {
        if (null != mLv) {
            mLv.setDivider(getResources().getDrawable(colorRes));
            mLv.setDividerHeight(getResources().getDimensionPixelSize(heightRes));
        }
    }

    protected abstract void setAdapter();

    protected void addHeaderFooter() {

    }

    @Override
    public void initView() {
        initView(true);
    }

    protected void refresh() {
        requestList();
       // mLv.setVisibility(View.GONE);
    }

    protected void requestList() {

    }

    protected void showProgress() {

        View progress = findViewById(R.id.v_progress);
        if (null != progress) {
            progress.setVisibility(View.VISIBLE);
        }

        View progressBg = findViewById(R.id.iv_pb_bg);
        if (null != progressBg) {
            progressBg.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void hideProgress() {


        View progress = findViewById(R.id.v_progress);
        if (null != progress) {
            progress.setVisibility(View.GONE);

        }

        View progressBg = findViewById(R.id.iv_pb_bg);
        if (null != progressBg) {
            progressBg.setVisibility(View.GONE);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mAdapterList != null) {
            mAdapterList.clear();
            mAdapterList.notifyDataSetChanged();
        }
    }

    public void onResponse(BaseItemAdapter mAdapterList) {
        onResponse(mAdapterList, null);
    }

    public void onResponse(BaseItemAdapter mAdapterList, ListDataOnListener mListDataOnListener) {
        // 因为每次都取全部数据，所以每次都clear()

        mAdapterList.notifyDataSetChanged();

        List data = mAdapterList.getData();
        if (mListDataOnListener != null) {
            mListDataOnListener.onSetListData(data);
        } else {
            if (null != data && data.size() > 0) {
                afterResponse();
            } else {
                showEmptyView(mAdapterList);
            }
        }

        mAdapterList.notifyDataSetChanged();
    }

    protected void showEmptyView(BaseItemAdapter mAdapterList) {

    }

    protected void afterResponse() {
        if(View.VISIBLE!=mLv.getVisibility()){
            hideProgress();
            mLv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onHttpError(boolean showPageError, MsgTO data, boolean isRefresh) {
        hideProgress();
        if(data==null){
            return;
        }
        if (showPageError) {
            if (isRefresh) {
                UIHelper.showErrorView(getActivity(), mLv, data ,this);
            } else {
                super.onHttpError(showPageError,data, isRefresh);
            }
        }
    }

    @Override
    public void reload() {
        showDialogProgress();
        refresh();
    }
}
