package com.example.refugees;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Signup extends AppCompatActivity {
    Context context = this;
    ImageView top;
    ImageView bottom_dark;
    ImageView bottom_light;
    ScrollView form;
    Interpolator interpolator = new FastOutSlowInInterpolator() ;

    int duration = 500;
    float ScreenWidth;
    float ScreenHeight;
    int direction;

    private static final int GALLERY_REQUEST_CODE = 1;
    private String ADD_USER_TAG = "userRegister";
    private TextInputEditText fName, eMail, phoneNo, password, gover, city;
    private String textName, textEmail, textPhone, textPassword, textGovern, textCity;
    private CircleImageView profileImg;
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
        top = findViewById(R.id.top);
        bottom_light = findViewById(R.id.bottom_light);
        bottom_dark = findViewById(R.id.bottom_dark);
        form = findViewById(R.id.form_signup);
    }
    public void setup() {
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
        return form.animate().setDuration(duration).translationXBy(form.getWidth() * -1 * direction).setInterpolator(interpolator);
    }
    public ViewPropertyAnimator animate(int next) {
        return form.animate().setDuration(duration).translationXBy(form.getWidth() * -1 * direction * next).setInterpolator(interpolator);
    }

    // Register a new user methods
    public void showSearchable(String userId) {}
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
    }

    public void RegisterUser(View view) {
        textName = fName.getText().toString();
        textEmail = eMail.getText().toString();
        textPhone = phoneNo.getText().toString();
        textPassword = password.getText().toString();
        textGovern = gover.getText().toString();
        textCity = city.getText().toString();

        VerifyUserByEmail();
    }
    private void VerifyUserByEmail() {

        if (CheckValidation()) {
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
                                    Log.d(ADD_USER_TAG, "verification email NOT sent");
                                }
                            }
                        });

                    } else {
                        Log.d(ADD_USER_TAG, "Error: " + task.getException().getMessage());
                    }
                }
            });
        } else {
            Log.d(ADD_USER_TAG, "Error: NOT Valid");
        }
    }

    private boolean CheckValidation() {
        if (imageUri != null && !textName.isEmpty()
                && !textEmail.isEmpty() && !textPhone.isEmpty()
                && !textPassword.isEmpty() && !textGovern.isEmpty()
                && !textCity.isEmpty())
            return true;
        return false;
    }

    private void UploadUserInfo() {
        if (CheckValidation()) {
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
                                        animation(userId);
                                    }
                                });

                            } else {
                                Log.d(ADD_USER_TAG, "Error: " + task.getException().getMessage());
                            }
                        }
                    });
                }
            });
        } else {
            Log.d(ADD_USER_TAG, "Error: NOT Valid");
        }

    }

    public void PickImage(View view) {
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