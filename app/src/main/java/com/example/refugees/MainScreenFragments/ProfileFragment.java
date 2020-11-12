package com.example.refugees.MainScreenFragments;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.transition.Slide;

import com.example.refugees.HelperClasses.Address;
import com.example.refugees.HelperClasses.Validation;
import com.example.refugees.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.example.refugees.MainScreenActivity.imageUri;
import static com.example.refugees.MainScreenActivity.imgChangedListener;
import static com.example.refugees.MainScreenActivity.navImg;
import static com.example.refugees.MainScreenActivity.navName;
import static com.example.refugees.MainScreenActivity.navView;

public class ProfileFragment extends Fragment {
    public ProfileFragment() {
        // Required empty public constructor
    }

    private Context context;
    private static final int GALLERY_REQUEST_CODE = 1;
    private View view;
    private CircleImageView profileImg;
    private ImageView updateImgBtn;
    private TextInputLayout name, email, phone, addressG, addressC;
    private String _name, _phone, _addressG, _addressC;
    private SwitchCompat searchSwitch;
    private boolean _searchState;
    private CircularProgressButton updateBtn, saveBtn, cancelBtn;
    private LinearLayout updateBtnLayout, address;
    private FirebaseUser currentUser;
    private DatabaseReference userRef;
    private StorageReference mStorage;
    Validation validator;

    int duration = 300;
    boolean mid_animation = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        InitializeFields();
        context = view.getContext();
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.LEFT);
        setEnterTransition(slide);
        slide.setSlideEdge(Gravity.LEFT);
        setExitTransition(slide);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Navigation.findNavController(view).navigate(R.id.action_profile_to_home);
                navView.setCheckedItem(R.id.home);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        retrieveUserInfo();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setEnabled(true);
                phone.setEnabled(true);
                addressG.setEnabled(true);
                addressC.setEnabled(true);
                searchSwitch.setEnabled(true);
                imgChangedListener = false;
                animate();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setEnabled(false);
                phone.setEnabled(false);
                addressG.setEnabled(false);
                addressC.setEnabled(false);
                searchSwitch.setEnabled(false);
                animate_back();
                retrieveUserInfo();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isImgChanged() | isNameChanged() | isPhoneChanged() | isAddressChanged() | isSearchStateChanged()) {
                    cancelBtn.setEnabled(false);
                    saveBtn.startAnimation();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            retrieveUserInfo();
                            Toast.makeText(getContext(), getString(R.string.profile_updated), Toast.LENGTH_LONG).show();
                            saveBtn.revertAnimation();
                            saveBtn.setBackground(getResources().getDrawable(R.drawable.login_btn_bg));
                            name.setEnabled(false);
                            phone.setEnabled(false);
                            addressG.setEnabled(false);
                            addressC.setEnabled(false);
                            searchSwitch.setEnabled(false);
                            updateImgBtn.setVisibility(View.GONE);
                            email.setVisibility(View.VISIBLE);
                            updateBtn.setVisibility(View.VISIBLE);
                            updateBtnLayout.setVisibility(View.GONE);
                        }
                    }, 1500);
                }
            }
        });

        updateImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
            }
        });
    }

    private void InitializeFields() {
        profileImg = view.findViewById(R.id.profile_img);
        updateImgBtn = view.findViewById(R.id.update_img_btn);
        name = view.findViewById(R.id.profile_layout_name);
        email = view.findViewById(R.id.profile_layout_email);
        phone = view.findViewById(R.id.profile_layout_phone);
        addressG = view.findViewById(R.id.profile_layout_govrnator);
        addressC = view.findViewById(R.id.profile_layout_city);
        searchSwitch = view.findViewById(R.id.searchable_switch);
        updateBtnLayout = view.findViewById(R.id.update_btns_layout);
        updateBtn = view.findViewById(R.id.profile_update_info);
        saveBtn = view.findViewById(R.id.profile_save_changes);
        cancelBtn = view.findViewById(R.id.profile_cancel_update);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
        mStorage = FirebaseStorage.getInstance().getReference("ProfileImages");
        validator = new Validation(getActivity().getResources());
        address = view.findViewById(R.id.address);
    }

    private void retrieveUserInfo() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Picasso.get().load(snapshot.child("image_url").getValue(String.class)).into(profileImg);
                    Picasso.get().load(snapshot.child("image_url").getValue(String.class)).into(navImg);
                    name.getEditText().setText(_name = snapshot.child("full_name").getValue(String.class));
                    navName.setText(_name = snapshot.child("full_name").getValue(String.class));
                    email.getEditText().setText(snapshot.child("email").getValue(String.class));
                    phone.getEditText().setText(_phone = snapshot.child("phone_no").getValue(String.class));
                    Address address = snapshot.child("address").getValue(Address.class);
                    addressG.getEditText().setText(_addressG = address.getGovernorate());
                    addressC.getEditText().setText(_addressC = address.getCity());
                    if (_searchState = snapshot.child("searchable").getValue(boolean.class)) {
                        searchSwitch.setChecked(true);
                    } else {
                        searchSwitch.setChecked(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("img pick", "onActivityResult: 1  " + requestCode);
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            Log.d("img pick", "onActivityResult: 2  "+requestCode);
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(getActivity());
        }

    }

    public boolean isImgChanged() {
        if (imgChangedListener) {
            mStorage.child(currentUser.getUid() + ".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()){
                                String downloadUrl = task.getResult().toString();
                                userRef.child("image_url").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Log.d("profileUpdate", "img uploaded");
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
        return imgChangedListener;
    }

    public boolean isNameChanged() {
        String newName = name.getEditText().getText().toString().trim();
        if (!_name.equals(newName)) {
            if (validator.validateName(name)) {
                userRef.child("full_name").setValue(newName).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("profileUpdate", "name updated");
                        }
                    }
                });
            }
            return true;
        } else return false;
    }

    public boolean isPhoneChanged() {
        String newPhone = phone.getEditText().getText().toString().trim();
        if (!_phone.equals(newPhone)) {
            if (validator.validatePhoneNo(phone)) {
                userRef.child("phone_no").setValue(newPhone).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("profileUpdate", "phone updated");
                        }
                    }
                });
            }
            return true;
        } else return false;
    }

    public boolean isAddressChanged() {
        String newAddressG = addressG.getEditText().getText().toString().trim();
        String newAddressC = addressC.getEditText().getText().toString().trim();
        if (!_addressG.equals(newAddressG) || !_addressC.equals(newAddressC)) {
            if (validator.validateNotEmpty(addressG) && validator.validateNotEmpty(addressC)) {
                Address newAddress = new Address(_addressG, _addressC);
                userRef.child("address").setValue(newAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("profileUpdate", "Address updated");
                        }
                    }
                });
            }
            return true;
        } else return false;
    }

    public boolean isSearchStateChanged() {
        boolean newState = searchSwitch.isChecked();
        if (_searchState != newState) {
            userRef.child("searchable").setValue(newState).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("profileUpdate", "searchable state updated");
                    }
                }
            });
            return true;
        } else return false;
    }
    public void animate() {
        if(mid_animation)
            return;
        mid_animation = true;
        email.setPivotY(email.getHeight());
        email.animate().setDuration(duration).alpha(0f);
        email.animate().setDuration(duration).scaleY(0f);

        phone.animate().setDuration(duration).translationYBy(email.getHeight() * -1);

        address.animate().setDuration(duration).translationYBy(email.getHeight() * -1);

        updateBtnLayout.animate().setDuration(duration).translationYBy(email.getHeight() * -2);
        updateBtnLayout.animate().setDuration(duration).alpha(1);

        updateBtn.animate().setDuration(duration).translationYBy(email.getHeight() * -1);
        updateBtn.animate().setDuration(duration).alpha(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                updateBtn.setVisibility(View.INVISIBLE);
                mid_animation = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        updateImgBtn.animate().setDuration(duration).scaleY(1f);
        updateImgBtn.animate().setDuration(duration).scaleX(1f);
    }
    public void animate_back() {
        if(mid_animation)
            return;
        mid_animation = true;
        Log.d("tag", "" + email.getHeight());
        email.setPivotY(email.getHeight());
        email.animate().setDuration(duration).alpha(1f);
        email.animate().setDuration(duration).scaleY(1f);

        phone.animate().setDuration(duration).translationYBy(email.getHeight());

        address.animate().setDuration(duration).translationYBy(email.getHeight());
        updateBtnLayout.animate().setDuration(duration).translationYBy(email.getHeight() * 2);
        updateBtnLayout.animate().setDuration(duration).alpha(0);

        updateBtn.animate().setDuration(duration).translationYBy(email.getHeight());
        updateBtn.animate().setDuration(duration).alpha(1).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                updateBtn.setVisibility(View.VISIBLE);
                mid_animation = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        updateImgBtn.animate().setDuration(duration).scaleY(0f);
        updateImgBtn.animate().setDuration(duration).scaleX(0f);
    }
}