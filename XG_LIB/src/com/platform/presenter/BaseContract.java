package com.platform.presenter;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.widget.ImageButton;

import com.platform.data.MsgTO;
import com.platform.data.Response;
import com.droideek.util.handler.HandlerObserver;
import com.droideek.ui.actionbar.ActionBar;
import com.droideek.ui.custom.LoadingDialog;

/**
 * Created by Droideek on 2016/5/9.
 */
public class BaseContract {
    public interface View extends BaseView{

        Activity getActivity();

        String getName();

        void onHttpError(boolean showPageError, MsgTO data, boolean isRefresh);

        void onHttpFailed(Response response, boolean showMsg);

        void onHttpSuccess();

        int getLayoutID();

        int getStyleID();

        void initDataBundle(Bundle bundle); //接收传入数据。

        void initSaveInstance(Bundle savedInstanceState);

        void onActionBarItem(int itemId);

        void hideProgress();

    }

    public interface Presenter extends IBasePresenter {

        LoadingDialog getDialog();

        void hideKeyboard();

        void onHttpFailed(Response response, boolean showMsg);

        void onHttpError(boolean showPageError, MsgTO data, boolean isRefresh);

        void onHttpSuccess();

        void onDestroy();

        void initBackImgBtn(ImageButton backBtn);

        void refreshCompleted();

        void onPause();

        void onStop();

        void initHandler(HandlerObserver handlerObserver);

        void removeHandler(HandlerObserver handlerObserver);

        //-------------ActionBarInterface-------------
        void hideActionBar();

        void setHomeAction(boolean isHome);

        void setTitle(int resid);

        void setTitle(String text);

        void setImageLogo(int resid);

        android.view.View setMyView(int layoutid);

        android.view.View  addAction(int view_id, android.view.View item);

        android.view.View  addAction(int view_id, int res_id);

        android.view.View  addRefreshAction(int view_id, int res_id);

        android.view.View  addButtonAction(int view_id, int res_id);

        android.view.View  addButtonAction(int view_id, int res_id, int bg_id);

        void onActionBarItem(int itemid);

        void removeAllActions();

        void removeActionAt(int index);

        ActionBar getActionbar();

        void setActionbar(ActionBar actionbar);

        void setActionbar(ActionBar actionbar, ImageButton backBtn);

        //--------DIALOG-------

        void showToast(int id);

        void showToast(String msg);

        void showDialogProgress();

        void showDialogProgress(int type);

        boolean sendEmptyMessage(int what);

        boolean sendMessage(int what, Bundle bundle);

        boolean sendMessage(int what, Object obj);

        boolean sendMessage(int what, int arg1, Object obj);

        boolean sendEmptyMessageAtTime(int what, long uptimeMillis);

        boolean sendEmptyMessageDelayed(int what, long delayMillis);

        void removeCallbacksAndMessages(Object token);

        void removeMessages(int what);

        void setProgressBarVisibility(int visibility);

        void hideDialogProgress();

        void setStatusBar(int colorRes);

        Object getResponseValue(Message msg);

        void onHttpCompleted();

        void goBack();

    }
}
