package com.andreiverdes.haiosview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by andrei on 02/12/15.
 */
public class HaiosView extends ScrollView {

    private View mChildView;

    public HaiosView(Context context) {
        super(context);
        this.afterCreate();
    }

    public HaiosView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.afterCreate();
    }

    public HaiosView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.afterCreate();
    }

    private void afterCreate(){
        this.mChildView = this.getChildAt(0);
    }

    @Override protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
    }
}
