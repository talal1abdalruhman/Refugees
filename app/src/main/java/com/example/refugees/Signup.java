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
import android.widget.ScrollView;

import java.util.Locale;
import java.util.Objects;

public class Signup extends AppCompatActivity {
    Context context = this;
    ImageView top;
    ImageView bottom_dark;
    ImageView bottom_light;
    ImageView logo;
    LinearLayout log;
    ScrollView form;

    Interpolator interpolator = new FastOutSlowInInterpolator() ;
    String language;
    int duration = 500;
    int delay = 100;
    float ScreenWidth;
    float ScreenHeight;
    int direction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        direction = getIntent().getIntExtra("direction", 1);
        setContentView(R.layout.activity_signup);
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
        form = findViewById(R.id.form_signup);
    }
    public void setup() {
        form.setX(ScreenWidth * direction);
        bottom_dark.setPivotY(bottom_dark.getHeight());
        bottom_light.setPivotY(bottom_light.getHeight());
        top.setPivotY(0);
        bottom_dark.setScaleY(0.1f);
        bottom_light.setScaleY(0.13f);
        top.setScaleY(0.2f);
        animate();
    }
    public ViewPropertyAnimator animate() {
        return form.animate().setDuration(duration).translationXBy(form.getWidth() * -1 * direction).setInterpolator(interpolator);
    }
    public ViewPropertyAnimator animate(int next) {
        return form.animate().setDuration(duration).translationXBy(form.getWidth() * -1 * direction * next).setInterpolator(interpolator);
    }
    public void click(View view) {
        animate(direction).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = intent = new Intent(context,  Searchable.class);;
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        animate(direction).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(getApplicationContext(), LogOptions.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("direction", -1);
                String message = getResources().getConfiguration().locale.getLanguage();
                intent.putExtra("language", message);
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