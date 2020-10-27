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
import android.widget.ScrollView;

import com.example.refugees.R;

public class test extends AppCompatActivity {
    Context context = this;
    LinearLayout options_log;
    Button skip;
    ImageView bottom_dark;
    ImageView bottom_light;
    ImageView logo;
    ImageView top;
    LinearLayout form;
    LinearLayout reset;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
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
    }
    public void setup() {
        int duration = 0;
        float mul = -3;
        int delay = 0;

        bottom_dark.setPivotY(bottom_dark.getHeight());
        bottom_dark.animate().setDuration(duration).scaleY((float)0.1);
        bottom_light.setPivotY(bottom_light.getHeight());
        bottom_light.animate().setDuration(duration).scaleY((float)0.13);


    }
//    @Override
//    public void onBackPressed() {
//        int duration = 500;
//        float mul = 3;
//        int delay = 0;
//
//        reset.animate().setDuration(duration).translationXBy(reset.getMeasuredWidth() * mul).setStartDelay(delay);
//        logo.animate().setDuration(duration).translationXBy(logo.getMeasuredWidth() * mul).setStartDelay(delay);
//        form.animate().setDuration(duration).translationXBy(form.getMeasuredWidth() * mul).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                Intent intent = new Intent(getApplicationContext(), Login.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                String message = getResources().getConfiguration().locale.getLanguage();
//                startActivity(intent);
//                finish();
//            }
//        });
//    }
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }   
}