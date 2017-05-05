package com.droideek.ui.custom;

import android.app.DialogFragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.droideek.net.ApiClient;
import com.xg.platform.R;
import com.droideek.util.DeviceUtils;

/**
 * Created by Droideek on 2016/6/15.
 */
public class SwitchIpFragment extends DialogFragment implements View.OnClickListener {
    final String[] mIpName = new String[]{"开发", "测试", "随便写"};
    final String[] mIp = new String[]{"https://m.seagoor.com", "https://192.168.1.232:8480", "http://192.168.1.232:8480"};
    int margin;
    String mCurIP;
    EditText et_custom;
    String mHost;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("切换环境");
        margin = DeviceUtils.dp2px(10);
        mHost = ApiClient.COMMON_URL; //TODO CHECK COMMON URL
        mCurIP = ApiClient.COMMON_URL;

        View view = inflater.inflate(R.layout.xg_dialog_switch_ip, container);
        view.findViewById(R.id.v_confirm).setOnClickListener(this);
        et_custom = (EditText) view.findViewById(R.id.et_custom);
        if(!mIp[0].equals(mHost)&&!mIp[1].equals(mHost)){
            et_custom.setText(mHost);
            et_custom.setTextColor(Color.argb(255, 0, 0, 250));
        }
        initView((LinearLayout) view.findViewById(R.id.v_root));

        return view;
    }

    private void initView(LinearLayout view) {
        RadioGroup group = new RadioGroup(view.getContext());
        for (int i = 0, size = mIpName.length; i < size; i++) {
            createView(group, i);
        }
        view.addView(group, 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

    }

    private void createView(RadioGroup group, final int index) {
        RadioButton button = new RadioButton(getActivity());
        StringBuilder sb = new StringBuilder();
        sb.append(mIpName[index]);
        sb.append(":  ");
        sb.append(mIp[index]);

        button.setText(sb.toString());
        button.setTextSize(15);
        button.setTextColor(mHost.equals(mIp[index]) ? Color.argb(255, 0, 0, 250): Color.argb(255, 33, 33 ,33));
        button.setPadding(0, margin, 0, margin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (index == mIp.length-1 && TextUtils.isEmpty(et_custom.getText().toString())) {
                    et_custom.setText(mIp[mIp.length - 2]);
                    et_custom.setTextColor(Color.argb(255, 33, 33 ,33));
                }

                mCurIP = mIp[index];
                if (index < mIp.length-1) {
                    et_custom.setText("");
                }
            }
        });

        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(margin, 0, margin, 0);

        group.addView(button, params);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (R.id.v_confirm == id) {
            mCurIP = TextUtils.isEmpty(mCurIP) ? et_custom.getText().toString() : mCurIP;
            changeIP(mCurIP);
        }
    }

    private void changeIP(String newIp) {
        if (null != mListener) {
            mListener.confirm(newIp);
        }
    }

    SelectListener mListener;

    public void setSelectListener(SelectListener listener) {
        mListener = listener;
    }
    public interface SelectListener{
        void confirm(String value);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
