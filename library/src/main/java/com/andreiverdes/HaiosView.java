package com.andreiverdes;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ScrollView;

import timber.log.Timber;

/**
 * Created by andrei on 03/12/15.
 */
public class HaiosView extends ScrollView {
    private static final float MAX_Y_OVERSCROLL_DISTANCE = 200;
    private int mMaxYOverscrollDistance ;

    public HaiosView(Context context) {
        super(context);
        this.initBounceListView(context);
    }

    public HaiosView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initBounceListView(context);
    }

    public HaiosView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initBounceListView(context);
    }

    private void initBounceListView(Context pContext)
    {
        setOverScrollMode(OVER_SCROLL_ALWAYS);
        //get the density of the screen and do some maths with it on the max overscroll distance
        //variable so that you get similar behaviors no matter what the screen size

        final DisplayMetrics metrics = pContext.getResources().getDisplayMetrics();
        final float density = metrics.density;

        mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
    }

    @Override protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        boolean returned = super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);
        Timber.i(""+this.canScrollVertically(-1)+" "+canScrollVertically(1));
        return returned;
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
