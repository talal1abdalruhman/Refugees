package com.example.refugees;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.example.refugees.HelperClasses.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class Login extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "language";
    private static final String LOGIN_TAG = "loginProccess";
    Context context = this;
    ImageView top;
    ImageView bottom_dark;
    ImageView bottom_light;
    LinearLayout form;
    ImageView logo;
    LinearLayout reset;
    Interpolator interpolator = new FastOutSlowInInterpolator() ;

    int duration = 550;
    float ScreenWidth;
    float ScreenHeight;
    int direction;
    boolean pressed;

    private TextInputEditText email, password;
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private CircularProgressButton loginBtn;
    private TextInputLayout emailLayout, passwordLayout;
    Dialog dialog;

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
        pressed = false;
        top = findViewById(R.id.top);
        bottom_light = findViewById(R.id.bottom_light);
        bottom_dark = findViewById(R.id.bottom_dark);
        bottom_light = findViewById(R.id.bottom_light);
        logo = findViewById(R.id.logo);
        reset = findViewById(R.id.reset);
        top = findViewById(R.id.top);
        form = findViewById(R.id.form);

        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);
        loginBtn = findViewById(R.id.login_btn);
        emailLayout =  findViewById(R.id.login_email_layout);
        passwordLayout = findViewById(R.id.login_password_layout);
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.login_verify_email_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void setup() {
        findViewById(R.id.anime_mid).animate().setDuration(100).alpha(0);
        form.setX(ScreenWidth * direction);
        top.setPivotY(0);
        bottom_dark.setY(ScreenHeight - bottom_dark.getHeight());
        bottom_light.setY(ScreenHeight - bottom_light.getHeight());
        bottom_dark.setPivotY(bottom_dark.getHeight());
        bottom_light.setPivotY(bottom_light.getHeight());
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
        findViewById(R.id.anime_mid).animate().setDuration(150).alpha(1);
        animate(direction).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(context,  Signup.class);;
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                String message = getResources().getConfiguration().locale.getLanguage();
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
                finish();
            }
        });
    }

    public void reset(View view) {
        findViewById(R.id.anime_mid).animate().setDuration(150).alpha(1);
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

    @Override
    public void onBackPressed() {
        if(dialog.isShowing()){
            dialog.dismiss();
        }else if(pressed)
            return;
        pressed = true;
        findViewById(R.id.anime_mid).animate().setDuration(150).alpha(1);
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

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && user.isEmailVerified()) {
            startActivity(new Intent(this, MainScreenActivity.class));
            finish();
        }
    }

    public void SignIn(View view) {

        if(!isConnect()) return;

        String textEmail = email.getText().toString();
        String textPassword = password.getText().toString();
        Validation validator = new Validation(getResources());
        emailLayout.setError(null);
        emailLayout.setErrorEnabled(false);
        passwordLayout.setError(null);
        emailLayout.setErrorEnabled(false);

        if (!validator.validateLoginEmail(emailLayout) | !validator.validateLoginPassword(passwordLayout)) return;
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
                            bottom_light.animate().setDuration(duration - 200).scaleY(0f);
                            bottom_dark.animate().setDuration(duration - 200).scaleY(0f);
                            top.animate().setDuration(duration - 200).scaleY(0f);
                            form.animate().setDuration(duration).translationXBy(form.getWidth() * -1).setInterpolator(interpolator).setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    Intent intent = new Intent(Login.this, MainScreenActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    startActivity(intent);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });
                            loginBtn.stopAnimation();
                        } else {
                            Log.d(LOGIN_TAG, "NOT Verified");
                            loginBtn.stopAnimation();
                            loginBtn.revertAnimation();
                            loginBtn.setBackground(getDrawable(R.drawable.login_btn_bg));
                            dialog.show();
                        }

                    } else {

                        if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                            passwordLayout.setError(getString(R.string.incorrect_password));
                            passwordLayout.requestFocus();
                        }else if (task.getException() instanceof FirebaseAuthInvalidUserException){
                            emailLayout.setError(getString(R.string.email_address_not_register));
                            emailLayout.requestFocus();
                        }
                    }
                }
            });
    }

    public void SendVerification(View view){
        SharedPreferences lang = getSharedPreferences("LANGUAGE_PREFERENCE", Context.MODE_PRIVATE);
        String lng = lang.getString("lang", "null");
        if(!lng.equals("null")) {
            mAuth.setLanguageCode(lng);
        }
        mAuth.useAppLanguage();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    dialog.dismiss();
                    Log.d(LOGIN_TAG, "verification email sent");
                }
            });
        }
    }

    public boolean isConnect() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            Toast.makeText(context, "No Internet Connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}