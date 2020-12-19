package com.example.refugees.HelperClasses;

import android.content.res.Resources;
import com.example.refugees.R;
import com.google.android.material.textfield.TextInputLayout;

public class Validation {
    Resources res;

    public Validation(Resources res) {
        this.res = res;
    }

    public Boolean validateName(TextInputLayout name) {
        String val = name.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            name.setError(res.getString(R.string.field_cannot_be_empty));
            name.requestFocus();
            return false;
        } else {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }

    public Boolean validateEmail(TextInputLayout email) {
        String val = email.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            email.setError(res.getString(R.string.field_cannot_be_empty));
            email.requestFocus();
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(val).matches()) {
            email.setError(res.getString(R.string.invalid_email_address));
            email.requestFocus();
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    public Boolean validatePassword(TextInputLayout password) {
        String val = password.getEditText().getText().toString();
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=\\S+$)" +           //no white spaces
                ".{6,}" +               //at least 6 characters
                "$";

        if (val.isEmpty()) {
            password.setError(res.getString(R.string.field_cannot_be_empty));
            password.requestFocus();
            return false;
        } else if (!val.matches(passwordVal)) {
            password.setError(res.getString(R.string.password_is_too_weak));
            password.requestFocus();
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    public Boolean validatePhoneNo(TextInputLayout phone) {
        String val = phone.getEditText().getText().toString();

        if (val.isEmpty()) {
            phone.setError(res.getString(R.string.field_cannot_be_empty));
            phone.requestFocus();
            return false;
        } else {
            phone.setError(null);
            phone.setErrorEnabled(false);
            return true;
        }
    }

    public Boolean validateNotEmpty(TextInputLayout ss) {
        String val = ss.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            ss.setError(res.getString(R.string.field_cannot_be_empty));
            ss.requestFocus();
            return false;
        } else {
            ss.setError(null);
            ss.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateLoginEmail(TextInputLayout email) {
        String Email = email.getEditText().getText().toString().trim();

        if (Email.isEmpty()) {
            email.setError(res.getString(R.string.field_cannot_be_empty));
            email.requestFocus();
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            email.setError(res.getString(R.string.invalid_email_address));
            email.requestFocus();
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    public boolean validateLoginPassword(TextInputLayout password) {
        String Password = password.getEditText().getText().toString().trim();

        if (Password.isEmpty()) {
            password.setError(res.getString(R.string.field_cannot_be_empty));
            password.requestFocus();
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }

    }

}
