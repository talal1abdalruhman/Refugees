package com.example.refugees.MainScreenFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.refugees.R;

public class SettingsFragment extends Fragment {

    public SettingsFragment() {
        // Required empty public constructor
    }

    View view;
    public static ConstraintLayout langLayout, chooseLangLayout, changeEmailLayout;
    RadioButton radioArabic, radioEnglish;
    ImageView emailArrow, passwordArrow;
    public static ImageView arrow;
    public static TextView selectedLang;
    boolean isOpen = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        langLayout = view.findViewById(R.id.settings_lang_layout);
        chooseLangLayout = view.findViewById(R.id.settings_choose_lang_layout);
        arrow = view.findViewById(R.id.settings_lang_down_arrow);
        selectedLang = view.findViewById(R.id.settings_selected_lang);
        radioArabic = view.findViewById(R.id.radioButton_arabic);
        radioEnglish = view.findViewById(R.id.radioButton_english);
        emailArrow = view.findViewById(R.id.arrow_email);
        passwordArrow = view.findViewById(R.id.arrow_password);
        changeEmailLayout = view.findViewById(R.id.changeEmailLayout);
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
            @Override
            public void onClick(View v) {
                if(!isOpen) {
                    chooseLangLayout.setVisibility(View.VISIBLE);
                    arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_up));
                    isOpen = true;
                } else {
                    chooseLangLayout.setVisibility(View.GONE);
                    arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_down));
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
    }

}