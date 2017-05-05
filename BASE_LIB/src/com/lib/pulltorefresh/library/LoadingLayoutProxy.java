package com.lib.pulltorefresh.library;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import com.lib.pulltorefresh.library.internal.LoadingLayout;

import java.util.HashSet;

public class LoadingLayoutProxy implements ILoadingLayout {

	private final HashSet<LoadingLayout> mLoadingLayouts;

	LoadingLayoutProxy() {
		mLoadingLayouts = new HashSet<LoadingLayout>();
	}

	/**
	 * This allows you to add extra LoadingLayout instances to this proxy. This
	 * is only necessary if you keep your own instances, and want to have them
	 * included in any
	 * {@link PullToRefreshBase#createLoadingLayoutProxy(boolean, boolean)
	 * createLoadingLayoutProxy(...)} calls.
	 * 
	 * @param layout
	 *            - LoadingLayout to have included.
	 */
	public void addLayout(LoadingLayout layout) {
		if (null != layout) {
			mLoadingLayouts.add(layout);
		}
	}

	@Override
	public void setLastUpdatedLabel(CharSequence label) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setLastUpdatedLabel(label);
		}
	}

	@Override
	public void setLoadingDrawable(Drawable drawable) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setLoadingDrawable(drawable);
		}
	}

	@Override
	public void setRefreshingLabel(CharSequence refreshingLabel) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setRefreshingLabel(refreshingLabel);
		}
	}

	@Override
	public void setPullLabel(CharSequence label) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setPullLabel(label);
		}
	}

	@Override
	public void setReleaseLabel(CharSequence label) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setReleaseLabel(label);
		}
	}

	public void setTextTypeface(Typeface tf) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setTextTypeface(tf);
		}
	}

	@Override
	public void setPullDrawable(Drawable drawable) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setPullDrawable(drawable);
		}

	}

	@Override
	public void setTextSize(float size) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setTextSize(size);
		}
	}

	@Override
	public void setTextColor(ColorStateList color) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setTextColor(color);
		}
	}

	@Override
	public void setSubHeaderText(CharSequence label) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setSubHeaderText(label);
		}

	}

	@Override
	public void setSubTextAppearance(int value) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setSubTextAppearance(value);
		}
	}

	@Override
	public void setSubTextColor(ColorStateList color) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setSubTextColor(color);
		}
	}

	@Override
	public void setTextAppearance(int value) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setTextAppearance(value);
		}
	}

	@Override
	public void setIsRotate(boolean rotate) {
		for (LoadingLayout layout : mLoadingLayouts) {
			layout.setIsRotate(rotate);
		}
	}

	@Override
	public int getRefreshHeight() {
		return 200;
	}
}
