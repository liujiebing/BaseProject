package com.platform.ui;

import android.os.Bundle;

import com.platform.presenter.DialogActContract;

/**
 * Created by Droideek on 2016/2/23.
 */
public abstract class BaseDialogActivity<T extends DialogActContract.Presenter> extends BaseFragmentActivity<T> implements DialogActContract.View  {
    //protected DialogActPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter.onCreate(this);
    }

    @Override
    public void finish() {
        super.finish();
        mPresenter.onFinish(this);
    }


}
