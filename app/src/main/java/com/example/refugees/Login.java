package com.example.refugees;

import androidx.annotation.RequiresApi;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

import java.util.Locale;
import java.util.Objects;

public class Login extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "language";
    private static final String LOGIN_TAG = "loginProccess";
    Context context = this;
    ImageView top;
    ImageView bottom_dark;
    ImageView bottom_light;
    ImageView logo;
    LinearLayout reset;
    ImageView top;
    ScrollView form_signup;

    private TextInputEditText email, password;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private CircularProgressButton loginBtn;

    LinearLayout log;
    LinearLayout form;
    Interpolator interpolator = new FastOutSlowInInterpolator() ;
    String language;
    int duration = 500;
    int delay = 100;
    float ScreenWidth;
    float ScreenHeight;
    int direction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        direction = getIntent().getIntExtra("direction", 1);
        setContentView(R.layout.activity_login);
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
        bottom_light = findViewById(R.id.bottom_light);
        bottom_dark = findViewById(R.id.bottom_dark);
        bottom_light = findViewById(R.id.bottom_light);
        logo = findViewById(R.id.logo);
        reset = findViewById(R.id.reset);
        top = findViewById(R.id.top);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.login_btn);
        form = findViewById(R.id.form);
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
        mul = 3;
        reset.animate().setDuration(duration).translationXBy(reset.getMeasuredWidth() * mul).setStartDelay(delay);
        form_signup.animate().setDuration(duration).translationXBy(form_signup.getMeasuredWidth() * mul).setStartDelay(delay);

        reset.setVisibility(View.VISIBLE);
        form_signup.setVisibility(View.VISIBLE);
        findViewById(R.id.inner).setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {

        int duration = 500;
        float mul = 3;
        int delay = 0;
        form.setX(ScreenWidth * direction);
        bottom_dark.setPivotY(bottom_dark.getHeight());
        bottom_light.setPivotY(bottom_light.getHeight());
        top.setPivotY(0);
        bottom_dark.setScaleY(0.1f);
        bottom_light.setScaleY(0.13f);
        top.setScaleY(0.2f);
        animate();
    }
    public ViewPropertyAnimator animate() {
        return form.animate().setDuration(duration).translationXBy((ScreenWidth/2 + form.getWidth()/2f) * -1 * direction).setInterpolator(interpolator);
    }
    public ViewPropertyAnimator animate(int next) {
        return form.animate().setDuration(duration).translationXBy(form.getWidth() * -1 * direction * next).setInterpolator(interpolator);
    }
    public void click(View view) {
        animate(direction).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = intent = new Intent(context,  Signup.class);;
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                finish();
            }
        });
    }
    public void reset(View view) {
        animate(direction).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = intent = new Intent(context,  Reset.class);;
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
        top.setPivotY(0);
        top.animate().setDuration(duration).scaleY((float)0.2);

        logo.animate().setDuration(duration).translationXBy(logo.getMeasuredWidth() * mul).setStartDelay(delay);
        form.animate().setDuration(duration).translationXBy(form.getMeasuredWidth() * mul).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {@Override public void onAnimationEnd(Animator animation) {}});
        form_signup.animate().setDuration(duration).translationXBy(form_signup.getMeasuredWidth() * mul).setStartDelay(delay).setListener(new AnimatorListenerAdapter() {
    @Override
    public void onBackPressed() {
        animate(direction).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(getApplicationContext(), LogOptions.class);
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

    // TODO: uncomment this method when create Dashboard activity
//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser user = mAuth.getCurrentUser();
//        if (user != null) {
//            startActivity(new Intent(this, DashBoard.class));
//            finish();
//        }
//    }

    public void SignIn(View view) {

        String textEmail = email.getText().toString();
        String textPassword = password.getText().toString();

        if (!textEmail.isEmpty() && !textPassword.isEmpty()) {
            loginBtn.startAnimation();
            mAuth.signInWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(LOGIN_TAG, "success");
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        boolean emailVerified = user.isEmailVerified();
                        String token = FirebaseInstanceId.getInstance().getToken();
                        mRef = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());
                        mRef.child("device_token").child("token").setValue(token).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                    Log.d(LOGIN_TAG, "token success");
                                else
                                    Log.d(LOGIN_TAG, " token field");
                                loginBtn.stopAnimation();
                            }
                        });
                        if (emailVerified) {
                            // TODO: uncomment this when create Dashboard activity
//                            Intent intent = new Intent(Login.this, DashBoard.class);
//                            startActivity(intent);
                            loginBtn.stopAnimation();
                        } else {
                            Log.d(LOGIN_TAG, "NOT Verified");
                            loginBtn.stopAnimation();
                        }

                    } else {
                        Log.d(LOGIN_TAG, "NOT success");
                        loginBtn.stopAnimation();
                    }
                }
            });
        }

    }


}