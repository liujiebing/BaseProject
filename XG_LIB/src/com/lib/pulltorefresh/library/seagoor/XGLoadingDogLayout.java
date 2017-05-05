package com.lib.pulltorefresh.library.seagoor;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.droideek.util.LogUtil;
import com.lib.pulltorefresh.library.PullToRefreshBase;
import com.lib.pulltorefresh.library.internal.LoadingLayout;
import com.xg.platform.R;

/**
 * Created by Droideek on 2016/4/26.
 */
public class XGLoadingDogLayout extends LoadingLayout {
    private LinearLayout mInnerLayout;
    private TextView mHeaderText;
    private TextView mSubHeaderText;
    private ImageView mFooterImage;
    private AnimationDrawable mLoadingDrawable;
    private Drawable mPullDrawable;
    private String mPullLabel;
    private String mRefreshingLabel;
    private String mReleaseLabel;
    private ImageView mPullImge;
    private int footerScale;
    private int footerMarginStart;
    private int footerMarginRange;

    private int pullImgWidth;
    private int pullImgHeight;
    private int footerImgWidth;
    private int pullAnimHeight;
    private int pullAnimWidth;

    public XGLoadingDogLayout(Context context, PullToRefreshBase.Mode mode, PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {
        super(context, mode, scrollDirection, attrs);
    }

    @Override
    protected void init(Context context, PullToRefreshBase.Mode mode, PullToRefreshBase.Orientation scrollDirection, TypedArray attrs) {

        mMode = mode;
        mScrollDirection = scrollDirection;

        inflate(context, R.layout.pull_to_refresh_dog_vertical, this);

        mInnerLayout = (LinearLayout) findViewById(R.id.fl_inner);
        mHeaderText = (TextView) findViewById(R.id.pull_to_refresh_text);
        mSubHeaderText = (TextView) findViewById(R.id.pull_to_refresh_sub_text);
        mPullImge = (ImageView) findViewById(R.id.pull_to_refresh_image);
        mFooterImage = (ImageView) findViewById(R.id.pull_to_refresh_footer);

        mLoadingDrawable = new AnimationDrawable();
        mLoadingDrawable.addFrame(getResources().getDrawable(R.drawable.xg_ptr_loading_1), 100);
        mLoadingDrawable.addFrame(getResources().getDrawable(R.drawable.xg_ptr_loading_2), 100);
        mLoadingDrawable.addFrame(getResources().getDrawable(R.drawable.xg_ptr_loading_3), 100);
        mLoadingDrawable.addFrame(getResources().getDrawable(R.drawable.xg_ptr_loading_4), 100);

        mPullDrawable = getResources().getDrawable(R.drawable.xg_ptr_loading_pull);
        footerScale = getResources().getDimensionPixelSize(R.dimen.xg_pull_img_footer_scale);
        footerMarginStart = getResources().getDimensionPixelSize(R.dimen.xg_pull_img_footer_margin_start);
        footerMarginRange = getResources().getDimensionPixelSize(R.dimen.xg_pull_img_footer_margin_range);
        pullImgWidth = getResources().getDimensionPixelSize(R.dimen.xg_pull_img_width);
        pullImgHeight = getResources().getDimensionPixelSize(R.dimen.xg_pull_img_height);
        footerImgWidth = getResources().getDimensionPixelSize(R.dimen.xg_pull_footer_img_width);

        pullAnimHeight = getResources().getDimensionPixelSize(R.dimen.xg_pull_anim_height);
        pullAnimWidth = getResources().getDimensionPixelSize(R.dimen.xg_pull_anim_width);

        mPullLabel = getResources().getString(R.string.pull_to_refresh_pull_label);
        mRefreshingLabel = getResources().getString(R.string.pull_to_refresh_refreshing_label);
        mReleaseLabel = getResources().getString(R.string.pull_to_refresh_release_label);

        setPullDrawable();
        reset();
    }


    void setPullDrawable(){
        mPullImge.setImageDrawable(mPullDrawable);
        mFooterImage.setImageDrawable(getResources().getDrawable(R.drawable.xg_ptr_loading_earch));
        //----初始化PARAM----

        LinearLayout.LayoutParams pullParams = (LinearLayout.LayoutParams) mPullImge.getLayoutParams();
        LinearLayout.LayoutParams footerParams = (LinearLayout.LayoutParams) mFooterImage.getLayoutParams();

        pullParams.height = pullImgHeight;
        pullParams.width = pullImgWidth;
        footerParams.height = footerImgWidth;
        footerParams.width = footerImgWidth;
        footerParams.bottomMargin = footerMarginStart;

        mPullImge.setLayoutParams(pullParams);
        mFooterImage.setLayoutParams(footerParams);
    }

    @Override
    public void setLoadingDrawable(Drawable drawable) {

    }

    @Override
    public void setPullLabel(CharSequence pullLabel) {

    }

    @Override
    public void setRefreshingLabel(CharSequence refreshingLabel) {

    }

    @Override
    public void refreshing() {
       // Log.d("LOADINGLAYOUT", "== refreshing(); pullImgHeight*2:"+(pullImgHeight*2));
        mSubHeaderText.setText(mRefreshingLabel);
        mFooterImage.setVisibility(GONE);

        mPullImge.setImageDrawable(mLoadingDrawable);
        LinearLayout.LayoutParams pullParams = (LinearLayout.LayoutParams) mPullImge.getLayoutParams();
        pullParams.height = pullAnimHeight;
        pullParams.width = pullAnimWidth;
        mPullImge.setLayoutParams(pullParams);

        ((AnimationDrawable)mPullImge.getDrawable()).start();
    }

    @Override
    public void onPull(float scaleOfLayout) {

        LinearLayout.LayoutParams pullParams = (LinearLayout.LayoutParams) mPullImge.getLayoutParams();
        LinearLayout.LayoutParams footerParams = (LinearLayout.LayoutParams) mFooterImage.getLayoutParams();

        if (scaleOfLayout > 0 && scaleOfLayout <= 1) {
            pullParams.height = (int)(pullImgHeight*(1+scaleOfLayout));
            pullParams.width = (int)(pullImgWidth*(1+scaleOfLayout));
            int fv= (int)(footerImgWidth - (footerScale*scaleOfLayout));
            footerParams.width = fv;
            footerParams.height = fv;
            footerParams.bottomMargin = footerMarginStart - (int)((footerMarginRange)*scaleOfLayout);
            mPullImge.setLayoutParams(pullParams);
            mFooterImage.setLayoutParams(footerParams);
        }

        if(scaleOfLayout <= 1 && !mPullLabel.equals(mSubHeaderText.getText())){
            mSubHeaderText.setText(mPullLabel);
        }
        if(scaleOfLayout > 1 && !mReleaseLabel.equals(mSubHeaderText.getText())){
            mSubHeaderText.setText(mReleaseLabel);
        }

        //--
       // Log.d("LOADINGLAYOUT", "==onPull(); scaleOfLayout:" + scaleOfLayout + " pullImgHeight:"+(pullParams.height) + " footerParams.width:"+footerParams.width);

    }

    @Override
    public void showInvisibleViews() {
       // Log.d("LOADINGLAYOUT", "==showInvisibleViews;");

        if (View.INVISIBLE == mHeaderText.getVisibility()) {
            mHeaderText.setVisibility(View.VISIBLE);
        }
       if (View.VISIBLE != mFooterImage.getVisibility()) {
            mFooterImage.setVisibility(View.VISIBLE);
        }
        if (View.INVISIBLE == mPullImge.getVisibility()) {
            mPullImge.setVisibility(View.VISIBLE);
        }
        if (View.INVISIBLE == mSubHeaderText.getVisibility()) {
            mSubHeaderText.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void hideAllViews() {
        //Log.d("LOADINGLAYOUT", "== hideAllViews();");
        if (View.VISIBLE == mHeaderText.getVisibility()) {
            mHeaderText.setVisibility(View.INVISIBLE);
        }
        if (View.VISIBLE == mFooterImage.getVisibility()) {
            mFooterImage.setVisibility(View.GONE);
        }
        if (View.VISIBLE == mPullImge.getVisibility()) {
            mPullImge.setVisibility(View.INVISIBLE);
        }
        if (View.VISIBLE == mSubHeaderText.getVisibility()) {
            mSubHeaderText.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getContentSize() {
       // Log.d("LOADINGLAYOUT", "== getContentSize();" + (mInnerLayout.getHeight()));
        return mInnerLayout.getHeight();
    }

    @Override
    public int getRefreshHeight() {
        //Log.d("LOADINGLAYOUT", "== getRefreshHeight();");
        return getContentSize();
    }

    @Override
    public void releaseToRefresh() {
       // Log.d("LOADINGLAYOUT", "== releaseToRefresh();");
        if (null != mSubHeaderText) {
            mSubHeaderText.setText(mReleaseLabel);
        }
    }

    @Override
    public void reset() {
       // Log.d("LOADINGLAYOUT", "== reset();");
        if (null != mSubHeaderText) {
            mSubHeaderText.setText(mPullLabel);
        }
        setPullDrawable();
        showInvisibleViews();

        Drawable mDrawable = mPullImge.getDrawable();
        if (mDrawable instanceof AnimationDrawable) {
            ((AnimationDrawable) mDrawable).stop();
        }

    }

    @Override
    protected int getDefaultDrawableResId() {
       // Log.d("LOADINGLAYOUT", "== getDefaultDrawableResId();");
        return 0;
    }

    @Override
    protected void onPullDrawableSet(Drawable imageDrawable) {

    }

    @Override
    protected void onPullImpl(float scaleOfLayout) {

    }

    @Override
    protected void pullToRefreshImpl() {

    }

    @Override
    protected void refreshingImpl() {

    }

    @Override
    protected void releaseToRefreshImpl() {

    }

    @Override
    protected void resetImpl() {

    }

}
