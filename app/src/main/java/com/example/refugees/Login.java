package com.example.refugees;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

public class Login extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "language";
    Context context = this;
    LinearLayout options_log;
    Button skip;
    ImageView bottom_dark;
    ImageView bottom_light;
    LinearLayout form;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final ConstraintLayout layout = findViewById(R.id.background);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setup();
            }
        });

        form = findViewById(R.id.form);

        options_log = findViewById(R.id.options_log);
        skip = findViewById(R.id.skip);
        bottom_dark = findViewById(R.id.bottom_dark);
        bottom_light = findViewById(R.id.bottom_light);
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
        skip.animate().setDuration(duration).translationXBy(options_log.getMeasuredWidth() * mul).setStartDelay(delay).setListener(new AnimatorListenerAdapter() { @Override public void onAnimationEnd(Animator animation) {}} );
        options_log.setVisibility(View.VISIBLE);
        skip.setVisibility(View.VISIBLE);

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

        options_log.animate().setDuration(duration).translationXBy(options_log.getMeasuredWidth() * mul).setStartDelay(delay);
        skip.animate().setDuration(duration).translationXBy(options_log.getMeasuredWidth() * mul).setStartDelay(delay).setListener(new AnimatorListenerAdapter() { @Override public void onAnimationEnd(Animator animation) {}} );
        form.animate().setDuration(duration).translationXBy(form.getMeasuredWidth() * mul).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(getApplicationContext(), LogOptions.class);
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

}