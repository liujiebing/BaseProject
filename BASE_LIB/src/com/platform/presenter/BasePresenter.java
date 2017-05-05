package com.platform.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;

import com.droideek.ui.actionbar.ActionBar;
import com.droideek.ui.actionbar.ActionBarInterface;
import com.droideek.ui.custom.LoadingDialog;
import com.droideek.util.AppStatusTracker;
import com.droideek.util.IntentUtil;
import com.droideek.util.LogUtil;
import com.droideek.util.StatusBarUtil;
import com.droideek.util.ToastUtil;
import com.droideek.util.handler.HandlerObserver;
import com.droideek.util.handler.HandlerUtil;
import com.platform.BaseApplication;
import com.platform.data.MsgTO;
import com.platform.data.Response;
import com.xg.platform.R;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Droideek on 2016/5/9.
 */
public class BasePresenter<T extends BaseContract.View> implements BaseContract.Presenter {
    public static String TAG = BasePresenter.class.getSimpleName();

    public static final String CODE_INVALID_TOKEN = "-100";

    public T mView;
    public Context mContext;

    public BasePresenter(Context context, T view) {
        this(view);
        this.mContext = context;
    }


    public BasePresenter(T view) {
        this.mView = view;
    }

    protected void unSubscribe(Subscriber subscriber) {
        if (null != subscriber) {
            subscriber.unsubscribe();
            subscriber = null;
        }
    }

    @Override
    public void unSubscribe() {

    }

    @Override
    public void onHttpSuccess() {
        mView.onHttpSuccess();
    }

    /**
     * 错误情况的处理 要跟 FAILED要区别开。
     *
     * @param showPageError
     * @param isRefresh
     */
    @Override
    public void onHttpError(boolean showPageError, MsgTO data, boolean isRefresh) {

        refreshCompleted();
        mView.onHttpError(showPageError,data, isRefresh);
    }

    @Override
    public void onHttpCompleted() {


        refreshCompleted();
    }

    protected boolean isRefresh(String curPage) {
        return "1".equals(curPage);
    }

    protected boolean hasMore(List list) {
        return !(null == list || list.size() <= 0);
    }

    public void onHttpFailed(Response response, boolean showMsg) {


        refreshCompleted();

        if (null != response && CODE_INVALID_TOKEN.equals(response.code)) {
            BaseApplication.GoToInterface goToInterface = BaseApplication.getGoToInterface();
            if (null != goToInterface) {
                goToInterface.logout(mView.getActivity(), "");
            }
        }

        if (showMsg) {
            showToast(null == response ? "失败了" : response.msg);
        }
        mView.onHttpFailed(response, showMsg);
    }


    @Override
    public LoadingDialog getDialog() {
        return null;
    }

    /**
     * 隐藏输入法面板
     */
    @Override
    public void hideKeyboard() {
        InputMethodManager imm = ((InputMethodManager) mView.getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE));
        if (imm != null && mView.getActivity().getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(mView.getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @Override
    public void initBackImgBtn(ImageButton backBtn) {
        if (null != actionbar) {
            if (backBtn != null) {
                backBtn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mView.onActionBarItem(R.id.actionbar_home_btn);
                    }
                });
            }
        }
    }

    @Override
    public void initHandler(HandlerObserver handlerObserver) {
        HandlerUtil.instance().addObserver(handlerObserver);
    }

    @Override
    public void removeHandler(HandlerObserver handlerObserver) {
        if (null == handlerObserver) return;
        HandlerUtil.instance().deleteObserver(handlerObserver);
    }


    //    @Override
//    public void onResume(HandlerObserver handlerObserver) {
//        initHandler(handlerObserver);
//    }

    @Override
    public void onPause() {
        hideDialogProgress();
    }

    @Override
    public void onStop() {
        // removeHandler(handlerObserver);
        refreshCompleted();
    }

    @Override
    public void onDestroy() {
        //removeCallbacksAndMessages(null);
        unSubscribe();
        hideKeyboard();
    }


    // -----------进度DIALOG-----------

    @Override
    public void refreshCompleted() {
        mView.hideProgress();
        hideDialogProgress();
        removeRefreshProgressBar();
    }

    public void removeRefreshProgressBar() {
        setProgressBarVisibility(View.GONE);
    }

    private LoadingDialog dialog;

    @Override
    public void showDialogProgress() {
        showDialogProgress(LoadingDialog.TYPE_TRANSPARENT);
    }

    @Override
    public void showDialogProgress(int type) {
        //if (mView.getActivity() == null || mView.getActivity().isFinishing() || isHidden()) return;
        try {
            if (dialog == null) {
                dialog = new LoadingDialog(mView.getActivity());
            }
            if (type >= 0) {
                dialog.setFullScreen(type);
            }
            if (!dialog.isShowing()) dialog.show();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hideDialogProgress() {
        //if (mView.getActivity() == null || mView.getActivity().isFinishing()) return;
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    //-----------------消息处理----------------------------------------
    private Message obtain(int what) {
        return obtain(what, 0, mView.getName());
    }

    private Message obtain(int what, Object obj) {
        return obtain(what, 0, obj);
    }

    private Message obtain(int what, int arg1, Object obj) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        msg.arg1 = arg1;
        msg.arg2 = mView.getName().hashCode();
        return msg;
    }

    private Message obtain(int what, Bundle bundle) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.setData(bundle);
        msg.arg2 = mView.getName().hashCode();

        return msg;
    }

    @Override
    public boolean sendMessage(int what, int arg1, Object obj) {
        return HandlerUtil.instance().sendMessage(obtain(what, arg1, obj));
    }

    @Override
    public boolean sendMessage(int what, Object obj) {
        return HandlerUtil.instance().sendMessage(obtain(what, obj));
    }

    @Override
    public boolean sendEmptyMessage(int what) {
        return HandlerUtil.instance().sendMessage(obtain(what));
    }

    @Override
    public boolean sendEmptyMessageAtTime(int what, long uptimeMillis) {
        return HandlerUtil.instance().sendMessageAtTime(obtain(what), uptimeMillis);
    }

    @Override
    public boolean sendEmptyMessageDelayed(int what, long delayMillis) {
        return HandlerUtil.instance().sendMessageDelayed(obtain(what), delayMillis);
    }

    @Override
    public boolean sendMessage(int what, Bundle bundle) {
        return HandlerUtil.instance().sendMessage(obtain(what, bundle));
    }

    @Override
    public void removeMessages(int what) {
        HandlerUtil.instance().removeMessages(what);
    }

    @Override
    public void removeCallbacksAndMessages(Object token) {
        HandlerUtil.instance().removeCallbacksAndMessages(token);
    }


    @Override
    public void showToast(int id) {
        ToastUtil.showToast(mView.getActivity(), id);
    }


    @Override
    public void showToast(String msg) {
        ToastUtil.showToast(mView.getActivity(), msg);
    }


    // -----------------自定义actionBar;-----------------------------------------
    private ActionBarInterface actionbarInterface;

    protected ActionBar actionbar;

    @Override
    public ActionBar getActionbar() {
        return actionbar;
    }

    @Override
    public void setActionbar(ActionBar actionbar) {
        this.actionbar = actionbar;
    }

    @Override
    public void setActionbar(ActionBar actionbar, ImageButton backBtn) {
        setActionbar(actionbar);
        initBackImgBtn(backBtn);
    }

    public void setActionbar(ActionBarInterface actionbar) {
        this.actionbarInterface = actionbar;
    }

    @Override
    public void hideActionBar() {
        if (actionbar != null)
            actionbar.hideActionBar();
        else if (actionbarInterface != null)
            actionbarInterface.hideActionBar();
    }

    @Override
    public void setHomeAction(boolean isHome) {
        if (actionbar != null)
            actionbar.setHomeAction(isHome);
        else if (actionbarInterface != null)
            actionbarInterface.setHomeAction(isHome);
    }

    @Override
    public void setTitle(int resid) {
        if (actionbar != null)
            actionbar.setTitle(resid);
        else if (actionbarInterface != null)
            actionbarInterface.setTitle(resid);
    }

    @Override
    public void setTitle(String text) {
        if (actionbar != null)
            actionbar.setTitle(text);
        else if (actionbarInterface != null)
            actionbarInterface.setTitle(text);
    }

    @Override
    public void setImageLogo(int resid) {
        if (actionbar != null)
            actionbar.setImageLogo(resid);
        else if (actionbarInterface != null)
            actionbarInterface.setImageLogo(resid);
    }

    @Override
    public View setMyView(int layoutid) {
        if (actionbar != null)
            return actionbar.setMyView(layoutid);
        else if (actionbarInterface != null)
            return actionbarInterface.setMyView(layoutid);
        return null;
    }

    @Override
    public View addAction(int view_id, int res_id) {
        View item = null;
        if (actionbar != null)
            item = actionbar.addAction(view_id, res_id);
        else if (actionbarInterface != null)
            item = actionbarInterface.addAction(view_id, res_id);
        setOnActionItemClick(item);
        return item;
    }

    @Override
    public View addAction(int view_id, View item) {
        if (actionbar != null)
            actionbar.addAction(view_id, item);
        else if (actionbarInterface != null)
            actionbarInterface.addAction(view_id, item);
        setOnActionItemClick(item);
        return item;
    }

    @Override
    public View addButtonAction(int view_id, int res_id, int bg_id) {
        View item = null;
        if (actionbar != null)
            item = actionbar.addButtonAction(view_id, res_id, bg_id);
        else if (actionbarInterface != null)
            item = actionbarInterface.addButtonAction(view_id, res_id, bg_id);
        setOnActionItemClick(item);
        return item;
    }

    @Override
    public View addButtonAction(int view_id, int res_id) {
        View item = null;
        if (actionbar != null)
            item = actionbar.addButtonAction(view_id, res_id, 0);
        else if (actionbarInterface != null)
            item = actionbarInterface.addButtonAction(view_id, res_id, 0);
        setOnActionItemClick(item);
        return item;
    }

    @Override
    public View addRefreshAction(int view_id, int res_id) {
        View item = null;
        if (actionbar != null)
            item = actionbar.addRefreshAction(view_id, res_id);
        else if (actionbarInterface != null)
            item = actionbarInterface.addRefreshAction(view_id, res_id);
        setOnActionItemClick(item);
        return item;
    }

    @Override
    public void removeActionAt(int index) {
        if (actionbar != null)
            actionbar.removeActionAt(index);
        else if (actionbarInterface != null)
            actionbarInterface.removeActionAt(index);
    }

    @Override
    public void removeAllActions() {
        if (actionbar != null)
            actionbar.removeAllActions();
        else if (actionbarInterface != null)
            actionbarInterface.removeAllActions();
    }

    @Override
    public void setProgressBarVisibility(int visibility) {
        if (actionbar != null)
            actionbar.setProgressBarVisibility(visibility);
        else if (actionbarInterface != null)
            actionbarInterface.setProgressBarVisibility(visibility);
    }

    private void setOnActionItemClick(View item) {
        if (item == null) return;
        item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int id = (Integer) v.getTag();
                mView.onActionBarItem(id);
            }
        });
    }

    @Override
    public void onActionBarItem(int itemId) {
        if (R.id.actionbar_home_btn == itemId) {
            goBack();
        }
    }

    public void goBack() {

        if (AppStatusTracker.getInstance().isTaskBottom()) {
            if(LogUtil.isDebug) Log.d(TAG, "== goBack(); isTaskBottom mCurrentActivity:"+AppStatusTracker.mCurrentActivity);
            BaseApplication.GoToInterface to = BaseApplication.getGoToInterface();
            if (null != to) {
                to.start(AppStatusTracker.mCurrentActivity);
            }
        }

        if(LogUtil.isDebug) Log.d(TAG, "== goBack()");

        IntentUtil.finishActivity(mView.getActivity());
    }

    @Override
    public void setStatusBar(int colorRes) {
        Activity act = mView.getActivity();
        StatusBarUtil.setColor(act, act.getResources().getColor(colorRes), act.getResources().getColor(R.color.x_default_bg), 0);
    }

    @Override
    public Object getResponseValue(Message msg) {
        return null;
    }

}
