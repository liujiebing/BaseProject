package com.platform.helper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListView;

import com.platform.data.MsgTO;
import com.platform.ui.ReloadListener;
import com.platform.ui.widget.ErrorView;
import com.xg.platform.R;
import com.droideek.util.DeviceUtils;
import com.droideek.util.ToastUtil;

/**
 * Created by Droideek on 2016/8/12.
 */
public class UIHelper {

    public static void showErrorView(Context context, ListView mLv, int emptyType) {
        showErrorView(context, mLv, new MsgTO(emptyType), null);
    }

    public static void showErrorView(Context context, ListView mLv, MsgTO data, ReloadListener mListener) {
        clearEmptyView(mLv);
        if (null == mLv.getEmptyView()) {
            ErrorView error = new ErrorView(context);
            error.setReloadListener(mListener);
            fitParentEmptyView(mLv, error);
            mLv.setEmptyView(error);
            error.populate(data);
        }else{
            if(!DeviceUtils.isNetworkConnected(context)){
                ToastUtil.showAppToast(context.getResources().getString(R.string.x_msg_check_net));
            }

        }
    }

    public static boolean fitParentEmptyView(ListView mLv, View empty) {
        ViewParent parent = mLv.getParent();
        if (null != parent && parent instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) parent;
            int height = group.getHeight();
            group.addView(empty,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height==0?ViewGroup.LayoutParams.MATCH_PARENT:height));
            return true;
        }
        return false;
    }


    public static void clearEmptyView(ListView mLv) {
        View empty = mLv.getEmptyView();
        if(empty!=null &&!(empty instanceof ErrorView)){
            ((ViewGroup) mLv.getParent()).removeView(empty);
            mLv.setEmptyView(null);
        }
    }

    public static void clearErrorView(ListView mLv){
        View empty = mLv.getEmptyView();
        if(empty!=null &&(empty instanceof ErrorView)){
            ((ViewGroup) mLv.getParent()).removeView(empty);
            mLv.setEmptyView(null);
        }
    }

}
