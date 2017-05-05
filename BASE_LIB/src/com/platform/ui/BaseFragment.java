package com.platform.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageButton;

import com.droideek.inject.Injector;
import com.platform.data.MsgTO;
import com.platform.data.Response;
import com.platform.ui.widget.ErrorView;
import com.xg.platform.R;
import com.droideek.util.handler.HandlerObserver;
import com.platform.presenter.BaseContract;
import com.droideek.ui.actionbar.ActionBar;

/**
 * Created by Droideek on 2015/12/27.
 */
public abstract class BaseFragment<T extends BaseContract.Presenter> extends Fragment implements BaseContract.View , HandlerObserver{
    protected final String TAG = this.getClass().getSimpleName();

    public T mPresenter;
    View mErrorView;

    @Override
    public String getName() {
        return TAG;
    }

    @Override
    public int getStyleID() {
        return 0;
    }

    public final View findViewById(int id) {
        if (id < 0 || getView() == null) {
            return null;
        }
        return getView().findViewById(id);
    }

    public abstract void onClick(View view);

    @Override
    public void handleMessage(Message msg) {
//        if (Command.cmd_err == msg.what) {
//            HttpJsonResponse response = (HttpJsonResponse) msg.obj;
//            showToast(response.getMsg());
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPresenter();

    }


    @Override
    public void initSaveInstance(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            Injector.onRestore(this, savedInstanceState);
        }else{
            initDataBundle(getArguments());
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        initSaveInstance(savedInstanceState);

        View mRoot = getRootView(inflater, container);
        View view = mRoot.findViewById(R.id.action_bar);
        if (view != null) {
            mPresenter.setActionbar((ActionBar) view, (ImageButton) mRoot.findViewById(R.id.actionbar_home_btn));
        }

        return mRoot;
    }


    protected View getRootView(LayoutInflater inflater, ViewGroup container) {
        if (getStyleID() == 0) {
            return inflater.inflate(getLayoutID(), container, false);
        } else {
            try {
                final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), getStyleID());
                LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
                return localInflater.inflate(getLayoutID(), null, false);
            } catch (Throwable e) {
                return inflater.inflate(getLayoutID(), container, false);
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initView();
    }

    protected abstract void initView();


    public void setTitle(String title) {
        mPresenter.setTitle(title);
    }

    public void setTitle(int resId) {
        mPresenter.setTitle(resId);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        Injector.save(this, outState);
       // saveStateToArguments();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onResume() {
        super.onResume();


       // StatisticUtil.onPageStart(getActivity(), getName());
    }

    @Override
    public void onPause() {
        super.onPause();


        mPresenter.onPause();
       // StatisticUtil.onPageEnd(getActivity(), getName());
    }

    @Override
    public void onStop() {
        super.onStop();


        mPresenter.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }


    @Override
    public void onDetach() {
        super.onDetach();

    }


    protected void showToast(int id) {
        mPresenter.showToast(id);
    }

    protected void showToast(String msg) {
        mPresenter.showToast(msg);
    }

    @Override
    public void onHttpError(boolean showPageError, MsgTO data, boolean isRefresh) {
        if(data!=null){
            showToast(data.msgId);
        }
    }

    @Override
    public void onHttpSuccess() {
        if (null != mErrorView) {
            mErrorView.setVisibility(View.GONE);
        }
    }

    public void showErrorView(MsgTO data) {
        View v = ((ViewStub)findViewById(R.id.v_error)).inflate();
        this.mErrorView = v;
        if (null != v && v instanceof ErrorView) {
            ErrorView view = (ErrorView) v;
            if(data!=null){
                showToast(data.msgId);
                view.setReloadListener(new ReloadListener() {
                    @Override
                    public void reload() {
                        loadData();
                    }
                });
                view.populate(data);
            }
        }
    }

    /**
     * 加载页面数据;
     */
    protected void loadData() {

    }

    protected void showDialogProgress() {
        mPresenter.showDialogProgress();
    }

    @Override
    public void onActionBarItem(int itemId){
        mPresenter.onActionBarItem(itemId);
    }


    protected void hideDialogProgress() {
        mPresenter.hideDialogProgress();
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onHttpFailed(Response response, boolean showMsg) {
        
    }

    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        if(grantResults != null){
            boolean isAllow=true;
            for (int i = 0; i < grantResults.length; i++) {
                if (PackageManager.PERMISSION_GRANTED != grantResults[i]) {
                    isAllow=false;
                    break;
                }
            }
            onPermissionResult(requestCode,isAllow);
        }
    }

    protected void onPermissionResult(int requestCode, boolean isAllow){};

}
