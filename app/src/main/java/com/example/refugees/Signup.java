package com.example.refugees;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.refugees.R;

public class Signup extends AppCompatActivity {
    Context context = this;
    LinearLayout options_log;
    Button skip;
    ImageView bottom_dark;
    ImageView bottom_light;
    ImageView logo;
    ImageView top;
    ScrollView form_signup;
    ConstraintLayout warning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
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
        form_signup = findViewById(R.id.form_signup);
        options_log = findViewById(R.id.options_log);
        skip = findViewById(R.id.skip);
        warning = findViewById(R.id.warning);
    }
    public void setup() {
        int duration = 0;
        float mul = -3;
        int delay = 0;

        bottom_dark.setPivotY(bottom_dark.getHeight());
        bottom_dark.animate().setDuration(duration).scaleY((float)0.1);
        bottom_light.setPivotY(bottom_light.getHeight());
        bottom_light.animate().setDuration(duration).scaleY((float)0.13);

        options_log.animate().setDuration(duration).translationXBy(options_log.getMeasuredWidth() * mul).setStartDelay(delay);
        logo.animate().setDuration(duration).translationXBy(logo.getMeasuredWidth() * mul).setStartDelay(delay);
        skip.animate().setDuration(duration).translationXBy(options_log.getMeasuredWidth() * mul).setStartDelay(delay);
        mul = 3;
        warning.animate().setDuration(duration).translationXBy(warning.getMeasuredWidth() * mul);

        options_log.setVisibility(View.VISIBLE);
        logo.setVisibility(View.VISIBLE);
        skip.setVisibility(View.VISIBLE);
        warning.setVisibility(View.VISIBLE);
    }
    @Override 
    public void onBackPressed() {
        int duration = 500;
        float mul = 3;
        int delay = 0;

        bottom_dark.setPivotY(bottom_dark.getHeight());
        bottom_dark.animate().setDuration(duration).scaleY((float)1);
        bottom_light.setPivotY(bottom_light.getHeight());
        bottom_light.animate().setDuration(duration).scaleY((float)1);
        top.setPivotY(0);
        top.animate().setDuration(duration).scaleY((float)1);


        options_log.animate().setDuration(duration).translationXBy(options_log.getMeasuredWidth() * mul).setStartDelay(delay);
        logo.animate().setDuration(duration).translationXBy(logo.getMeasuredWidth() * mul).setStartDelay(delay);
        skip.animate().setDuration(duration).translationXBy(options_log.getMeasuredWidth() * mul).setStartDelay(delay);

        form_signup.animate().setDuration(duration).translationXBy(form_signup.getMeasuredWidth() * mul).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(context, LogOptions.class);
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
    public void register(View view) {
        int duration = 500;
        float mul = -3;
        int delay = 0;
        top.setPivotY(0);
        top.animate().setDuration(duration).scaleY((float)1);
        warning.animate().setDuration(duration).translationXBy(warning.getMeasuredWidth() * mul).setStartDelay(delay);
        form_signup.animate().setDuration(duration).translationXBy(form_signup.getMeasuredWidth() * mul).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(context, Searchable.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });

    }
}