package com.example.refugees.MainScreenFragments;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.transition.Slide;

import com.example.refugees.HelperClasses.Validation;
import com.example.refugees.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class
UpdateEmailFragment extends Fragment {

    public UpdateEmailFragment() {
        // Required empty public constructor
    }

    CircularProgressButton updateBtn, cancelBtn;
    FirebaseUser currentUser;
    TextInputLayout passwordLayout, newEmailLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.LEFT);
        setEnterTransition(slide);
        slide.setSlideEdge(Gravity.LEFT);
        setExitTransition(slide);
        return inflater.inflate(R.layout.fragment_update_email, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        updateBtn = view.findViewById(R.id.settings_update_email);
        cancelBtn = view.findViewById(R.id.settings_update_email_cancel);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        passwordLayout = view.findViewById(R.id.updateEmail_layout_password);
        newEmailLayout = view.findViewById(R.id.updateEmail_layout_email);
        Validation validator = new Validation(getResources());

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordLayout.setError(null);
                passwordLayout.setErrorEnabled(false);
                if(currentUser != null){
                    if(!validator.validateNotEmpty(passwordLayout) | !validator.validateEmail(newEmailLayout))
                        return;
                    updateBtn.startAnimation();
                    String password = passwordLayout.getEditText().getText().toString().trim();
                    AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), password);
                    currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                String newEmail = newEmailLayout.getEditText().getText().toString().trim();
                                currentUser.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
                                                    userRef.child(currentUser.getUid()).child("email").setValue(newEmail)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            updateBtn.revertAnimation();
                                                            updateBtn.setBackground(getResources().getDrawable(R.drawable.login_btn_bg));
                                                            Navigation.findNavController(view).navigate(R.id.action_updateEmailFragment_to_settings);
                                                        }
                                                    });
                                        }
                                    }
                                });
                            } else if(task.getException() instanceof FirebaseAuthInvalidCredentialsException){
                                passwordLayout.setError(getResources().getString(R.string.incorrect_password));
                                passwordLayout.setErrorEnabled(true);
                                updateBtn.revertAnimation();
                                updateBtn.setBackground(getResources().getDrawable(R.drawable.login_btn_bg));
                            } else {
                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                updateBtn.revertAnimation();
                                updateBtn.setBackground(getResources().getDrawable(R.drawable.login_btn_bg));
                            }
                        }
                    });
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_updateEmailFragment_to_settings);
            }
        });
    }
}