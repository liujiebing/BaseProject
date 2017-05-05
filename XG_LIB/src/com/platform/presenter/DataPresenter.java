package com.platform.presenter;

import rx.Subscriber;

/**
 * Created by Droideek on 2016/8/10.
 */
public class DataPresenter {
    protected BaseContract.Presenter mBasePresenter;

    public DataPresenter(BaseContract.Presenter basePresenter) {
        this.mBasePresenter = basePresenter;
    }

    protected void unSubscribe(Subscriber subscriber) {
        if (null != subscriber) {
            subscriber.unsubscribe();
            subscriber = null;
        }
    }


    public void unSubscribe() {

    }


}
