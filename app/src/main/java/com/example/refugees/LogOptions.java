package com.example.refugees;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class LogOptions extends AppCompatActivity {
    Context context = this;
    String language;
    LinearLayout options_lang;
    LinearLayout options_log;
    Button skip;
    TextView language2;
    ImageView bottom_dark;
    ImageView bottom_light;
    ImageView logo;
    LinearLayout option_search;
    LinearLayout search;
    ImageView back;
    ImageView search_icon;
    TextView search_text;
    TextView dis;
    LinearLayout form;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        language = getIntent().getStringExtra("language");
        try {
            setApplocale(language);
        } catch( java.lang.NullPointerException e) {
            setApplocale("en");
        }
        setContentView(R.layout.activity_log_options);
        final ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.background);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                setup();
            }
        });
        form = findViewById(R.id.form);
        options_lang = findViewById(R.id.options_lang);
        options_log = findViewById(R.id.options_log);
        skip = findViewById(R.id.skip);
        language2 = findViewById(R.id.langage);
        bottom_dark = findViewById(R.id.bottom_dark);
        bottom_light = findViewById(R.id.bottom_light);
        logo = findViewById(R.id.logo);
        search = findViewById(R.id.search);
        option_search = findViewById(R.id.options_search);
        back = findViewById(R.id.back);
        search_icon = findViewById(R.id.search_icon);
        search_text = findViewById(R.id.search_text);
        dis = findViewById(R.id.dis);

    }
    public void setup() {
        int duration = 0;
        float mul = -3;
        int delay = 0;
        options_lang.animate().setDuration(duration).translationXBy(options_lang.getMeasuredWidth() * mul).setStartDelay(delay);
        language2.animate().setDuration(duration).translationXBy(language2.getMeasuredWidth() * mul).setStartDelay(delay);
        mul = 3;
        option_search.animate().setDuration(duration).translationXBy(option_search.getMeasuredWidth() * mul).setStartDelay(delay);
        back.animate().setDuration(duration).translationXBy(back.getMeasuredWidth() * mul).setStartDelay(delay);
        search.animate().setDuration(duration).translationXBy(search.getMeasuredWidth() * mul).setStartDelay(delay);
        dis.animate().setDuration(duration).translationXBy(dis.getMeasuredWidth() * mul).setStartDelay(delay);
        form.animate().setDuration(duration).translationXBy(form.getMeasuredWidth() * mul).setStartDelay(delay);

        options_lang.setVisibility(View.VISIBLE);
        language2.setVisibility(View.VISIBLE);

        option_search.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);
        search.setVisibility(View.VISIBLE);
        dis.setVisibility(View.VISIBLE);

        form.setVisibility(View.VISIBLE);

    }
    public void setApplocale(String language) {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        configuration.locale = new Locale(language);
        resources.updateConfiguration(configuration, displayMetrics);
    }
    @Override
    public void onBackPressed() {
        int duration = 500;
        float mul = 3;
        int delay = 0;
        options_lang.animate().setDuration(duration).translationXBy(options_lang.getMeasuredWidth() * mul).setStartDelay(delay);
        language2.animate().setDuration(duration).translationXBy(language2.getMeasuredWidth() * mul).setStartDelay(delay);
        options_log.animate().setDuration(duration).translationXBy(options_log.getMeasuredWidth() * mul).setStartDelay(delay);
        skip.animate().setDuration(duration).translationXBy(options_log.getMeasuredWidth() * mul).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(getApplicationContext(), Language.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });
    }

    public void signup(View view) {
        int duration = 500;
        float mul = -3;
        int delay = 0;
        bottom_dark.setPivotY(bottom_dark.getHeight());
        bottom_dark.animate().setDuration(duration).scaleY((float)0.1);
        bottom_light.setPivotY(bottom_light.getHeight());
        bottom_light.animate().setDuration(duration).scaleY((float)0.13);

        options_log.animate().setDuration(duration).translationXBy(options_log.getMeasuredWidth() * mul).setStartDelay(delay);
        logo.animate().setDuration(duration).translationXBy(logo.getMeasuredWidth() * mul).setStartDelay(delay);
        skip.animate().setDuration(duration).translationXBy(options_log.getMeasuredWidth() * mul).setStartDelay(delay).setListener(new AnimatorListenerAdapter() { @Override public void onAnimationEnd(Animator animation) {}} );

        option_search.animate().setDuration(duration).translationXBy(option_search.getMeasuredWidth() * mul).setStartDelay(delay);
        back.animate().setDuration(duration).translationXBy(back.getMeasuredWidth() * mul).setStartDelay(delay);
        search.animate().setDuration(duration).translationXBy(search.getMeasuredWidth() * mul).setStartDelay(delay);
        dis.animate().setDuration(duration).translationXBy(dis.getMeasuredWidth() * mul).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(context, Searchable.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });
    }
    public void login(View view) {
        int duration = 500;
        float mul = -3;
        int delay = 0;
        bottom_dark.setPivotY(bottom_dark.getHeight());
        bottom_dark.animate().setDuration(duration).scaleY((float)0.1);
        bottom_light.setPivotY(bottom_light.getHeight());
        bottom_light.animate().setDuration(duration).scaleY((float)0.13);

        options_log.animate().setDuration(duration).translationXBy(options_log.getMeasuredWidth() * mul).setStartDelay(delay);
        skip.animate().setDuration(duration).translationXBy(options_log.getMeasuredWidth() * mul).setStartDelay(delay).setListener(new AnimatorListenerAdapter() { @Override public void onAnimationEnd(Animator animation) {}} );
        form.animate().setDuration(duration).translationXBy(form.getMeasuredWidth() * mul).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(context, test.class);
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
}