package com.droideek.ui.actionbar;

import android.view.View;

public interface ActionBarInterface {
	
	void hideActionBar();
	
	void setHomeAction(boolean isHome);
	
	void setTitle(int resid);
	
	void setTitle(String text);
	
	void setImageLogo(int resid);
	
	View setMyView(int layoutid);
	
	View addAction(int view_id, View item);
	
	View addAction(int view_id, int res_id);
	View addRefreshAction(int view_id, int res_id);
	View addButtonAction(int view_id, int res_id);
	View addButtonAction(int view_id, int res_id, int bg_id);
	
	void onActionBarItem(int itemid);
	
	void removeAllActions();
	
	void removeActionAt(int index);
	
	void setProgressBarVisibility(int visibility);
	
}
