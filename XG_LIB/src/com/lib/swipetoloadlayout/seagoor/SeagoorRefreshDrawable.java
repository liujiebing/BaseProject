package com.lib.swipetoloadlayout.seagoor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.TypedValue;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

import com.xg.platform.R;


/**
 * Created by Droideek on 2016/11/2.
 */
public class SeagoorRefreshDrawable extends BaseRefreshDrawable {

    private static final int ANIMATION_DURATION = 1000;
    private LinearInterpolator LINEAR_INTERPOLATOR = new LinearInterpolator();

    int mHeight;
    int mWidth;

    int mDogOffsetX;
    int mDogOffsetY;
    int mEarthOffsetX;
    int mEarthOffsetY;
    int mDogWidth;
    int mDogHeight;
    int mEarthWidth;
    int mEarthHeight;
    int mEarthScaleRange;
    int mEarthInitDeltaY;
    int mEarthFinalDeltaY;
    int mDogFinalDeltaY;

    int mDogInitHeight;
    int mDogInitWidth;


    float mPercent;


    SeagoorRefreshHeaderView mHeaderView;
    int mTotalDragDistance;
    Matrix mMatrix;
    Animation mAnimation;

    Bitmap mDog;
    Bitmap mEarth;

    public SeagoorRefreshDrawable(Context context, SeagoorRefreshHeaderView headerView, int totalDistance) {
        super(context);
        this.mHeaderView = headerView;
        this.mTotalDragDistance = totalDistance;
        this.mMatrix = new Matrix();
        setupAnimations();

        headerView.post(new Runnable() {
            @Override
            public void run() {
                initiateDimens();
            }
        });
    }


    private void initiateDimens() {
        mHeight = mWidth = dp2px(80);
        mDogInitHeight = dp2px(21);
        mDogInitWidth = dp2px(20);
        mEarthInitDeltaY = mEarthScaleRange = dp2px(10);
        mEarthFinalDeltaY = dp2px(12);
        mDogFinalDeltaY = dp2px(13);
        createBitmap();
    }

    private void createBitmap() {
        mEarth = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.xg_ptr_loading_earch);
        int eWidth = dp2px(70);
        if (mEarth.getWidth() != eWidth) {
            mEarth = Bitmap.createScaledBitmap(mEarth, eWidth, eWidth, true);
        }

        mDog = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.xg_ptr_loading_pull);

        mEarthWidth = mEarth.getWidth();
        mEarthHeight = mEarth.getHeight();

        mDogWidth = mDog.getWidth();
        mDogHeight = mDog.getHeight();

        mEarthOffsetX = (mWidth - mEarthWidth)/2;
        mEarthOffsetY = (mHeight - mEarthHeight) + mEarthInitDeltaY;

        mDogOffsetX = (mWidth - mDogInitWidth)/2;
        mDogOffsetY = mEarthOffsetY - mDogInitHeight;

    }


    private void setupAnimations() {
        mAnimation = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
                invalidateSelf();
            }
        };
        mAnimation.setRepeatCount(Animation.INFINITE);
        mAnimation.setRepeatMode(Animation.RESTART);
        mAnimation.setInterpolator(LINEAR_INTERPOLATOR);
        mAnimation.setDuration(ANIMATION_DURATION);
    }

    @Override
    public void setPercent(float percent, boolean invalidate) {
        this.mPercent = Math.min(1, percent);

        if(invalidate) invalidateSelf();
    }

    @Override
    public void offsetTopAndBottom(int offset) {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        if(mDogInitHeight ==0) return;

        int count = canvas.save();
        drawEarth(canvas);
        drawDog(canvas);

        canvas.restoreToCount(count);
    }

    private void drawEarth(Canvas canvas) {
        Matrix matrix = mMatrix;
        matrix.reset();

        float dy = (mHeight - mEarthFinalDeltaY - mEarthOffsetY) * mPercent;
        matrix.postTranslate(mEarthOffsetX, mEarthOffsetY + dy);

        canvas.drawBitmap(mEarth, matrix, null);
    }

    private void drawDog(Canvas canvas) {
        Matrix matrix = mMatrix;
        matrix.reset();

        float deltaScale = (mDogWidth -mDogInitWidth) * mPercent;

        float s = (mDogInitWidth + deltaScale)/mDogWidth;

        float dy = (mHeight - mDogFinalDeltaY - mDogHeight) * mPercent;

        matrix.postTranslate(mDogOffsetX - deltaScale/2, mDogOffsetY+ dy);
        matrix.postScale(s, s);

        canvas.drawBitmap(mDog, matrix, null);
    }

    public int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }

}
