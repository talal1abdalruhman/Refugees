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
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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
    ImageView family;
    Dialog dialog;
    View views;
    boolean gov_open = false, UN_open = false, schools_open = false, hospitals_open = false;
    private final int START = 2131296799, END1 = 2131296481, END2 = 2131296482;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        views = view;
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.LEFT);
        setExitTransition(slide);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        motionLayouts.add(view.findViewById(R.id.options2));
        motionLayouts.add(view.findViewById(R.id.options1));
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
    }
    public void motionLayoutTouch() {
        motionLayouts.get(0).setOnTouchListener(new View.OnTouchListener() {
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
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    y = event.getY();
                    x = event.getX();
                }
                if(event.getAction() == MotionEvent.ACTION_UP)
                    if(Math.abs(y - event.getY()) <= touch * 0.7 && Math.abs(x - event.getX()) < touch * 0.7) {
                        if (user != null) {
                            Navigation.findNavController(views).navigate(R.id.action_home_to_searchFragment);
                        } else {
                            dialog.show();
                        }
                        //TODO: this one is for the REUNION YOUR FAMILY FUNCTION TO GO TO A NEW FRAGMENT AND START THE ANIMATION FOR THAT
                        Toast.makeText(getActivity(), "Reunion your family", Toast.LENGTH_SHORT / 20).show();
                    }
                return false;
            }
        });
        motionLayouts.get(1).setOnTouchListener(new View.OnTouchListener() {
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
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    y = event.getY();
                    x = event.getX();
                }
                if(event.getAction() == MotionEvent.ACTION_UP)
                    if(Math.abs(y - event.getY()) <= touch * 0.7 && Math.abs(x - event.getX()) < touch * 0.7) {
                        //TODO: this one is for the PAPER WORK INSTRUCTIONS FUNCTION TO GO TO A NEW FRAGMENT AND START THE ANIMATION FOR THAT
                        Toast.makeText(getActivity(), "PAPER WORK INSTRUCTIONS", Toast.LENGTH_SHORT / 20).show();
                        Navigation.findNavController(views).navigate(R.id.action_home_to_paperWorksFragment);
                    }
                return false;
            }
        });
        motionLayouts.get(2).setOnTouchListener(new View.OnTouchListener() {
            int half = motionLayouts.get(2).getWidth()/2 - 1;
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
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    y = event.getY();
                    x = event.getX();
                }
                if(event.getAction() == MotionEvent.ACTION_UP)
                    if(Math.abs(y - event.getY()) <= touch * 0.7 && Math.abs(x - event.getX()) < touch * 0.7) {
                        if(gov_open) {
                            //TODO: this one is for the GOVERMENT CIRCLES FUNCTION TO GO TO A NEW FRAGMENT AND START THE ANIMATION FOR THAT
                            Toast.makeText(getActivity(), "GOVERMENT CIRCLES", Toast.LENGTH_SHORT / 20).show();
                        }
                        else if(UN_open) {
                            //TODO: this one is for the UNHCR FUNCTION TO GO TO A NEW FRAGMENT AND START THE ANIMATION FOR THAT
                            Toast.makeText(getActivity(), "UNHCR", Toast.LENGTH_SHORT / 20).show();
                        }
                        else if(x <= half) {
                            //TODO: this one is for the UNHCR FUNCTION TO GO TO A NEW FRAGMENT AND START THE ANIMATION FOR THAT
                            Toast.makeText(getActivity(), "UNHCR", Toast.LENGTH_SHORT / 20).show();
                        }
                        else {
                            //TODO: this one is for the GOVERMENT CIRCLES FUNCTION TO GO TO A NEW FRAGMENT AND START THE ANIMATION FOR THAT
                            Toast.makeText(getActivity(), "GOVERMENT CIRCLES", Toast.LENGTH_SHORT / 20).show();
                        }
                    }
                return false;
            }
        });
        motionLayouts.get(3).setOnTouchListener(new View.OnTouchListener() {
            int half = motionLayouts.get(3).getWidth()/2 - 1;
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
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    y = event.getY();
                    x = event.getX();
                }
                if(event.getAction() == MotionEvent.ACTION_UP)
                    if(Math.abs(y - event.getY()) <= touch * 0.7 && Math.abs(x - event.getX()) < touch * 0.7) {
                        if(schools_open) {
                            //TODO: this one is for the GOVERMENT CIRCLES FUNCTION TO GO TO A NEW FRAGMENT AND START THE ANIMATION FOR THAT
                                Toast.makeText(getActivity(), "SCHOOLS", Toast.LENGTH_SHORT / 20).show();
                        }
                        else if(hospitals_open) {
                            //TODO: this one is for the UNHCR FUNCTION TO GO TO A NEW FRAGMENT AND START THE ANIMATION FOR THAT
                            Toast.makeText(getActivity(), "HOSPITALS", Toast.LENGTH_SHORT / 20).show();
                        }
                        else if(x <= half) {
                            //TODO: this one is for the UNHCR FUNCTION TO GO TO A NEW FRAGMENT AND START THE ANIMATION FOR THAT
                            Toast.makeText(getActivity(), "HOSPITALS", Toast.LENGTH_SHORT / 20).show();
                        }
                        else {
                            //TODO: this one is for the GOVERMENT CIRCLES FUNCTION TO GO TO A NEW FRAGMENT AND START THE ANIMATION FOR THAT
                            Toast.makeText(getActivity(), "SCHOOLS", Toast.LENGTH_SHORT / 20).show();
                        }
                    }
                return false;
            }
        });
    }
    public void motionLayoutStuff() {
        for (int i = 0; i < motionLayouts.size(); i++) {
            int index = i;
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
                public void onTransitionCompleted(MotionLayout motionLayout, int state) {
                    animating = false;
                    check(index, state);
                    scrollView.requestDisallowInterceptTouchEvent(false);
                }
            });
        }
    }

    public void setup() {
        motionLayoutTouch();
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
    public void check(int index, int state) {
        if(index == 2) {
            if(state == START)
                gov_open = UN_open = false;
            else if (state == END1) {
                gov_open = true;
                UN_open = false;
            }
            else if(state == END2) {
                gov_open = false;
                UN_open = true;
            }
        }
        if(index == 3) {
            if(state == START)
                schools_open = hospitals_open = false;
            else if (state == END1) {
                schools_open = true;
                hospitals_open = false;
            }
            else {
                schools_open = false;
                hospitals_open = true;
            }
        }
    }
}