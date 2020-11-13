package com.example.refugees;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class LogOptions extends AppCompatActivity {
    Context context = this;
    ImageView top;
    ImageView bottom_dark;
    ImageView bottom_light;
    ImageView logo;
    LinearLayout log;
    Interpolator interpolator = new FastOutSlowInInterpolator() ;

    String language;
    int duration = 550;
    float ScreenWidth;
    float ScreenHeight;
    int direction;
    boolean pressed;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        direction = getIntent().getIntExtra("direction", 1);


        SharedPreferences lang = getSharedPreferences("LANGUAGE_PREFERENCE", Context.MODE_PRIVATE);
        String lng = lang.getString("lang",null);
        if(!lng.equals(null)) {
            setApplocale(lng);
            if(user != null && user.isEmailVerified()){
//                TODO: uncommit this
//            if(false) {
                Intent intent = new Intent(this, MainScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        } else {
            language =  getIntent().getStringExtra("language");
            setApplocale(language);
        }

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
        pressed = false;
        top = findViewById(R.id.top);
        bottom_light = findViewById(R.id.bottom_light);
        bottom_dark = findViewById(R.id.bottom_dark);
        logo = findViewById(R.id.logo);
        log = findViewById(R.id.log);
    }
    public void setup() {
        if(direction == -1) {
            findViewById(R.id.anime_mid).animate().setDuration(100).alpha(0);
            findViewById(R.id.anime).setAlpha(0);
        }
        else {
            findViewById(R.id.anime).animate().setDuration(100).alpha(0);
            findViewById(R.id.anime_mid).setAlpha(0);
        }
        top.setPivotY(0);
        bottom_dark.setPivotY(bottom_dark.getHeight());
        bottom_light.setPivotY(bottom_light.getHeight());
        if(direction == -1) {
            logo.setX(logo.getWidth() * -1);
            bottom_dark.setScaleY(0.1f);
            bottom_light.setScaleY(0.13f);
            top.setScaleY(0.2f);
        }
        logo.setY(ScreenHeight + ((ScreenHeight * 0.85f) * -1) + (top.getHeight()) + ((bottom_dark.getHeight())/4f * -1));
        bottom_dark.setY(ScreenHeight - bottom_dark.getHeight());
        bottom_light.setY(ScreenHeight - bottom_light.getHeight());
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
        findViewById(R.id.anime_mid).animate().setDuration(200).alpha(1);
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
    public void skip(View view) {
        findViewById(R.id.anime_mid).animate().setDuration(100).alpha(1);
        bottom_light.animate().setDuration(duration - 200).scaleY(0f);
        bottom_dark.animate().setDuration(duration - 200).scaleY(0f);
        top.animate().setDuration(duration - 200).scaleY(0f);
        log.animate().setDuration(duration).translationXBy(log.getWidth() * -1).setInterpolator(interpolator);
        logo.animate().setDuration(duration).translationXBy(logo.getWidth() * -1).setInterpolator(interpolator).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = intent = new Intent(context,  MainScreenActivity.class);;
                startActivity(intent);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        if(pressed)
            return;
        pressed = true;
        animate(direction).setListener(new AnimatorListenerAdapter() {
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