package com.andreiverdes.haiosview.sample;

import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;

/**
 * Created by andrei on 03/12/15.
 */
public class OverScrollView extends ScrollView {
    private static final int WIDTH_DEVIDER_OVERSCROLL_DISTANCE = 2;

    private TimeInterpolator mInterpolator;
    private int mMaxOverscrollDistance;
    private int mAnimTime;
    private long mStartTime;

    /**
     * Instantiates {@link OverScrollView} object.
     */
    public OverScrollView(final Context context) {
        super(context);
        init();
    }

    /**
     * Instantiates {@link OverScrollView} object.
     */
    public OverScrollView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Instantiates {@link OverScrollView} object.
     */
    public OverScrollView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setOverScrollMode(OVER_SCROLL_ALWAYS);
        final int heightPixels = getContext().getResources().getDisplayMetrics().heightPixels;
        mMaxOverscrollDistance = heightPixels / WIDTH_DEVIDER_OVERSCROLL_DISTANCE;
        mAnimTime = getContext().getResources().getInteger(android.R.integer.config_mediumAnimTime);
        mInterpolator = new AccelerateDecelerateInterpolator();
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        int overScrollDistance = mMaxOverscrollDistance;
        if (isTouchEvent) {
            mStartTime = AnimationUtils.currentAnimationTimeMillis();
        } else {
            final long elapsedTime = AnimationUtils.currentAnimationTimeMillis() - mStartTime;
            float interpolation = mInterpolator.getInterpolation((float) elapsedTime / 300f);
            interpolation = interpolation > 1 ? 1 : interpolation;
            overScrollDistance -= overScrollDistance * interpolation;
            overScrollDistance = overScrollDistance < 0 ? 0 : overScrollDistance;
        }

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, overScrollDistance, isTouchEvent);
    }

    @TargetApi(21)
    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        // Not consumed means it wasn't handled because ScrollView
        // doesn't take over scrolling bounds into scroll range,
        // so we fling it ourselves to get it bounce back
        if (getOverScrollMode() == OVER_SCROLL_ALWAYS && !consumed) {
            fling((int) velocityY);

            return true;
        } else {
            return super.dispatchNestedFling(velocityX, velocityY, consumed);
        }
    }
}
