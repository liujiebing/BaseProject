package com.droideek.ui.actionbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class DoubleClickRelativeLayout extends RelativeLayout{

	public DoubleClickRelativeLayout(Context context) {
		super(context);
	}
	
	public DoubleClickRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DoubleClickRelativeLayout(Context context, AttributeSet attrs,
									 int defStyle) {
		super(context, attrs, defStyle);
	}

	private OnDoubleClickListener mOnDoubleClickListener;
	
	private static final int DELAY_MILLIS = 500;
	
	public void setOnDoubleClickListener(OnDoubleClickListener onDoubleClickListener){
		this.mOnDoubleClickListener = onDoubleClickListener;
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(mOnDoubleClickListener != null){
					doubleEven(DELAY_MILLIS);
				}
			}
		});
	}
	
	private void doubleEven(int delayMillis) {
		//repress_to_exit_app
		if(!dRunnable.getIsRun()){
			dRunnable.setIsRun(true);
			//KituriToast.toastShow(this, R.string.repress_to_exit_app);
			this.postDelayed(dRunnable , delayMillis);
		}else{
			if(mOnDoubleClickListener != null){
				mOnDoubleClickListener.onDoubleClick(this);
			}			
		}
	}
		
	
	private DoubleRunnable dRunnable = new DoubleRunnable();
	
	class DoubleRunnable implements Runnable {

		private Boolean isRun = false;
		
		
		public Boolean getIsRun() {
			return isRun;
		}

		public void setIsRun(Boolean isRun) {
			this.isRun = isRun;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			isRun = false;
		}
		
	}

	public interface OnDoubleClickListener {
		public void onDoubleClick(View view);
	}


}
