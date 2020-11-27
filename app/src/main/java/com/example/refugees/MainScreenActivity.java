package com.example.refugees;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
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
import androidx.navigation.Navigation;

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

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.refugees.MainScreenFragments.SettingsFragment.arrow;
import static com.example.refugees.MainScreenFragments.SettingsFragment.chooseLangLayout;
import static com.example.refugees.MainScreenFragments.SettingsFragment.selectedLang;

public class MainScreenActivity extends AppCompatActivity {
    public static NavigationView navView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavController navController;
    private View navHostFragment;
    public static CircleImageView navImg;
    public static TextView navName;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private FirebaseUser user;
    private CircleImageView profileImg;
    public static Uri imageUri;
    public static boolean imgChangedListener = false;

    float ScreenWidth;
    float ScreenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences lang = getSharedPreferences("LANGUAGE_PREFERENCE", Context.MODE_PRIVATE);
        String lng = lang.getString("lang", "null");
        setApplocale(lng);
        setContentView(R.layout.activity_main_screen);

        setupNavDrawer();
        final DrawerLayout layout = findViewById(R.id.drawer_layout);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                ScreenWidth = displayMetrics.widthPixels;
                ScreenHeight = displayMetrics.heightPixels;
                setTheNav();
                setup();


            }
        });

        itemSelect();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) {
            navView.getMenu().findItem(R.id.login).setVisible(false);
            retrieveUserInfo();
        } else {
            navView.getMenu().findItem(R.id.profile).setVisible(false);
            navView.getMenu().findItem(R.id.notification).setVisible(false);
            navView.getMenu().findItem(R.id.logout).setVisible(false);
        }

    }

    public void setup() {
        findViewById(R.id.anime_mid).animate().setDuration(200).alpha(0);
        toolbar.setY(toolbar.getHeight() * -1);
        toolbar.animate().setDuration(1000).translationY(0);
    }
    public void itemSelect() {
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("item_select", item.getItemId() + "");
                if (item.getItemId() == R.id.logout) {
                    mAuth.signOut();
                    SharedPreferences lang = getSharedPreferences("LANGUAGE_PREFERENCE", Context.MODE_PRIVATE);
                    String lng = lang.getString("lang", "null");
                    Intent intent = new Intent(MainScreenActivity.this, LogOptions.class);
                    intent.putExtra("language", lng);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.login) {
                    SharedPreferences lang = getSharedPreferences("LANGUAGE_PREFERENCE", Context.MODE_PRIVATE);
                    String lng = lang.getString("lang", "null");
                    Intent intent = new Intent(MainScreenActivity.this, LogOptions.class);
                    intent.putExtra("language", lng);
                    startActivity(intent);
                } else {
                    navController.popBackStack();
                    navController.navigate(item.getItemId());
                    item.setChecked(true);
                    drawerLayout.closeDrawers();
                }
                return true;
            }
        });

    }

    public void setTheNav() {
        navHostFragment = findViewById(R.id.navHostFragment);
        Rect rectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        navView.setTranslationY(toolbar.getHeight() + statusBarHeight);
        navView.setPadding(0,0,0,toolbar.getHeight() + statusBarHeight + 30);
        navHostFragment.setTranslationY(toolbar.getHeight() + statusBarHeight);
        navHostFragment.setPadding(0, 0, 0, toolbar.getHeight() + statusBarHeight);
        navHostFragment.setTranslationY(toolbar.getHeight());
        navHostFragment.setPadding(0,0,0,toolbar.getHeight());
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.close();
        } else {
//            toolbar.animate().setDuration(1000).translationY(toolbar.getHeight() * -1);
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

        boolean isAr = getResources().getConfiguration().locale.getLanguage().equals("ar");
        if (isAr) {
            navView.setItemBackground(getDrawable(R.drawable.item_select_state_ar));
            toolbar.setBackground(getDrawable(R.drawable.img_up2));
        } else {
            navView.setItemBackground(getDrawable(R.drawable.item_select_state_en));
            toolbar.setBackground(getDrawable(R.drawable.img_up));
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navController = Navigation.findNavController(this, R.id.navHostFragment);
        navView.setCheckedItem(R.id.home);
    }

    public void retrieveUserInfo() {
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void ChangeLang(@NotNull View view) {

        SharedPreferences langPreference = getSharedPreferences("LANGUAGE_PREFERENCE", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = langPreference.edit();

        switch (view.getId()) {
            case R.id.radioButton_english: {
                setApplocale("en");
                editor.putString("lang", "en").commit();
                chooseLangLayout.setVisibility(View.GONE);
                arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_down));
                selectedLang.setText(getString(R.string.english));
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                break;
            }
            case R.id.radioButton_arabic: {
                setApplocale("ar");
                editor.putString("lang", "ar").commit();
                chooseLangLayout.setVisibility(View.GONE);
                arrow.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_down));
                selectedLang.setText(getString(R.string.arabic));
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                break;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setApplocale(String language) {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        configuration.locale = new Locale(language);
        configuration.setLayoutDirection(new Locale(language));
        resources.updateConfiguration(configuration, displayMetrics);
    }

    public void GotoLogOps(View view){
        if(user != null)
            mAuth.signOut();
        SharedPreferences lang = getSharedPreferences("LANGUAGE_PREFERENCE", Context.MODE_PRIVATE);
        String lng = lang.getString("lang", "null");
        Intent intent = new Intent(MainScreenActivity.this, LogOptions.class);
        intent.putExtra("language", lng);
        startActivity(intent);
        finish();
    }

}