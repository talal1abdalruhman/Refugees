package com.example.refugees.MainScreenFragments;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.transition.Slide;

import com.example.refugees.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PaperWorksFragment extends Fragment {
    public NestedScrollView scroller;
    public Fragment context = this;
    public View views;
    public ArrayList<ConstraintLayout> descs, steps, headers;
    public ArrayList<com.google.android.material.button.MaterialButton> confirms;
    public ArrayList<com.airbnb.lottie.LottieAnimationView> status;
    public ArrayList<Float> steps_save;
    public ArrayList<ImageView> arrows;
    public HashMap<View, Integer> map;
    public ArrayList<Boolean> states;
    public PaperWorksFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.LEFT);
        setEnterTransition(slide);
        slide.setSlideEdge(Gravity.LEFT);
        setExitTransition(slide);
        return inflater.inflate(R.layout.fragment_paper_works, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final NestedScrollView layout = view.findViewById(R.id.father);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setup();
            }
        });
        views = view;
        descs = new ArrayList<>();
        steps = new ArrayList<>();
        headers = new ArrayList<>();
        arrows = new ArrayList<>();
        states = new ArrayList<>();
        steps_save = new ArrayList<>();
        confirms = new ArrayList<>();
        status = new ArrayList<>();
        map = new HashMap<>();

        scroller = views.findViewById(R.id.father);

        steps.add(views.findViewById(R.id.step_one));
        steps.add(views.findViewById(R.id.step_two));
        steps.add(views.findViewById(R.id.step_three));
        steps.add(views.findViewById(R.id.step_four));
        steps.add(views.findViewById(R.id.step_five));
        steps.add(views.findViewById(R.id.step_six));

        descs.add(views.findViewById(R.id.step_one_desc));
        descs.add(views.findViewById(R.id.step_two_desc));
        descs.add(views.findViewById(R.id.step_three_desc));
        descs.add(views.findViewById(R.id.step_four_desc));
        descs.add(views.findViewById(R.id.step_five_desc));
        descs.add(views.findViewById(R.id.step_six_desc));

        headers.add(views.findViewById(R.id.step_one_header));
        headers.add(views.findViewById(R.id.step_two_header));
        headers.add(views.findViewById(R.id.step_three_header));
        headers.add(views.findViewById(R.id.step_four_header));
        headers.add(views.findViewById(R.id.step_five_header));
        headers.add(views.findViewById(R.id.step_six_header));

        arrows.add(views.findViewById(R.id.step_one_arrow));
        arrows.add(views.findViewById(R.id.step_two_arrow));
        arrows.add(views.findViewById(R.id.step_three_arrow));
        arrows.add(views.findViewById(R.id.step_four_arrow));
        arrows.add(views.findViewById(R.id.step_five_arrow));
        arrows.add(views.findViewById(R.id.step_six_arrow));

        confirms.add(views.findViewById(R.id.materialButton));
        confirms.add(views.findViewById(R.id.materialButton2));
        confirms.add(views.findViewById(R.id.materialButton3));
        confirms.add(views.findViewById(R.id.materialButton4));
        confirms.add(views.findViewById(R.id.materialButton5));
        confirms.add(views.findViewById(R.id.materialButton6));

        status.add(views.findViewById(R.id.step_one_status));
        status.add(views.findViewById(R.id.step_two_status));
        status.add(views.findViewById(R.id.step_three_status));
        status.add(views.findViewById(R.id.step_four_status));
        status.add(views.findViewById(R.id.step_five_status));
        status.add(views.findViewById(R.id.step_six_status));

        for(int i = 0; i < steps.size(); i++)
            states.add(false);
        for(int i = 0; i < headers.size(); i++)
            map.put(headers.get(i), i);
        for(int i = 0; i < confirms.size(); i++)
            map.put(confirms.get(i), i);
        for(int i = 0; i < headers.size(); i++) {
            headers.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view = (ConstraintLayout)view;
                    int index = map.get(view);
                    if(!states.get(index))
                        animate(index);
                    else
                        animate_back(index);
                }
            });
        }
        for(int i = 0; i < confirms.size(); i++) {
            confirms.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view = view;
                    int index = map.get(view);
                    confirm(index);
                }
            });
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    public void setup() {
        for(int i = 0; i < descs.size(); i++) {
            descs.get(i).setPivotY(0);
            descs.get(i).setScaleY(0);
            descs.get(i).setAlpha(0);
        }
        steps_save.add(0f);
        int cumulative_sum = descs.get(0).getHeight();
        for(int i = 1; i < steps.size(); i++) {
            steps.get(i).setTranslationY(cumulative_sum * -1);
            cumulative_sum += descs.get(i).getHeight();
        }
        for(int i = 1; i < steps.size(); i++) {
            steps_save.add(steps.get(i).getTranslationY());
            Log.d("testing this out ", "haha " + steps_save.get(i) + " " + i);
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    public void animate(int index) {
        for(int i = 0; i < steps.size(); i++) {
            if(i == index)
                continue;
            if(states.get(i))
                animate_back(i);
        }
        states.set(index, true);
        arrows.get(index).animate().setDuration(300).rotation(180);
        descs.get(index).animate().setDuration(300).alpha(1);
        for(int i = index + 1; i < steps.size(); i++) {
            steps.get(i).animate().setDuration(300).translationY(steps_save.get(i) + descs.get(index).getHeight());
        }
        descs.get(index).animate().setDuration(300).scaleY(1);
    }
    public void animate_back(int index) {
        scroller.requestDisallowInterceptTouchEvent(true);
        scroller.smoothScrollTo(0, 0);
        states.set(index, false);
        arrows.get(index).animate().setDuration(300).rotation(0);
        descs.get(index).animate().setDuration(300).alpha(0);
        for(int i = index + 1; i < steps.size(); i++) {
            steps.get(i).animate().setDuration(300).translationY(steps_save.get(i));
        }
        descs.get(index).animate().setDuration(300).scaleY(0);
    }
    public void confirm(int index) {
        animate_back(index);
        com.airbnb.lottie.LottieAnimationView check = views.findViewById(R.id.step_one_status);
        status.get(index).animate().setDuration(150).scaleX(0).scaleY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                status.get(index).animate().setDuration(150).scaleX(1).scaleY(1);
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        status.get(index).setMinAndMaxProgress(0f, 0.5f);
        status.get(index).playAnimation();
    }
}