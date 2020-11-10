package com.example.refugees.MainScreenFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;

import com.example.refugees.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    public HomeFragment() {
        // Required empty public constructor
    }


    private boolean animating = false;
    final private String TAG = "tag";
    private int touch;
    ArrayList<MotionLayout> motionLayouts;
    ScrollView scrollView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        motionLayouts = new ArrayList<MotionLayout>(4);
        motionLayouts.add(view.findViewById(R.id.reunion));
        motionLayouts.add(view.findViewById(R.id.instructions));
        motionLayouts.add(view.findViewById(R.id.options1));
        motionLayouts.add(view.findViewById(R.id.options2));
        scrollView = view.findViewById(R.id.scrollView);
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