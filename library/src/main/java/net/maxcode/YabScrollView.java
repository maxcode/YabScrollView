package net.maxcode;

import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;

import java.lang.ref.WeakReference;

/**
 * Created by andrei on 03/12/15.
 */
public class YabScrollView extends ScrollView {

    private static final int WIDTH_DEVIDER_OVERSCROLL_DISTANCE = 2;
    private static final float BOUNCE_ANIM_TIME = 300f;

    private GestureDetectorCompat mGestureDetector;
    private GesturesDetectorListener mGesturesDetectorListener;
    private TimeInterpolator mInterpolator;

    private boolean mTopHit;
    private boolean mBottomHit;
    private int mMaxOverScrollDistance;
    private long mStartTime;

    public YabScrollView(final Context context) {
        super(context);
        init();
    }

    public YabScrollView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public YabScrollView(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.mTopHit = false;
        this.mBottomHit = false;
        this.mGesturesDetectorListener = new GesturesDetectorListener(this);
        this.mGestureDetector = new GestureDetectorCompat(getContext(), mGesturesDetectorListener);
        this.setOverScrollMode(OVER_SCROLL_ALWAYS);
        final int heightPixels = getContext().getResources().getDisplayMetrics().heightPixels;
        mMaxOverScrollDistance = heightPixels / WIDTH_DEVIDER_OVERSCROLL_DISTANCE;
        mInterpolator = new AccelerateDecelerateInterpolator();
    }

    public void setSwipeListener(ISwipeListener pSwipeListener) {
        this.mGesturesDetectorListener.setSwipeListener(pSwipeListener);
    }

    @Override protected void onScrollChanged(int scollX, int scrollY, int oldScrollX, int oldScrollY) {
        super.onScrollChanged(scollX, scrollY, oldScrollX, oldScrollY);
        View childView = this.getChildAt(0);
        if(childView == null){
            return;
        }
        if(getScrollY() <= 0){
            this.mTopHit = true;
            this.mBottomHit = false;
        } else if(getScrollY() >= (childView.getHeight()-this.getHeight())){
            this.mTopHit = false;
            this.mBottomHit = true;
        } else {
            this.mTopHit = false;
            this.mBottomHit = false;
        }
    }

    @Override public boolean dispatchTouchEvent(MotionEvent ev) {
        this.mGestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override public boolean canScrollVertically(int direction) {
        return true;
    }

    @Override protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY,
                                             int scrollRangeX, int scrollRangeY, int maxOverScrollX,
                                             int maxOverScrollY, boolean isTouchEvent) {
        int overScrollDistance = mMaxOverScrollDistance;
        if (isTouchEvent) {
            mStartTime = AnimationUtils.currentAnimationTimeMillis();
        } else {
            final long elapsedTime = AnimationUtils.currentAnimationTimeMillis() - mStartTime;
            float interpolation = mInterpolator.getInterpolation((float) elapsedTime / BOUNCE_ANIM_TIME);
            interpolation = interpolation > 1 ? 1 : interpolation;
            overScrollDistance -= overScrollDistance * interpolation;
            overScrollDistance = overScrollDistance < 0 ? 0 : overScrollDistance;
        }

        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, overScrollDistance, isTouchEvent);
    }

    @TargetApi(21)
    @Override public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
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

    @Override protected void onDetachedFromWindow() {
        this.mGestureDetector = null;
        this.mGesturesDetectorListener = null;
        super.onDetachedFromWindow();
    }

    public boolean isTopHit() {
        return mTopHit;
    }

    public boolean isBottomHit() {
        return mBottomHit;
    }

    public interface ISwipeListener{
        void onSwipeUp(YabScrollView pYabScrollView);
        void onSwipeDown(YabScrollView pYabScrollView);
    }

    public static class GesturesDetectorListener extends GestureDetector.SimpleOnGestureListener{

        private static final int SWIPE_THRESHOLD = 150;
        public static final int SWIPE_VELOCITY_THRESHOLD = 150;

        private WeakReference<YabScrollView> mYabScrollViewWeakReference;
        private WeakReference<ISwipeListener> mSwipeListenerWeakReference;

        public GesturesDetectorListener(YabScrollView pYabScrollView){
            this.mYabScrollViewWeakReference = new WeakReference<>(pYabScrollView);
        }

        @Override public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            this.onSwipeRight();
                        } else {
                            this.onSwipeLeft();
                        }
                    }
                    result = true;
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        this.onSwipeDown();
                    } else {
                        this.onSwipeUp();
                    }
                }
                result = true;

            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }

        public void setSwipeListener(ISwipeListener pSwipeListener){
            this.mSwipeListenerWeakReference = new WeakReference<>(pSwipeListener);
        }

        private void onSwipeUp() {
            if(mYabScrollViewWeakReference.get() != null && mYabScrollViewWeakReference.get().isBottomHit()){
                if(mSwipeListenerWeakReference != null && mSwipeListenerWeakReference.get() != null){
                    this.mSwipeListenerWeakReference.get().onSwipeUp(mYabScrollViewWeakReference.get());
                }
            }
        }

        private void onSwipeDown() {
            if(mYabScrollViewWeakReference.get() != null && mYabScrollViewWeakReference.get().isTopHit()){
                if(mSwipeListenerWeakReference != null && mSwipeListenerWeakReference.get() != null){
                    mSwipeListenerWeakReference.get().onSwipeDown(mYabScrollViewWeakReference.get());
                }
            }
        }

        private void onSwipeLeft() {}

        private void onSwipeRight() {}
    }
}