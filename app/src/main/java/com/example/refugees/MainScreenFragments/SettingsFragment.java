package com.example.refugees.MainScreenFragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.transition.Slide;

import com.example.refugees.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static com.example.refugees.MainScreenActivity.navView;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    View view;
    public static ConstraintLayout langLayout, chooseLangLayout, changeEmailLayout, resetPasswordLayout;
    RadioButton radioArabic, radioEnglish;
    ImageView emailArrow, passwordArrow;
    public static ImageView arrow;
    public static TextView selectedLang;
    boolean isOpen = false;
    FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        // Inflate the layout for this fragment
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.LEFT);
        setEnterTransition(slide);
        slide.setSlideEdge(Gravity.LEFT);
        setExitTransition(slide);
        return view;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_settings_to_home);
                navView.setCheckedItem(R.id.home);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        final FrameLayout layout = (FrameLayout) view.findViewById(R.id.father);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setup();
            }
        });
        langLayout = view.findViewById(R.id.settings_lang_layout);
        chooseLangLayout = view.findViewById(R.id.settings_choose_lang_layout);
        arrow = view.findViewById(R.id.settings_lang_down_arrow);
        selectedLang = view.findViewById(R.id.settings_selected_lang);
        radioArabic = view.findViewById(R.id.radioButton_arabic);
        radioEnglish = view.findViewById(R.id.radioButton_english);
        emailArrow = view.findViewById(R.id.arrow_email);
        passwordArrow = view.findViewById(R.id.arrow_password);
        changeEmailLayout = view.findViewById(R.id.changeEmailLayout);
        resetPasswordLayout = view.findViewById(R.id.resetPasswordLayout);
        user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            changeEmailLayout.setVisibility(View.GONE);
            resetPasswordLayout.setVisibility(View.GONE);
        }

        String language = getResources().getConfiguration().locale.getLanguage();

        if(language.equals("en")){
            radioEnglish.setChecked(true);
            selectedLang.setText(getString(R.string.english));
        } else {
            radioArabic.setChecked(true);
            selectedLang.setText(getString(R.string.arabic));
            emailArrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_left));
            passwordArrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_left));
        }

        langLayout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                if(!isOpen) {
                    animate();
                    isOpen = true;
                } else {
                    animate_back();
                    isOpen = false;
                }
            }
        });

        changeEmailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_settings_to_updateEmailFragment);
            }
        });

        resetPasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_settings_to_updatePasswordFragment);
            }
        });
    }
    public void setup() {
        resetPasswordLayout.animate().setDuration(0).translationY(chooseLangLayout.getY() * -1);
        changeEmailLayout.animate().setDuration(0).translationY(chooseLangLayout.getY() * -1);
        chooseLangLayout.setPivotY(0);
        chooseLangLayout.setScaleY(0);
    }
    public void animate() {
        changeEmailLayout.animate().setDuration(300).translationY(0);
        resetPasswordLayout.animate().setDuration(300).translationY(0);
        chooseLangLayout.animate().setDuration(300).scaleY(1f);
        arrow.animate().setDuration(300).rotation(180);
    }
    public void animate_back() {
        resetPasswordLayout.animate().setDuration(300).translationY(chooseLangLayout.getY() * -1);
        changeEmailLayout.animate().setDuration(300).translationY(chooseLangLayout.getY() * -1);
        chooseLangLayout.animate().setDuration(300).scaleY(0f);
        arrow.animate().setDuration(300).rotation(0);
    }
}