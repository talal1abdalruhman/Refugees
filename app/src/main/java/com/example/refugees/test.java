package com.example.refugees;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import java.util.ArrayList;

@SuppressLint("ClickableViewAccessibility")
public class test extends AppCompatActivity {
    private boolean animating = false;
    final private String TAG = "tag";
    private int touch;
    ArrayList<MotionLayout> motionLayouts;
    ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        motionLayouts = new ArrayList<MotionLayout>(4);
        motionLayouts.add(findViewById(R.id.reunion));
        motionLayouts.add(findViewById(R.id.instructions));
        motionLayouts.add(findViewById(R.id.options1));
        motionLayouts.add(findViewById(R.id.options2));
        scrollView = findViewById(R.id.scrollView);
        touch = ViewConfiguration.get(scrollView.getContext()).getScaledTouchSlop();
        scrollView.setSmoothScrollingEnabled(true);
        for(int i = 0; i < 4; i++)
            motionLayouts.get(i).setOnTouchListener(new View.OnTouchListener() {
                double x = -1;
                double y = -1;
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    scrollView.requestDisallowInterceptTouchEvent(true);
                    if(event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                        double currX = event.getX();
                        double currY = event.getY();
                        if (y == -1)
                            y = currY;
                        if (x == -1)
                            x = currX;
                        double disX = java.lang.Math.abs(currX - x);
                        double disY = java.lang.Math.abs(currY - y);
                        if(disY > disX && disY > touch * 7 && !animating) {
                            scrollView.requestDisallowInterceptTouchEvent(false);
                        }
                    }
                    return false;
                }
            });

        for(int i = 0; i < 4; i++) {
            motionLayouts.get(i).setTransitionListener(new MotionLayout.TransitionListener() {
                @Override
                public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {
                    animating = true;
                    scrollView.requestDisallowInterceptTouchEvent(true);
                }
                @Override
                public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {
                }
                @Override
                public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {
                }
                @Override
                public void onTransitionCompleted(MotionLayout motionLayout, int i) {
                    animating = false;
                    scrollView.requestDisallowInterceptTouchEvent(false);
                }
            });
        }
    }
}