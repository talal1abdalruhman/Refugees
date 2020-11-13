package com.example.refugees;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

public class Searchable extends AppCompatActivity {
    private final String ADD_USER_TAG = "userRegister";
    Context context = this;
    ImageView top;
    ImageView bottom_dark;
    ImageView bottom_light;
    ConstraintLayout form;
    Interpolator interpolator = new FastOutSlowInInterpolator();

    int duration = 550;
    float ScreenWidth;
    float ScreenHeight;
    int direction;
    boolean pressed;
    int delay = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        direction = getIntent().getIntExtra("direction", 1);
        setContentView(R.layout.activity_searchable);
        final ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.background);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
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
        form = findViewById(R.id.warning);
    }

    public void setup() {
        findViewById(R.id.anime_mid).animate().setDuration(100).alpha(0);
        form.setX(ScreenWidth * direction);
        bottom_dark.setY(ScreenHeight - bottom_dark.getHeight());
        bottom_light.setY(ScreenHeight - bottom_light.getHeight());
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


    @Override
    public void onBackPressed() {
        if (pressed)
            return;
        findViewById(R.id.anime_mid).animate().setDuration(delay).alpha(1);
        pressed = true;
        animate(direction).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(getApplicationContext(), Signup.class);
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


    public void SelectSearchStatus(View view) {
        findViewById(R.id.anime_mid).animate().setDuration(delay).alpha(1);
        animate(direction).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = intent = new Intent(context, Confirm.class);

                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });
        // TODO: uncommit this
//        int id = view.getId();
//        String userId = getIntent().getStringExtra("user_id");
//        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
//        boolean searchable;
//        if (id == R.id.enalbe) searchable = true;
//        else searchable = false;
//        userRef.child("searchable").setValue(searchable).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    Log.d(ADD_USER_TAG, "searchable => " + searchable);
//                    animate(direction).setListener(new AnimatorListenerAdapter() {
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            Intent intent = intent = new Intent(context, Confirm.class);
//
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                            startActivity(intent);
//                            finish();
//                        }
//                    });
//                } else Log.d(ADD_USER_TAG, "searchable => something wrong");
//            }
//        });
    }

}