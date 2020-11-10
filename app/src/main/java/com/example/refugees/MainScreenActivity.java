package com.example.refugees;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainScreenActivity extends AppCompatActivity {
    private NavigationView navView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavController navController;
    public static CircleImageView navImg;
    public static TextView navName;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private FirebaseUser user;
    private CircleImageView profileImg;
    public static Uri imageUri;
    public static boolean imgChangedListener = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_screen);

//        setupNavDrawer();

//        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Log.d("item_select", item.getItemId() + "");
//                if (item.getItemId() == R.id.logout) {
//                    // TODO: handle it when complate using sharedPreference
//                } else {
//                    navController.popBackStack();
//                    navController.navigate(item.getItemId());
//                    item.setChecked(true);
//                    drawerLayout.closeDrawers();
//                }
//                return true;
//            }
//        });
//
//        retrieveUserInfo();

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.close();
        } else {
            super.onBackPressed();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setupNavDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
        drawerLayout.setDrawerElevation(0);
        // TODO: handle the following when finish the sharedPreference
        // TODO: you already did ^^
        boolean isAr = Locale.getDefault().getLanguage().equals("ar");
        if (isAr) {
            navView.setItemBackground(getDrawable(R.drawable.item_select_state_ar));
            toolbar.setBackground(getDrawable(R.drawable.img_up2));
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
//        navController = Navigation.findNavController(this, R.id.navHostFragment);
        navView.setCheckedItem(R.id.home);
    }

    public void retrieveUserInfo() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        View navHeader = navView.getHeaderView(0);
        navImg = (CircleImageView) navHeader.findViewById(R.id.nav_header_img);
        navName = (TextView) navHeader.findViewById(R.id.nav_header_name);

        if (user != null) {
            userRef = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String userName = snapshot.child("full_name").getValue(String.class);
                        String userImgUrl = snapshot.child("image_url").getValue(String.class);
                        Log.d("url_img", userImgUrl);
                        Log.d("url_img", navImg.getId() + "");
                        Picasso.get().load(userImgUrl).into(navImg);
                        navName.setText(userName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final int unmaskedRequestCode = requestCode & 0x0000ffff;

        if (unmaskedRequestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.d("img pick", "onActivityResult: 3");
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                profileImg = findViewById(R.id.profile_img);
                Log.d("img pick", "onActivityResult: 4");
                try {
                    if (Build.VERSION.SDK_INT < 28) {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                                this.getContentResolver(),
                        imageUri);
                        profileImg.setImageBitmap(bitmap);
                    } else {
                        ImageDecoder.Source src = ImageDecoder.createSource(
                                this.getContentResolver(),
                                imageUri);
                        Bitmap bitmap = ImageDecoder.decodeBitmap(src);
                        profileImg.setImageBitmap(bitmap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imgChangedListener = true;
            }
        }
    }

}