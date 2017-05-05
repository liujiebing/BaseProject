package com.platform.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageButton;

import com.droideek.inject.Injector;

import com.platform.data.MsgTO;
import com.platform.ui.widget.ErrorView;
import com.platform.BaseApplication;
import com.xg.platform.R;
import com.platform.data.Response;
import com.droideek.util.handler.HandlerObserver;
import com.platform.presenter.BaseContract;
import com.droideek.ui.actionbar.ActionBar;
import com.droideek.util.ToastUtil;
import com.droideek.util.Util;

/**
 * Created by Droideek on 17/1/14.
 */

public abstract class BaseActivity <T extends BaseContract.Presenter> extends Activity  implements BaseContract.View , HandlerObserver {
    protected final String TAG = this.getClass().getSimpleName();
    //这里标记是否已经发起过一次TOKEN失效引起的登录操作。如果已经发起过一次，但用户取消了的话，只弹出一条消息，不再弹出登录框
    private boolean isLoginRequested = false;

    public T mPresenter;

    protected GestureDetector mGestureDetector;
    View mErrorView;

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == MSG_DISMISS_TOAST) {
            hideToast();
        }

//        if (Command.cmd_err == msg.what) {
//            HttpJsonResponse response = (HttpJsonResponse) msg.obj;
//            showToast(response.getMsg());
//        }
    }


    @Override
    public String getName() {
        return TAG;
    }

    @Override
    public int getStyleID() {
        return 0;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void setContentView(int id) {
        super.setContentView(id);
    }

    protected void setBackGroundColor() {
        setBackGroundColor(R.color.x_default_bg);
    }

    protected final void setBackGroundColor(int colorRes) {
        getWindow().setBackgroundDrawable(getResources().getDrawable(colorRes));
    }

    @Override
    public void initSaveInstance(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            Injector.onRestore(this, savedInstanceState);

            loadInstance(savedInstanceState);
        } else {
            initDataBundle(getIntent().getExtras());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setBackGroundColor();
        initSaveInstance(savedInstanceState);

        //JsonClient.instance(getApplicationContext());
        setContentView(getLayoutID());

        setPresenter();

        mPresenter.setActionbar((ActionBar) findViewById(R.id.action_bar), (ImageButton) findViewById(R.id.actionbar_home_btn));

        initView();


    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);
    }

    public abstract void initView();

    public void loadInstance(Bundle savedInstanceState) {

    }

    public void setTitle(String title) {
        mPresenter.setTitle(title);
    }

    public void setTitle(int resId) {
        mPresenter.setTitle(resId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {


        super.onSaveInstanceState(outState);
        Injector.save(this, outState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    public void onStart() {


        super.onStart();
    }

    @Override
    public void onResume() {


        super.onResume();

        if (!BaseApplication.isActive()) {
            BaseApplication.setActive(true);
        }

        onResumeStatistic();
    }

    public void onResumeStatistic() {
       // StatisticUtil.onResume(this, getName());
    }

    @Override
    public void onPause() {
        super.onPause();


        mPresenter.onPause();
        onPauseStatistic();
    }

    public void onPauseStatistic() {
      //  StatisticUtil.onPause(this, getName());
    }

    @Override
    public void onStop() {
        super.onStop();


        mPresenter.onStop();

        if (!Util.isAppOnForeground(this)) {
            BaseApplication.setActive(false);
        }
    }

    @Override
    public void onDestroy() {


        try {
            super.onDestroy();

            View root = getWindow().getDecorView().findViewById(android.R.id.content);
            Util.unbindDrawables(root);

            mPresenter.onDestroy();

            if (!Util.isAppOnForeground(this) && BaseApplication.isActive()) {
                BaseApplication.setActive(false);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }

        hideToast();
        ToastUtil.cancelAll();
    }


    private void hideToast() {
        if (null != toast && toast.isShowing()) {
            toast.dismiss();
            toast = null;
        }
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
        if (null != v && v instanceof ErrorView) {
            ErrorView view = (ErrorView) v.findViewById(R.id.err_root);
            this.mErrorView = view;
            view.setReloadListener(new ReloadListener() {
                @Override
                public void reload() {
                    loadData();
                }
            });
            if(data!=null){
                showToast(data.msgId);
                view.populate(data);
            }
        }
    }

    /**
     * 加载页面数据;
     */
    protected void loadData() {

    }

    private static final int MSG_DISMISS_TOAST = -33333;
    private AlertDialog toast = null;

    protected void showToast(int id) {
        mPresenter.showToast(id);
    }

    protected void showToast(String msg) {
        mPresenter.showToast(msg);
    }

    public void showDialogProgress() {
        mPresenter.showDialogProgress();
    }

    public void onActionBarItem(int itemId){

        mPresenter.onActionBarItem(itemId);
    }

    public void hideDialogProgress() {
        mPresenter.hideDialogProgress();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if ((event.getKeyCode() == KeyEvent.KEYCODE_BACK) && (event.getAction() == KeyEvent.ACTION_UP)) {
            mPresenter.goBack();
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onHttpFailed(Response response, boolean showMsg) {

    }

    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
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

    protected void onPermissionResult(int requestCode, boolean isAllow) {};
}
