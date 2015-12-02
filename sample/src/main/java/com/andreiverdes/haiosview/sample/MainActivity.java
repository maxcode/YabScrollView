package com.andreiverdes.haiosview.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.plant(new Timber.DebugTree());
        setContentView(R.layout.activity_main);
//        OverScrollDecoratorHelper.setUpOverScroll((ScrollView)findViewById(R.id.list));
//        new VerticalOverScrollBounceEffectDecorator(
//                new ScrollViewOverScrollDecorAdapter((ScrollView)findViewById(R.id.list)),
//                0.50f,
////                VerticalOverScrollBounceEffectDecorator.DEFAULT_TOUCH_DRAG_MOVE_RATIO_FWD,
//                .50f,
////                VerticalOverScrollBounceEffectDecorator.DEFAULT_TOUCH_DRAG_MOVE_RATIO_BCK,
//                -5f
////                VerticalOverScrollBounceEffectDecorator.DEFAULT_DECELERATE_FACTOR
//        );
    }
}
