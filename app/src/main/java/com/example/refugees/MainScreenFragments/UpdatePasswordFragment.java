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

public class UpdatePasswordFragment extends Fragment {

    public UpdatePasswordFragment() {
        // Required empty public constructor
    }

    TextInputLayout currentPass, newPass, confirmPass;
    CircularProgressButton updateBtn, cancelBtn;
    FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.LEFT);
        setEnterTransition(slide);
        slide.setSlideEdge(Gravity.LEFT);
        setExitTransition(slide);
        return inflater.inflate(R.layout.fragment_update_password, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentPass = view.findViewById(R.id.updatePassword_layout_current_password);
        newPass = view.findViewById(R.id.updatePassword_layout_new_password);
        confirmPass = view.findViewById(R.id.updatePassword_layout_confirm_password);
        updateBtn = view.findViewById(R.id.settings_update_password);
        cancelBtn = view.findViewById(R.id.settings_update_password_cancel);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation validator = new Validation(getResources());
                currentPass.setErrorEnabled(false);
                currentPass.setError(null);
                newPass.setErrorEnabled(false);
                newPass.setError(null);
                confirmPass.setErrorEnabled(false);
                confirmPass.setError(null);

                if (!validator.validatePassword(newPass)) return;
                String currentPassText = currentPass.getEditText().getText().toString().trim();
                String newPassText = newPass.getEditText().getText().toString().trim();
                String confirmPassText = confirmPass.getEditText().getText().toString().trim();
                if (!newPassText.equals(confirmPassText)) {
                    confirmPass.setErrorEnabled(true);
                    confirmPass.setError(getString(R.string.password_not_match));
                    return;
                }
                if (currentUser != null) {
                    updateBtn.startAnimation();
                    AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), currentPassText);
                    currentUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                currentUser.updatePassword(newPassText).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
                                            userRef.child(currentUser.getUid()).child("password").setValue(newPassText)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            updateBtn.revertAnimation();
                                                            updateBtn.setBackground(getResources().getDrawable(R.drawable.login_btn_bg));
                                                            Navigation.findNavController(view).navigate(R.id.action_updatePasswordFragment_to_settings);
                                                        }
                                                    });
                                        }
                                    }
                                });

                            } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                currentPass.setError(getResources().getString(R.string.incorrect_password));
                                currentPass.setErrorEnabled(true);
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
                Navigation.findNavController(view).navigate(R.id.action_updatePasswordFragment_to_settings);
            }
        });
    }
}