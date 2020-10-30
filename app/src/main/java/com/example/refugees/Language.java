package com.example.refugees;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

public class Language extends AppCompatActivity {
    Context context = this;
    ImageView logo;
    ImageView top;
    ImageView bottom_dark;
    ImageView bottom_light;
    LinearLayout lang;
    Dialog dialog;
    Interpolator interpolator = new FastOutSlowInInterpolator() ;

    int duration = 400;
    float ScreenWidth;
    float ScreenHeight;
    int direction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        direction = getIntent().getIntExtra("direction", 1);
        setContentView(R.layout.activity_language);
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
        logo = findViewById(R.id.logo);
        bottom_dark = findViewById(R.id.bottom_dark);
        bottom_light = findViewById(R.id.bottom_light);
        lang = findViewById(R.id.lang);
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.exit);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    public void setup() {
        logo.setY(ScreenHeight + ((ScreenHeight * 0.85f) * -1) + (top.getHeight()) + ((bottom_dark.getHeight())/4f * -1));
        bottom_dark.setY(ScreenHeight - bottom_dark.getHeight());
        bottom_light.setY(ScreenHeight - bottom_light.getHeight());
        lang.setX(ScreenWidth * direction);
        animate();
    }
    public ViewPropertyAnimator animate() {
        return lang.animate().setDuration(duration).translationXBy(lang.getWidth() * -1 * direction).setInterpolator(interpolator);
    }
    public ViewPropertyAnimator animate(int next) {
        return lang.animate().setDuration(duration).translationXBy(lang.getWidth() * -1 * direction * next).setInterpolator(interpolator);
    }
    public void click(View view) {
        animate(direction).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(context, LogOptions.class);
                intent.putExtra("language", view.getId() == R.id.english ? "en" : "ar");
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        dialog.show();
    }
    public void yes(View view) {
        dialog.dismiss();
        finish();
    }
    public void no(View view) {
        dialog.dismiss();
    }
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}

