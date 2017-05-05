package com.platform.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.droideek.entry.data.Entry;
import com.droideek.entry.widget.Populatable;
import com.xg.platform.R;
import com.platform.data.MsgTO;
import com.platform.ui.ReloadListener;
import com.droideek.util.DeviceUtils;

/**
 * Created by Droideek on 2016/1/4.
 */
public class ErrorView extends LinearLayout implements Populatable<Entry>, View.OnClickListener{
    private TextView tv_msg;
    private TextView tv_commit;
    private ImageView iv_logo;
    private MsgTO mData;
    private ReloadListener reloadListener;

    public ErrorView(Context context) {
        super(context);
        initView();
    }

    public ErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public ErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        if (isInEditMode()) {
            return;
        }

        setOrientation(VERTICAL);
        inflate(getContext(), R.layout.x_error_default, this);

        setPadding(0, DeviceUtils.dp2px(50), 0, 0);
        setBackgroundColor(Color.argb(255, 255, 255, 255));

        iv_logo = (ImageView) findViewById(R.id.err_img);
        tv_msg = (TextView) findViewById(R.id.v_error_msg);
        tv_commit = (TextView) findViewById(R.id.err_commit);
    }

    @Override
    public void populate(Entry data) {
        if (data == null) {
            return;
        }

        if (data instanceof MsgTO) {
            MsgTO obj = (MsgTO) data;
            setData(obj);
            mData = obj;
        }
    }

    private void setData(MsgTO obj) {

        switch (obj.msgType) {

            case MsgTO.TYPE_NET_ERROR:
                iv_logo.setImageResource(R.drawable.xg_net_error);
                tv_msg.setText(R.string.x_error_net_req);
                tv_commit.setText(R.string.x_click_refresh);
                tv_commit.setOnClickListener(this);
                break;
            case MsgTO.TYPE_PROGRAM_ERROR:
            default:
                iv_logo.setImageResource(R.drawable.xg_empty_icon);
                tv_commit.setVisibility(View.GONE);
                tv_msg.setText(R.string.x_error_program);
                break;

        }
    }

    @Override
    public void onClick(View v) {
        if (R.id.err_commit == v.getId()) {
            if(reloadListener!=null){
                reloadListener.reload();
            }
        }
    }

    public void setReloadListener(ReloadListener reloadListener){
        this.reloadListener=reloadListener;
    }


}
