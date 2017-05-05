package com.platform.presenter;

import android.app.Activity;

/**
 * Created by Droideek on 2016/6/3.
 */
public class DialogActContract {

    public interface View extends BaseContract.View {
       
    }

    public interface Presenter extends BaseContract.Presenter{

        void onCreate(Activity context);

        void onFinish(Activity context);

        void setMatchWidth(Activity context, float percent);
    }
}
