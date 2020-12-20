package com.example.refugees.MainScreenFragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    MaterialButton registrationCall;

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
        registrationCall = view.findViewById(R.id.registration_call);

        steps.add(views.findViewById(R.id.step_one));
        steps.add(views.findViewById(R.id.step_two));
        steps.add(views.findViewById(R.id.step_three));
        steps.add(views.findViewById(R.id.step_four));
        steps.add(views.findViewById(R.id.step_five));

        descs.add(views.findViewById(R.id.step_one_desc));
        descs.add(views.findViewById(R.id.step_two_desc));
        descs.add(views.findViewById(R.id.step_three_desc));
        descs.add(views.findViewById(R.id.step_four_desc));
        descs.add(views.findViewById(R.id.step_five_desc));

        headers.add(views.findViewById(R.id.step_one_header));
        headers.add(views.findViewById(R.id.step_two_header));
        headers.add(views.findViewById(R.id.step_three_header));
        headers.add(views.findViewById(R.id.step_four_header));
        headers.add(views.findViewById(R.id.step_five_header));

        arrows.add(views.findViewById(R.id.step_one_arrow));
        arrows.add(views.findViewById(R.id.step_two_arrow));
        arrows.add(views.findViewById(R.id.step_three_arrow));
        arrows.add(views.findViewById(R.id.step_four_arrow));
        arrows.add(views.findViewById(R.id.step_five_arrow));

        confirms.add(views.findViewById(R.id.materialButton));
        confirms.add(views.findViewById(R.id.materialButton2));
        confirms.add(views.findViewById(R.id.materialButton3));
        confirms.add(views.findViewById(R.id.materialButton4));
        confirms.add(views.findViewById(R.id.materialButton5));

        status.add(views.findViewById(R.id.step_one_status));
        status.add(views.findViewById(R.id.step_two_status));
        status.add(views.findViewById(R.id.step_three_status));
        status.add(views.findViewById(R.id.step_four_status));
        status.add(views.findViewById(R.id.step_five_status));

        for (int i = 0; i < steps.size(); i++)
            states.add(false);
        for (int i = 0; i < headers.size(); i++)
            map.put(headers.get(i), i);
        for (int i = 0; i < confirms.size(); i++)
            map.put(confirms.get(i), i);
        for (int i = 0; i < headers.size(); i++) {
            headers.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view = (ConstraintLayout) view;
                    int index = map.get(view);
                    if (!states.get(index))
                        animate(index);
                    else
                        animate_back(index);
                }
            });
        }
        for (int i = 0; i < confirms.size(); i++) {
            confirms.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view = view;
                    int index = map.get(view);
                    ObjectAnimator.ofInt(scroller, "scrollY", 0).setDuration(500).start();
                    confirm(index);
                    SaveCompleteness(index);
                }
            });
        }
        registrationCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:064008000")));
            }
        });

        RetrieveCompleteness();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setup() {
        for (int i = 0; i < descs.size(); i++) {
            descs.get(i).setPivotY(0);
            descs.get(i).setScaleY(0);
            descs.get(i).setAlpha(0);
        }
        steps_save.add(0f);
        int cumulative_sum = descs.get(0).getHeight();
        for (int i = 1; i < steps.size(); i++) {
            steps.get(i).setTranslationY(cumulative_sum * -1);
            cumulative_sum += descs.get(i).getHeight();
        }
        for (int i = 1; i < steps.size(); i++) {
            steps_save.add(steps.get(i).getTranslationY());
            Log.d("testing this out ", "haha " + steps_save.get(i) + " " + i);
        }
        fix(headers.size() - 1, false);
    }

    public void fix(int index, boolean open) {
        scroller.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            ObjectAnimator anim;
            boolean in = true;

            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    int height = views.getHeight();
                    int curr_bottom = scrollY + height;
                    int condition = headers.get(0).getHeight() * headers.size() + 200;
                    int position = 0;
                    position = headers.get(index).getHeight() * index;
                    Log.d("test", "there we go f " + (position));
                    if (position + height >= condition) {
                        position -= height;
                        position += headers.get(index).getHeight() * 2;
                        position -= 2.5 * index - 10;
                        if (open)
                            position += descs.get(index).getHeight();
                    } else
                        position += 2.5 * index + 10;
                    if (open)
                        condition += descs.get(index).getHeight();
                    condition = Math.max(height + 200, condition);
                    anim = ObjectAnimator.ofInt(scroller, "scrollY", 0).setDuration(500);
                    if (open)
                        anim = ObjectAnimator.ofInt(scroller, "scrollY", position).setDuration(500);
                    if (curr_bottom >= condition && !anim.isRunning()) {
                        Log.d("test", "there we go " + curr_bottom + " " + condition + " " + (position) + " ");
                        scroller.smoothScrollTo(0, condition - height - 20);
                        if (scroller.getScrollY() == condition - height - 20)
                            anim.start();
                    }
                }
            }
        });
    }

    public void check(int index) {
        int bottom = (int) scroller.getScrollY() + views.getHeight();
        int top = (int) scroller.getScrollY();
        int position = headers.get(index).getHeight() * (index + 1) + descs.get(index).getHeight() + 30;
        int position2 = headers.get(index).getHeight() * index;
        Log.d("test", "this is it " + bottom + " " + position);
        Log.d("test", "this is it 2 " + top + " " + position2);
        if (position >= bottom) {
            ObjectAnimator.ofInt(scroller, "scrollY", position - views.getHeight() + 40).setDuration(500).start();
        } else if (position2 <= top)
            ObjectAnimator.ofInt(scroller, "scrollY", position2).setDuration(500).start();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void animate(int index) {
        check(index);
        for (int i = 0; i < steps.size(); i++) {
            if (i == index)
                continue;
            if (states.get(i))
                animate_back(i);
        }
        fix(index, true);
        states.set(index, true);
        arrows.get(index).animate().setDuration(300).rotation(180);
        descs.get(index).animate().setDuration(300).alpha(1);
        for (int i = index + 1; i < steps.size(); i++) {
            steps.get(i).animate().setDuration(300).translationY(steps_save.get(i) + descs.get(index).getHeight());
        }
        descs.get(index).animate().setDuration(300).scaleY(1);
        if (headers.get(0).getHeight() * (index + 1) < headers.get(0).getHeight() * headers.size())
            ObjectAnimator.ofInt(scroller, "scrollY", headers.get(0).getHeight() * index).setDuration(500).start();
    }

    public void animate_back(int index) {
        states.set(index, false);
        arrows.get(index).animate().setDuration(300).rotation(0);
        descs.get(index).animate().setDuration(300).alpha(0);
        for (int i = index + 1; i < steps.size(); i++) {
            steps.get(i).animate().setDuration(300).translationY(steps_save.get(i));
        }
        descs.get(index).animate().setDuration(300).scaleY(0);
        fix(headers.size() - 1, false);
    }

    public void confirm(int index) {
        animate_back(index);
        com.airbnb.lottie.LottieAnimationView check = views.findViewById(R.id.step_one_status);
        status.get(index).animate().setDuration(150).scaleX(0).scaleY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                status.get(index).animate().setDuration(150).scaleX(1).scaleY(1);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        status.get(index).setMinAndMaxProgress(0f, 0.5f);
        status.get(index).playAnimation();
    }

    public void SaveCompleteness(int index) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
            mRef.child("paper_work_completeness").child(index + "").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d("completeness", "onComplete: step " + index);
                }
            });
        } else {
            SharedPreferences preferences = requireActivity().getSharedPreferences("STEPS_COMPLETENESS", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(index + "", true).commit();
            editor.apply();
        }
    }

    public void RetrieveCompleteness() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
            mRef.child("paper_work_completeness").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        int index = Integer.parseInt(ds.getKey());
                        complete(index);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            for (int i = 0; i < steps.size(); i++) {
                SharedPreferences preferences = requireActivity().getSharedPreferences("STEPS_COMPLETENESS", Context.MODE_PRIVATE);
                boolean status = preferences.getBoolean(i + "", false);
                if (status) complete(i);
            }
        }
    }

    public void complete(int index) {
        status.get(index).animate().setDuration(150).scaleX(0).scaleY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                status.get(index).animate().setDuration(150).scaleX(1).scaleY(1);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        status.get(index).setMinAndMaxProgress(0f, 0.5f);
        status.get(index).playAnimation();
    }
}