package com.example.refugees;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Locale;
import java.util.Objects;

public class LogOptions extends AppCompatActivity {
    Context context = this;
    ImageView top;
    ImageView bottom_dark;
    ImageView bottom_light;
    ImageView logo;
    LinearLayout log;

    Interpolator interpolator = new FastOutSlowInInterpolator() ;
    String language;
    int duration = 400;
    int delay = 100;
    float ScreenWidth;
    float ScreenHeight;
    int direction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        direction = getIntent().getIntExtra("direction", 1);
        language =  getIntent().getStringExtra("language");

        setApplocale(language);

        setContentView(R.layout.activity_log_options);
        final ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.background);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                ScreenWidth = displayMetrics.widthPixels;
                ScreenHeight = displayMetrics.heightPixels;

                setup();
            }
        });
        top = findViewById(R.id.top);
        bottom_light = findViewById(R.id.bottom_light);
        bottom_dark = findViewById(R.id.bottom_dark);
        logo = findViewById(R.id.logo);
        log = findViewById(R.id.log);
    }
    public void setup() {
        if(direction == -1) {
            logo.setX(logo.getWidth() * -1);
            bottom_dark.setPivotY(bottom_dark.getHeight());
            bottom_light.setPivotY(bottom_light.getHeight());
            top.setPivotY(0);
            bottom_dark.setScaleY(0.1f);
            bottom_light.setScaleY(0.13f);
            top.setScaleY(0.2f);
        }
        logo.setY(ScreenHeight * 0.143933347f);
        log.setX(ScreenWidth * direction);
        animate();
    }
    public ViewPropertyAnimator animate() {
        if(direction == -1) {
            logo.animate().setDuration(duration).translationXBy(logo.getWidth()).setInterpolator(interpolator);
            bottom_light.animate().setDuration(duration).scaleY((float)1);
            bottom_dark.animate().setDuration(duration).scaleY((float)1);
            top.animate().setDuration(duration).scaleY((float)1);
        }
        return log.animate().setDuration(duration).translationXBy(log.getWidth() * -1 * direction).setInterpolator(interpolator);
    }
    public ViewPropertyAnimator animate(int next) {
        return log.animate().setDuration(duration).translationXBy(log.getWidth() * -1 * direction * next).setInterpolator(interpolator);
    }
    public ViewPropertyAnimator animate_out() {
        bottom_light.setPivotY(bottom_light.getHeight());
        bottom_dark.setPivotY(bottom_dark.getHeight());
        top.setPivotY(0);
        bottom_light.animate().setDuration(duration - 200).scaleY(0.13f);
        bottom_dark.animate().setDuration(duration).scaleY(0.1f);
        top.animate().setDuration(duration).scaleY(0.2f);
        return logo.animate().setDuration(duration).translationXBy(logo.getWidth() * -1).setInterpolator(interpolator);
    }
    public void click(View view) {
        animate_out();
        animate(direction).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = intent = new Intent(context,  Signup.class);;
                if(view.getId() == R.id.login)
                    intent = new Intent(context, Login.class);
                else if(view.getId() == R.id.signup)
                    intent = new Intent(context,  Signup.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        animate().setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = intent = new Intent(context,  Language.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("direction", -1);
                startActivity(intent);
                finish();
            }
        });
    }
    public void setApplocale(String language) {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        configuration.locale = new Locale(language);
        configuration.setLayoutDirection(new Locale(language));
        resources.updateConfiguration(configuration, displayMetrics);
    }
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}