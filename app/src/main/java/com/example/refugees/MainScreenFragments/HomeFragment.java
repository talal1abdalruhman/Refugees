package com.example.refugees.MainScreenFragments;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.airbnb.lottie.LottieAnimationView;
import com.example.refugees.LogOptions;
import com.example.refugees.R;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

@SuppressLint("ClickableViewAccessibility")

public class HomeFragment extends Fragment {
    public HomeFragment() {
        // Required empty public constructor
    }

    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private LocationSettingsRequest.Builder builder;
    private final int REQUEST_CHECK_CODE = 123;
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
    private final int START = 2131297241, END1 = 2131296785, END2 = 2131296786;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        String lang  = getResources().getConfiguration().locale.getLanguage();
        views = view;
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
//                TODO: ask the user if he want to exit the application or get him back to logOtions
//                Navigation.findNavController(view).navigate(R.id.action_settings_to_home);
                    ViewPropertyAnimator anim;
                    String lang  = getResources().getConfiguration().locale.getLanguage();
                    if(lang.equals("ar"))
                        anim = out_animation_ar();
                    else
                        anim = out_animation();
                    anim.setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            if (user != null)
                                mAuth.signOut();
                            Intent intent = new Intent(getContext(), LogOptions.class);
                            startActivity(intent);
                            getActivity().finish();
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
        motionLayouts.add(view.findViewById(R.id.uns));
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
                String lang = getResources().getConfiguration().locale.getLanguage();
                TextView floats = views.findViewById(R.id.floats);
                floats.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        swipe_help();
                    }
                });
                if(lang.equals("ar"))
                    setup_ar();
                else
                    setup();
            }
        });

        AskForLocationPermission();
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
                if (event.getAction() == MotionEvent.ACTION_UP)
                    if (Math.abs(y - event.getY()) <= touch * 0.7 && Math.abs(x - event.getX()) < touch * 0.7) {
                        if (user != null) {
                            Navigation.findNavController(views).navigate(R.id.action_home_to_searchFragment);
                        } else {
                            dialog.show();
                        }
//                        Toast.makeText(getActivity(), "Reunion your family", Toast.LENGTH_SHORT / 20).show();
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
                if (event.getAction() == MotionEvent.ACTION_UP)
                    if (Math.abs(y - event.getY()) <= touch * 0.7 && Math.abs(x - event.getX()) < touch * 0.7) {
//                        Toast.makeText(getActivity(), "PAPER WORK INSTRUCTIONS", Toast.LENGTH_SHORT / 20).show();
                        Navigation.findNavController(views).navigate(R.id.action_home_to_paperWorksFragment);
                    }
                return false;
            }
        });
        motionLayouts.get(2).setOnTouchListener(new View.OnTouchListener() {
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
                if (event.getAction() == MotionEvent.ACTION_UP)
                    if (Math.abs(y - event.getY()) <= touch * 0.7 && Math.abs(x - event.getX()) < touch * 0.7) {
//                        Toast.makeText(getActivity(), "UNHCR", Toast.LENGTH_SHORT / 20).show();
                        Navigation.findNavController(views).navigate(R.id.action_home_to_unhcrFragment);
                    }
                return false;

            }
        });
        motionLayouts.get(3).setOnTouchListener(new View.OnTouchListener() {
            int half = motionLayouts.get(3).getWidth() / 2 - 1;
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
                if (event.getAction() == MotionEvent.ACTION_UP)
                    if (Math.abs(y - event.getY()) <= touch * 0.7 && Math.abs(x - event.getX()) < touch * 0.7) {
                        if (schools_open) {
//                            Toast.makeText(getActivity(), "SCHOOLS", Toast.LENGTH_SHORT / 20).show();
                            Navigation.findNavController(views).navigate(R.id.action_home_to_educationFragment);
                        } else if (hospitals_open) {
//                            Toast.makeText(getActivity(), "HOSPITALS", Toast.LENGTH_SHORT / 20).show();
                            Navigation.findNavController(views).navigate(R.id.action_home_to_healthServicesFragment);
                        } else if (x <= half) {
//                            Toast.makeText(getActivity(), "HOSPITALS", Toast.LENGTH_SHORT / 20).show();
                            Navigation.findNavController(views).navigate(R.id.action_home_to_healthServicesFragment);
                        } else {
//                            Toast.makeText(getActivity(), "SCHOOLS", Toast.LENGTH_SHORT / 20).show();
                            Navigation.findNavController(views).navigate(R.id.action_home_to_educationFragment);
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

    public void swipe_help() {
        if(motionLayouts.get(0).getCurrentState() == motionLayouts.get(0).getEndState()) {
            motionLayouts.get(0).transitionToStart();
            motionLayouts.get(0).setTransitionListener(new MotionLayout.TransitionListener() {
                @Override
                public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {

                }

                @Override
                public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {

                }

                @Override
                public void onTransitionCompleted(MotionLayout motionLayout, int i) {
                    motionLayouts.get(0).setTransitionListener(null);
                    swipe_help();
                }

                @Override
                public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {


                }
            });
            return;
        }
        scrollView.smoothScrollTo(0, 0);
        LottieAnimationView swipe = views.findViewById(R.id.anime);
        swipe.animate().setDuration(150).alpha(1);
        swipe.setMinAndMaxProgress(0.2f, 0.73f);
        swipe.playAnimation();
        swipe.addAnimatorListener(new Animator.AnimatorListener() {
            int counter = 0;
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                swipe.animate().alpha(0f);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        motionLayouts.get(0).transitionToEnd();
        motionLayouts.get(0).setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {
                motionLayouts.get(0).setTransitionListener(null);
                swipe.animate().alpha(0);
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

            }
        });
    }


    public void setup() {
        views.findViewById(R.id.floats).animate().scaleX(1f).scaleY(1f).setDuration(1200);
        motionLayoutTouch();
        for (int i = 0; i < 3; i++)
            if (i % 2 == 0)
                motionLayouts.get(i).setX(motionLayouts.get(i).getWidth() * -1);
            else
                motionLayouts.get(i).setX(motionLayouts.get(i).getWidth());
        split();
        ViewPropertyAnimator anim = null;
        for (int i = 0; i < motionLayouts.size(); i++)
            anim = motionLayouts.get(i).animate().setDuration(1000).translationX(0);
    }
    public void setup_ar() {
        views.findViewById(R.id.floats).animate().scaleX(1f).scaleY(1f).setDuration(1200);
        motionLayoutTouch();
        for (int i = 0; i < 3; i++)
            if (i % 2 != 0)
                motionLayouts.get(i).setX(motionLayouts.get(i).getWidth() * -1);
            else
                motionLayouts.get(i).setX(motionLayouts.get(i).getWidth());
        split();
        for (int i = 0; i < motionLayouts.size(); i++)
            motionLayouts.get(i).animate().setDuration(1000).translationX(0);
    }
    public void split() {
        LinearLayout left, upperLeft, right, upperRight;
        TextView textView;
        ImageView imageView;

        textView = views.findViewById(R.id.hospitals);
        imageView = views.findViewById(R.id.hospital);
        left = views.findViewById(R.id.Left);
        upperLeft = views.findViewById(R.id.Left_upper);
        left.setTranslationX(left.getWidth() * -1);
        upperLeft.setTranslationX(left.getWidth() * -1);
        textView.setTranslationX(left.getWidth() * -1);
        imageView.setTranslationX(left.getWidth() * -1);
        left.animate().setDuration(1000).translationX(0);
        upperLeft.animate().setDuration(1000).translationX(0);
        textView.animate().setDuration(1000).translationX(0);
        imageView.animate().setDuration(1000).translationX(0);

        right = views.findViewById(R.id.Right);
        upperRight = views.findViewById(R.id.Right_upper);
        textView = views.findViewById(R.id.schools);
        imageView = views.findViewById(R.id.school);

        right.setTranslationX(right.getWidth());
        upperRight.setTranslationX(right.getWidth());
        textView.setTranslationX(right.getWidth());
        imageView.setTranslationX(right.getWidth());
        right.animate().setDuration(1000).translationX(0);
        upperRight.animate().setDuration(1000).translationX(0);
        textView.animate().setDuration(1000).translationX(0);
        imageView.animate().setDuration(1000).translationX(0);
    }
    public void unsplit() {
        LinearLayout left, upperLeft, right, upperRight;
        TextView textView;
        ImageView imageView;

        textView = views.findViewById(R.id.hospitals);
        imageView = views.findViewById(R.id.hospital);
        left = views.findViewById(R.id.Left);
        upperLeft = views.findViewById(R.id.Left_upper);
        left.animate().setDuration(1000).translationX(left.getWidth() * -1);
        upperLeft.animate().setDuration(1000).translationX(left.getWidth() * -1);
        textView.animate().setDuration(1000).translationX(left.getWidth() * -1);
        imageView.animate().setDuration(1000).translationX(left.getWidth() * -1);
        views.findViewById(R.id.dis_hospital).animate().setDuration(1000).translationX(left.getWidth() * -1);

        right = views.findViewById(R.id.Right);
        upperRight = views.findViewById(R.id.Right_upper);
        textView = views.findViewById(R.id.schools);
        imageView = views.findViewById(R.id.school);

        right.animate().setDuration(1000).translationX(right.getWidth());
        upperRight.animate().setDuration(1000).translationX(right.getWidth());
        textView.animate().setDuration(1000).translationX(right.getWidth());
        imageView.animate().setDuration(1000).translationX(right.getWidth());
        views.findViewById(R.id.dis_school).animate().setDuration(1000).translationX(right.getWidth());
    }
    public ViewPropertyAnimator out_animation() {
        for (int i = 0; i < 3; i++)
            if (i % 2 == 0)
                motionLayouts.get(i).animate().translationX(motionLayouts.get(i).getWidth() * -1);
            else
                motionLayouts.get(i).animate().translationX(motionLayouts.get(i).getWidth());
        unsplit();
        return motionLayouts.get(0).animate();
    }
    public ViewPropertyAnimator out_animation_ar() {
        for (int i = 0; i < 3; i++)
            if (i % 2 != 0)
                motionLayouts.get(i).animate().translationX(motionLayouts.get(i).getWidth() * -1);
            else
                motionLayouts.get(i).animate().translationX(motionLayouts.get(i).getWidth());
        unsplit();
        return motionLayouts.get(0).animate();
    }
    public void check(int index, int state) {
        if(index == 3) {
            if(state == START)
                schools_open = hospitals_open = false;
            else if (state == END1) {
                schools_open = true;
                hospitals_open = false;
            } else {
                schools_open = false;
                hospitals_open = true;
            }
        }
    }
    public void AskForLocationPermission() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
            Log.d("permission", "AskForLocationPermission: asked ");
        } else {
            Log.e("permission", "PERMISSION GRANTED");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Log.d("permission", "onRequestPermissionsResult: returned" + requestCode);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("permission", "onRequestPermissionsResult: accepted");
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}