package com.example.refugees.MainScreenFragments;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.transition.Slide;

import com.example.refugees.R;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.refugees.MainScreenActivity.navView;

public class HelpFragment extends Fragment {


    public HelpFragment() {
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
        return views = view = inflater.inflate(R.layout.fragment_help, container, false);
    }

    public ArrayList<ConstraintLayout> layouts, descs, places, headers;
    public ArrayList<ImageView> arrows;
    public ArrayList<Boolean> states;
    public ArrayList<Float> places_save;
    public HashMap<View, Integer> map;
    View view, views;
    public ScrollView scroller;

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ConstraintLayout layout = view.findViewById(R.id.father);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                setup();
            }
        });
        places_save = new ArrayList<>();
        headers = new ArrayList<ConstraintLayout>();
        descs = new ArrayList<ConstraintLayout>();
        arrows = new ArrayList<ImageView>();
        places = new ArrayList<>();
        map = new HashMap<>();
        states = new ArrayList<>();
        scroller = view.findViewById(R.id.scroller);
        scroller.setVerticalScrollBarEnabled(false);

        headers.add(view.findViewById(R.id.community1_header));
        headers.add(view.findViewById(R.id.community2_header));
        headers.add(view.findViewById(R.id.community3_header));
        headers.add(view.findViewById(R.id.community4_header));
        headers.add(view.findViewById(R.id.community5_header));

        places.add(view.findViewById(R.id.community1_layout));
        places.add(view.findViewById(R.id.community2_layout));
        places.add(view.findViewById(R.id.community3_layout));
        places.add(view.findViewById(R.id.community4_layout));
        places.add(view.findViewById(R.id.community5_layout));

        descs.add(view.findViewById(R.id.community1_desc));
        descs.add(view.findViewById(R.id.community2_desc));
        descs.add(view.findViewById(R.id.community3_desc));
        descs.add(view.findViewById(R.id.community4_desc));
        descs.add(view.findViewById(R.id.community5_desc));

        arrows.add(view.findViewById(R.id.community1_arrow));
        arrows.add(view.findViewById(R.id.community2_arrow));
        arrows.add(view.findViewById(R.id.community3_arrow));
        arrows.add(view.findViewById(R.id.community4_arrow));
        arrows.add(view.findViewById(R.id.community5_arrow));
        for(int i = 0; i < places.size(); i++)
            states.add(false);
        for(int i = 0; i < headers.size(); i++)
            map.put(headers.get(i), i);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_help_to_home);
                navView.setCheckedItem(R.id.home);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }
    public void setup() {
        for(int i = 0; i < descs.size(); i++) {
            descs.get(i).setPivotY(0);
            descs.get(i).setScaleY(0);
            descs.get(i).setAlpha(0);
        }
        places_save.add(0f);
        for(int i = 0; i < headers.size(); i++)
            headers.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    view = (ConstraintLayout)v;
                    int index = map.get(view);
                    if(!states.get(index))
                        animate(index);
                    else
                        animate_back(index);
                }
            });
        int cumulative_sum = descs.get(0).getHeight();
        for(int i = 1; i < places.size(); i++) {
            places.get(i).setTranslationY(cumulative_sum * -1);
            cumulative_sum += descs.get(i).getHeight();
        }
        for(int i = 1; i < places.size(); i++) {
            places_save.add(places.get(i).getTranslationY());
            Log.d("testing this out ", "haha " + places_save.get(i) + " " + i);
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
                    int condition = headers.get(0).getHeight() * headers.size() + descs.get(index).getHeight() + 200;
                    int position = 0;
                    position = headers.get(index).getHeight() * index;
                    Log.d("test", "there we go f " + (position));
                    if(position + height >= condition) {
                        position -= height;
                        position += headers.get(index).getHeight() * 2;
                        position -= 2.5 * index - 10;
                        if(open)
                            position += descs.get(index).getHeight();
                    }
                    else
                        position += 2.5 * index + 10;
                    if(open)
                        condition += descs.get(index).getHeight();
                    condition = Math.max(height + 200, condition);
                    anim = ObjectAnimator.ofInt(scroller, "scrollY", position).setDuration(500);
                    if(curr_bottom >= condition && !anim.isRunning()) {
                        Log.d("test", "there we go " + curr_bottom + " " + condition + " " + (position) + " " );
                        scroller.smoothScrollTo(0, condition - height - 20);
                        if(scroller.getScrollY() == condition - height - 20)
                            anim.start();
                    }
                }
            }
        });
    }
    public void check(int index) {
        int bottom = (int)scroller.getScrollY() + views.getHeight();
        int top = (int)scroller.getScrollY();
        int position = headers.get(index).getHeight() * (index + 1) + descs.get(index).getHeight() + 30;
        int position2 = headers.get(index).getHeight() * index;
        Log.d("test", "this is it " + bottom + " " + position);
        Log.d("test", "this is it 2 " + top + " " + position2);
        if(position >= bottom) {
            ObjectAnimator.ofInt(scroller, "scrollY", position - views.getHeight() + 40).setDuration(500).start();
        }
        else if(position2 <= top)
            ObjectAnimator.ofInt(scroller, "scrollY", position2).setDuration(500).start();
    }
    public void animate(int index) {
        for(int i = 0; i < places.size(); i++) {
            if(i == index)
                continue;
            if(states.get(i))
                animate_back(i);
        }
        fix(index, true);
        states.set(index, true);
        arrows.get(index).animate().setDuration(300).rotation(180);
        descs.get(index).animate().setDuration(300).alpha(1);
        for(int i = index + 1; i < places.size(); i++) {
            places.get(i).animate().setDuration(300).translationY(places_save.get(i) + descs.get(index).getHeight());
        }
        descs.get(index).animate().setDuration(300).scaleY(1);
        check(index);
    }
    public void animate_back(int index) {
        fix(headers.size() - 1, false);
        states.set(index, false);
        arrows.get(index).animate().setDuration(300).rotation(0);
        descs.get(index).animate().setDuration(300).alpha(0);
        for(int i = index + 1; i < places.size(); i++) {
            places.get(i).animate().setDuration(300).translationY(places_save.get(i));
        }
        descs.get(index).animate().setDuration(300).scaleY(0);
    }
}