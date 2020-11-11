package com.example.refugees;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.example.refugees.HelperClasses.Address;
import com.example.refugees.HelperClasses.User;
import com.example.refugees.HelperClasses.Validation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import de.hdodenhof.circleimageview.CircleImageView;

public class Signup extends AppCompatActivity {
    Context context = this;
    ImageView top;
    ImageView bottom_dark;
    ImageView bottom_light;
    ScrollView form;
    Dialog dialog;

    Interpolator interpolator = new FastOutSlowInInterpolator() ;

    int duration = 550;
    float ScreenWidth;
    float ScreenHeight;
    int direction;
    boolean pressedImage;
    boolean pressed;

    private static final int GALLERY_REQUEST_CODE = 1;
    private String ADD_USER_TAG = "userRegister";
    private TextInputEditText fName, eMail, phoneNo, password, gover, city;
    private TextInputLayout nameLayout, emailLayout, phoneLayout, passwordLayout, governatorLayout, cityLayout;
    private String textName, textEmail, textPhone, textPassword, textGovern, textCity;
    private CircleImageView profileImg;
    private CircularProgressButton registerBtn;
    private Uri imageUri = null;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        direction = getIntent().getIntExtra("direction", 1);
        setContentView(R.layout.activity_signup);
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
        InitializeFields();
        pressed = false;
        top = findViewById(R.id.top);
        bottom_light = findViewById(R.id.bottom_light);
        bottom_dark = findViewById(R.id.bottom_dark);
        form = findViewById(R.id.form_signup);
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.back_signup);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    public void setup() {
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

    // Register a new user methods
    public void animation(String userId) {
        animate(direction).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(context, Searchable.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("user_id", userId);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        if(pressed)
            return;
        if(fName.getText().toString().isEmpty()
         && eMail.getText().toString().isEmpty()
         && phoneNo.getText().toString().isEmpty()
         && password.getText().toString().isEmpty()
         && gover.getText().toString().isEmpty()
         && city.getText().toString().isEmpty()
                && !pressedImage) {
            pressed = true;
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
            return;
        }
        dialog.show();
    }
    public void back(View view) {
        dialog.dismiss();
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
    public void no(View view) {
        dialog.dismiss();
    }
    @Override
    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
    public void InitializeFields() {

        mStorageRef = FirebaseStorage.getInstance().getReference().child("ProfileImages");
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("users");

        fName = findViewById(R.id.register_full_name);
        eMail = findViewById(R.id.register_email);
        phoneNo = findViewById(R.id.register_phone);
        password = findViewById(R.id.register_password);
        gover = findViewById(R.id.register_govrnator);
        city = findViewById(R.id.register_City);
        profileImg = findViewById(R.id.register_img);
        nameLayout = findViewById(R.id.register_layout_name);
        emailLayout = findViewById(R.id.register_layout_email);
        passwordLayout = findViewById(R.id.register_layout_password);
        phoneLayout = findViewById(R.id.register_layout_phone);
        governatorLayout = findViewById(R.id.register_layout_govrnator);
        cityLayout = findViewById(R.id.register_layout_city);
        registerBtn = findViewById(R.id.register_btn);
    }

    public void RegisterUser(View view) {
        textName = fName.getText().toString();
        textEmail = eMail.getText().toString();
        textPhone = phoneNo.getText().toString();
        textPassword = password.getText().toString();
        textGovern = gover.getText().toString();
        textCity = city.getText().toString();

        Validation validator = new Validation(getResources());

        if(!validator.validateName(nameLayout) | !validator.validateEmail(emailLayout) |
                !validator.validatePassword(passwordLayout) | !validator.validatePhoneNo(phoneLayout) |
                !validator.validateNotEmpty(governatorLayout) | !validator.validateNotEmpty(cityLayout)) return;
        VerifyUserByEmail();
        animation("2");
    }
    private void VerifyUserByEmail() {
        emailLayout.setError(null);
        emailLayout.setErrorEnabled(false);
        registerBtn.startAnimation();
            Log.d(ADD_USER_TAG, "VerifyUserByEmail start");
            mAuth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(ADD_USER_TAG, "Auth success");

                        FirebaseUser user = mAuth.getCurrentUser();
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(ADD_USER_TAG, "verification email sent");
                                    UploadUserInfo();
                                } else {
                                    registerBtn.revertAnimation();
                                    registerBtn.setBackground(getDrawable(R.drawable.login_btn_bg));
                                    Log.d(ADD_USER_TAG, "verification email NOT sent");
                                }
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if(e instanceof FirebaseAuthUserCollisionException){
                        emailLayout.setError(getString(R.string.email_already_used));
                        emailLayout.requestFocus();
                    }
                }
            });

    }


    private void UploadUserInfo() {
        if (imageUri != null) {
            final String userId = mAuth.getCurrentUser().getUid();
            StorageReference imgRef = mStorageRef.child(userId + ".jpg");
            imgRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Log.d(ADD_USER_TAG, "Img upload success");
                                String downloadUrl = task.getResult().toString();
                                Address address = new Address(textGovern, textCity);
                                User user = new User(
                                        userId, downloadUrl,
                                        textName, textEmail,
                                        textPhone, textPassword,
                                        address);
                                mDatabaseReference.child(userId).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(ADD_USER_TAG, "user info upload success");

                                        registerBtn.doneLoadingAnimation(R.color.green_done, BitmapFactory.decodeResource(getResources(),R.drawable.ic_done));
                                        animation(userId);
                                    }
                                });

                            } else {
                                registerBtn.revertAnimation();
                                registerBtn.setBackground(getDrawable(R.drawable.login_btn_bg));
                                Log.d(ADD_USER_TAG, "Error: " + task.getException().getMessage());
                            }
                        }
                    });
                }
            });
        } else {
            registerBtn.revertAnimation();
            registerBtn.setBackground(getDrawable(R.drawable.login_btn_bg));
            Log.d(ADD_USER_TAG, "Error: NOT Valid");
        }

    }

    public void PickImage(View view) {
        pressedImage = true;
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();

                try {
                    if (Build.VERSION.SDK_INT < 28) {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                this.getContentResolver(),
                                imageUri);
                        profileImg.setImageBitmap(bitmap);
                    } else {
                        ImageDecoder.Source src = ImageDecoder.createSource(this.getContentResolver(), imageUri);
                        Bitmap bitmap = ImageDecoder.decodeBitmap(src);
                        profileImg.setImageBitmap(bitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}