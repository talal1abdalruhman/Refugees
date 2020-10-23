package com.example.refugees;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Language extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "language";
    String  message = "ENGLISH";
    Context context = this;
    TextView language2;
    LinearLayout options_lang;
    LinearLayout options_log;
    Button skip;
    LinearLayout options_log_ar;
    Button skip_ar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        final ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.background);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setup();
            }
        });
        language2 = findViewById(R.id.langage);
        options_lang = findViewById(R.id.options_lang);
        options_log = findViewById(R.id.options_log);
        skip = findViewById(R.id.skip);
        options_log_ar = findViewById(R.id.options_log_ar);
        skip_ar = findViewById(R.id.skip_ar);
    }
    public void setup() {
        int duration = 0;
        float mul = 2;
        int delay = 0;
        mul = 3;
        options_log.animate().setDuration(duration).translationXBy(options_log.getMeasuredWidth() * mul).setStartDelay(delay);
        options_log_ar.animate().setDuration(duration).translationXBy(options_log_ar.getMeasuredWidth() * mul).setStartDelay(delay);
        skip_ar.animate().setDuration(duration).translationXBy(options_log.getMeasuredWidth() * mul).setStartDelay(delay);
        skip.animate().setDuration(duration).translationXBy(options_log.getMeasuredWidth() * mul).setStartDelay(delay);
    }
    public void move_slide(View view) {
        message = ((Button)view).getText().toString();
        int duration = 500;
        float mul = -3;
        int delay = 0;
        options_lang.animate().setDuration(duration).translationXBy(options_lang.getMeasuredWidth() * mul).setStartDelay(delay);
        language2.animate().setDuration(duration).translationXBy(language2.getMeasuredWidth() * mul).setStartDelay(delay);
        if(view == findViewById(R.id.english)){
            options_log.animate().setDuration(duration).translationXBy(options_log.getMeasuredWidth() * mul).setStartDelay(delay);
            skip.animate().setDuration(duration).translationXBy(options_log.getMeasuredWidth() * mul).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    message = "en";
                    next(message);
                }
            });
        }
        else {
            options_log_ar.animate().setDuration(duration).translationXBy(options_log_ar.getMeasuredWidth() * mul).setStartDelay(delay);
            skip_ar.animate().setDuration(duration).translationXBy(options_log_ar.getMeasuredWidth() * mul).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    message = "ar";
                    next(message);
                }
            });
        }
    }
    public void next(String message) {
        Intent intent = new Intent(getApplicationContext(), LogOptions.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        finish();
    }
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}
