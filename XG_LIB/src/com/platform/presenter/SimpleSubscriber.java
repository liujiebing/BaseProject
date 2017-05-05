package com.platform.presenter;

import com.droideek.net.ResponseFailedException;
import com.xg.platform.R;
import com.platform.data.MsgTO;

import java.net.ConnectException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by Droideek on 2016/8/11.
 */
public class SimpleSubscriber<T> extends Subscriber<T> {
    protected BaseContract.Presenter mBasePresenter;
    boolean showPageError; //是否显示错误页面
    boolean isRefresh = true; //是否是第一页，即刷新的情况， 相对加载更多。

    public SimpleSubscriber(BaseContract.Presenter presenter) {
        this(presenter, true, true);
    }

    /**
     * 如果LISTVIEW需要显示错误页面。使用这个构造。
     *
     * @param presenter
     * @param showPageError
     * @param isRefresh
     */
    public SimpleSubscriber(BaseContract.Presenter presenter, boolean showPageError, boolean isRefresh) {
        this.mBasePresenter = presenter;
        this.showPageError = showPageError;
        this.isRefresh = isRefresh;
    }

    @Override
    public void onNext(T response) {
        mBasePresenter.onHttpSuccess();
    }

    @Override
    public void onCompleted() {
        mBasePresenter.onHttpCompleted();
    }

    @Override
    public void onError(Throwable e) {
        try {
            if (e instanceof ResponseFailedException) {
                ResponseFailedException exception = (ResponseFailedException) e;
                if (null != exception) {
                    onFailed(exception);
                    return;
                }
            }

        } catch (Throwable t) {
            //noTHING;

        }

        if(e instanceof UnknownHostException||e instanceof ConnectException){
            onNetError();
        }else{
            onErr();
        }

    }

    /**
     * 网络访问异常
     */
    public void onNetError() {
        try{
            mBasePresenter.onHttpError(showPageError,new MsgTO(MsgTO.TYPE_NET_ERROR, R.string.x_error_net_req), isRefresh);
        }catch (Throwable throwable){

        }
    }

    /**
     * 服务异常
     * @param exception
     */
    public void onFailed(ResponseFailedException exception) {
        try {
            mBasePresenter.onHttpFailed(exception.response, showPageError);
        } catch (Throwable throwable) {
            onErr();
        }
    }


    public void onErr() {
        try {
            mBasePresenter.onHttpError(showPageError,new MsgTO(MsgTO.TYPE_PROGRAM_ERROR, R.string.x_error_program), isRefresh);
        } catch (Throwable throwable) {
            //NOTHING
        }
    }
}
