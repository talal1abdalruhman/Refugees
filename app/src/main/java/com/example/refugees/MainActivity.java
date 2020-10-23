package com.example.refugees;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.loader.content.Loader;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "language";
    String  message = "ENGLISH";
    Context context = this;
    ImageView top;
    ImageView bottom_dark;
    ImageView bottom_light;
    ImageView logo;
    TextView language;
    Button english;
    Button arabic;
    LinearLayout options_lang;
    LinearLayout options_log;
    Button skip;
    LinearLayout options_log_ar;
    Button skip_ar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.background);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setup();
            }
        });
        top = findViewById(R.id.top);
        bottom_dark = findViewById(R.id.bottom_dark);
        bottom_light = findViewById(R.id.bottom_light);
        logo = findViewById(R.id.logo);
        language = findViewById(R.id.langage);
        options_lang = findViewById(R.id.options_lang);

    }
    public void setup() {
        int duration = 0;
        float mul = 2;
        int delay = 0;

        mul = -1;
        top.animate().setDuration(duration).translationYBy(top.getMeasuredHeight() * mul).setStartDelay(delay);

        mul = 1;
        bottom_light.animate().setDuration(duration).translationYBy(bottom_light.getMeasuredHeight() * mul).setStartDelay(delay);
        bottom_dark.animate().setDuration(duration).translationYBy(bottom_dark.getMeasuredHeight() * mul).setStartDelay(delay);

        mul = 3;
        logo.animate().setDuration(duration).translationYBy(logo.getMeasuredHeight() * mul).setStartDelay(delay);

        mul = 3;
        language.animate().setDuration(duration).translationXBy(language.getMeasuredWidth() * mul).setStartDelay(delay);
        options_lang.animate().setDuration(duration).translationXBy(options_lang.getMeasuredWidth() * mul).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                move_logo();
            }
        });
        language.setVisibility(View.VISIBLE);
        options_lang.setVisibility(View.VISIBLE);
    }
    public void move_logo() {
        int duration = 600;
        float mul = (float)-3.25;
        int delay = 200;
        logo.animate().setDuration(duration).translationYBy(logo.getMeasuredHeight() * mul).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                move_top();
            }
        });
    }
    public void move_top() {
        int duration = 400;
        float mul = (float)0.5;
        int delay = 250;
        mul = 1;
        top.animate().setDuration(duration).translationYBy(top.getMeasuredHeight() * mul).setStartDelay(delay);
        mul = (float)0.5;
        logo.animate().setDuration(duration).translationYBy(logo.getMeasuredHeight() * mul).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                move_bottom();
            }
        });
    }
    public void move_bottom() {
        int duration = 350;
        float mul = -1;
        int delay = 300;
        bottom_dark.animate().setDuration(duration).translationYBy(bottom_dark.getMeasuredHeight() * mul).setStartDelay(delay);
        bottom_light.animate().setDuration(duration).translationYBy(bottom_light.getMeasuredHeight() * mul).setStartDelay(delay);
        mul = (float)-0.25;
        logo.animate().setDuration(duration).translationYBy(logo.getMeasuredHeight() * mul).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                move_side();
            }
        });
    }
    public void move_side() {
        int duration = 500;
        float mul = -3;
        int delay = 150;
        Log.d("test", "lang " + language.getWidth());
        language.animate().setDuration(duration).translationXBy(language.getMeasuredWidth() * mul).setStartDelay(delay);
        options_lang.animate().setDuration(duration).translationXBy(options_lang.getMeasuredWidth() * mul).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(getApplicationContext(), Language.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}