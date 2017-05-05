package com.platform.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageButton;

import com.droideek.inject.Injector;
import com.droideek.net.ApiClient;
import com.platform.BaseApplication;
import com.platform.data.MsgTO;
import com.platform.data.Response;
import com.platform.ui.widget.ErrorView;
import com.xg.platform.R;
import com.droideek.util.handler.HandlerObserver;
import com.platform.presenter.BaseContract;
import com.droideek.ui.actionbar.ActionBar;
import com.droideek.ui.custom.SwitchIpFragment;
import com.droideek.util.DeviceUtils;
import com.droideek.util.LogUtil;
import com.droideek.util.ToastUtil;
import com.droideek.util.Util;


public abstract class BaseFragmentActivity<T extends BaseContract.Presenter> extends AppCompatActivity implements BaseContract.View , HandlerObserver {
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

        if (LogUtil.isDebug) { //TODO DELETE TEST;
            mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    int X_MIN_DISTANCE = DeviceUtils.deviceWidth()/2;
                    int X_MIN_VELOCITY = -3000;

                    if ((e1.getX() - e2.getX() > X_MIN_DISTANCE) && velocityX < X_MIN_VELOCITY &&
                            (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY() - e2.getY()))) {

                        final SwitchIpFragment dialog = new SwitchIpFragment();
                        dialog.setSelectListener(new SwitchIpFragment.SelectListener() {
                            @Override
                            public void confirm(String newIp) {
                                 ApiClient.COMMON_URL = newIp;
                                    BaseApplication.GoToInterface goToInterface = BaseApplication.getGoToInterface();
                                    if (null != goToInterface) {
                                        goToInterface.switchIp(BaseFragmentActivity.this);
                                    }

                                dialog.dismiss();
                            }
                        });
                        dialog.show(getFragmentManager(), "switchip"); // TODO: 17/1/12 open when debug

                    }
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
            });
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (LogUtil.isDebug && null !=mGestureDetector) {
            mGestureDetector.onTouchEvent(ev);
        }
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
        //StatisticUtil.onResume(this, getName());
    }

    @Override
    public void onPause() {
        super.onPause();


        mPresenter.onPause();
        onPauseStatistic();
    }

    public void onPauseStatistic() {
        //StatisticUtil.onPause(this, getName());
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
