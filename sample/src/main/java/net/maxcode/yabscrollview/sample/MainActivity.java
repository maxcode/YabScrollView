package net.maxcode.yabscrollview.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import net.maxcode.yabscrollview.ISwipeListener;
import net.maxcode.yabscrollview.YabScrollView;

import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements ISwipeListener {

    private YabScrollView mYabScrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.plant(new Timber.DebugTree());
        setContentView(R.layout.activity_main);
        this.findViews();
        this.mYabScrollView.setSwipeListener(this);
    }

    private void findViews() {
        this.mYabScrollView = (YabScrollView) this.findViewById(R.id.yabscrollview);
    }

    @Override public void onSwipeUp(YabScrollView pYabScrollView) {
        Toast.makeText(this, "Swipe UP detected!", Toast.LENGTH_SHORT).show();
    }

    @Override public void onSwipeDown(YabScrollView pYabScrollView) {
        Toast.makeText(this, "Swipe DOWN detected!", Toast.LENGTH_SHORT).show();
    }
}
