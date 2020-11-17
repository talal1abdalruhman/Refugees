package com.example.refugees.MainScreenFragments;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.transition.Slide;

import com.example.refugees.LogOptions;
import com.example.refugees.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

@SuppressLint("ClickableViewAccessibility")

public class HomeFragment extends Fragment {
    public HomeFragment() {
        // Required empty public constructor
    }

    FirebaseUser user;
    private FirebaseAuth mAuth;
    private boolean animating = false;
    final private String TAG = "tag";
    private int touch;
    ArrayList<MotionLayout> motionLayouts;
    ScrollView scrollView;
    TextView reunionTxt;
    ImageView family;
    Dialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.LEFT);
        setExitTransition(slide);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reunionTxt = view.findViewById(R.id.title_ren);
        family = view.findViewById(R.id.family);
        user = FirebaseAuth.getInstance().getCurrentUser();
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.reunion_whithout_account_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mAuth = FirebaseAuth.getInstance();
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                } else {
//                TODO: ask the user if he want to exit the application or get him back to l~ogOtions
//                Navigation.findNavController(view).navigate(R.id.action_settings_to_home);
                    out_animation().setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if(user != null)
                                mAuth.signOut();
                            Intent intent = new Intent(getContext(), LogOptions.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        motionLayouts = new ArrayList<MotionLayout>(4);
        motionLayouts.add(view.findViewById(R.id.reunion));
        motionLayouts.add(view.findViewById(R.id.instructions));
        motionLayouts.add(view.findViewById(R.id.options1));
        motionLayouts.add(view.findViewById(R.id.options2));
        scrollView = view.findViewById(R.id.scrollView);
        touch = ViewConfiguration.get(scrollView.getContext()).getScaledTouchSlop();
        scrollView.setSmoothScrollingEnabled(true);
        motionLayoutStuff();
        final ConstraintLayout layout = (ConstraintLayout) view.findViewById(R.id.father);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setup();
            }
        });
        // TODO: I found two solutions for this one is to handle the touch thingy and the other is to ... I forgot I will check later
//        view.findViewById(R.id.dis_ren).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (user != null) {
//                    Navigation.findNavController(view).navigate(R.id.action_home_to_searchFragment);
//                } else {
//                    dialog.show();
//                }
//            }
//        });
//        reunionTxt.setOnClickListener(new View.OnCli
//        kListener() {
//            @Override
//            public void onClick(View v) {
//                if (user != null) {
//                    Navigation.findNavController(view).navigate(R.id.action_home_to_searchFragment);
//                } else {
//                    dialog.show();
//                }
//            }
//        });
//        family.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (user != null) {
//                    Navigation.findNavController(view).navigate(R.id.action_home_to_searchFragment);
//                } else {
//                    dialog.show();
//                }
//            }
//        });

    }

    public void motionLayoutStuff() {
        for (int i = 0; i < motionLayouts.size(); i++)
            motionLayouts.get(i).setOnTouchListener(new View.OnTouchListener() {
                double x = -1;
                double y = -1;

                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    scrollView.requestDisallowInterceptTouchEvent(true);
                    if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                        double currX = event.getX();
                        double currY = event.getY();
                        if (y == -1)
                            y = currY;
                        if (x == -1)
                            x = currX;
                        double disX = java.lang.Math.abs(currX - x);
                        double disY = java.lang.Math.abs(currY - y);
                        if (disY > disX && (disY > touch * 6.5f) && !animating) {
                            scrollView.requestDisallowInterceptTouchEvent(false);
                        }
                    }
                    return false;
                }
            });

        for (int i = 0; i < motionLayouts.size(); i++) {
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

    public void setup() {
        for (int i = 0; i < motionLayouts.size(); i++)
            if (i % 2 == 0)
                motionLayouts.get(i).setX(motionLayouts.get(i).getWidth() * -1);
            else
                motionLayouts.get(i).setX(motionLayouts.get(i).getWidth());

        for (int i = 0; i < motionLayouts.size(); i++)
            motionLayouts.get(i).animate().setDuration(1000).translationX(0);
    }

    public ViewPropertyAnimator out_animation() {
        for (int i = 0; i < motionLayouts.size(); i++)
            if (i % 2 == 0 && i == motionLayouts.size() - 1)
                return motionLayouts.get(i).animate().translationX(motionLayouts.get(i).getWidth() * -1);
            else if (i == motionLayouts.size() - 1)
                return motionLayouts.get(i).animate().translationX(motionLayouts.get(i).getWidth());
            else if (i % 2 == 0)
                motionLayouts.get(i).animate().translationX(motionLayouts.get(i).getWidth() * -1);
            else
                motionLayouts.get(i).animate().translationX(motionLayouts.get(i).getWidth());
        return motionLayouts.get(0).animate();
    }
}