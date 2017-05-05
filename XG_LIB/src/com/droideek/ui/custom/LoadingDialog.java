package com.droideek.ui.custom;

import android.app.Dialog;
import android.content.Context;
import android.widget.FrameLayout;

import com.xg.platform.R;

/**
 * Created by Droideek on 2016/1/20.
 */
public class LoadingDialog extends Dialog {
    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_FULLSCREEN = 1;
    public static final int TYPE_TRANSPARENT = 2;
    private FrameLayout rootView;

    public LoadingDialog(Context context) {
        super(context, R.style.Theme_Dialog);

        applyCompat();

        setContentView(R.layout.xg_dialog_base);
        setCancelable(true);
        setCanceledOnTouchOutside(false);

        rootView = (FrameLayout) findViewById(R.id.content_root);

    }

    private void applyCompat() {
//        if (Build.VERSION.SDK_INT < 19) {
//            return;
//        }
//        getWindow().setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    /*******************
     * default false
     * *****************/
    public void setFullScreen(int type) {
        switch (type) {
            case TYPE_FULLSCREEN:
                rootView.setBackgroundColor(0xffffffff);
                break;
            case TYPE_TRANSPARENT:
            case TYPE_DEFAULT:
                rootView.setBackgroundColor(0x00ffffff);
                break;

            default:
                break;
        }
    }
}
