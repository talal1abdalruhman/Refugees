package com.example.refugees;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Locale;

public class Searchable extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "language";
    Context context = this;
    ImageView bottom_dark;
    ImageView bottom_light;
    ConstraintLayout warning;
    ImageView top;
    ScrollView form_signup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        final ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.background);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setup();
            }

        });
        bottom_dark = findViewById(R.id.bottom_dark);
        bottom_light = findViewById(R.id.bottom_light);
        warning = findViewById(R.id.warning);
        form_signup = findViewById(R.id.form_signup);
    }
    public void setup() {
        int duration = 0;
        int mul = -3;
        int delay = 0;
        bottom_dark.setPivotY(bottom_dark.getHeight());
        bottom_dark.animate().setDuration(duration).scaleY((float)0.1);
        bottom_light.setPivotY(bottom_light.getHeight());
        bottom_light.animate().setDuration(duration).scaleY((float)0.13);

        form_signup.animate().setDuration(duration).translationXBy(form_signup.getMeasuredWidth() * mul);

        findViewById(R.id.inner).setVisibility(View.VISIBLE);


    }
    public void onBackPressed() {
        int duration = 500;
        float mul = 3;
        int delay = 0;
        top = findViewById(R.id.top);
        top.setPivotY(0);
        top.animate().setDuration(duration).scaleY((float)0.2);
        form_signup.animate().setDuration(duration).translationXBy(form_signup.getMeasuredWidth() * mul);
        warning.animate().setDuration(duration).translationXBy(warning.getMeasuredWidth() * mul).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(getApplicationContext(), Signup.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                String message = getResources().getConfiguration().locale.getLanguage();
                intent.putExtra(EXTRA_MESSAGE, message);
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
    public void enable(View view) {
    }
    public void disable(View view) {
    }
}