package com.example.refugees;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;

public class test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }


    //Now we need the undo the animation
    //BTW this is the worst way to do animations so don't do it XD
//
//    @Override
//    public void onBackPressed() {
//        int duration = 500;
//        float mul = -3;
//        int delay = 0;
//        bottom_dark.setPivotY(bottom_dark.getHeight());
//        bottom_dark.animate().setDuration(duration).scaleY((float)0.1);
//        bottom_light.setPivotY(bottom_light.getHeight());
//        bottom_light.animate().setDuration(duration).scaleY((float)0.13);
//
//        options_log.animate().setDuration(duration).translationXBy(options_log.getMeasuredWidth() * mul).setStartDelay(delay);
//        skip.animate().setDuration(duration).translationXBy(options_log.getMeasuredWidth() * mul).setStartDelay(delay).setListener(new AnimatorListenerAdapter() { @Override public void onAnimationEnd(Animator animation) {}} );
//        form.animate().setDuration(duration).translationXBy(form.getMeasuredWidth() * mul).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                Intent intent = new Intent(context, test.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
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