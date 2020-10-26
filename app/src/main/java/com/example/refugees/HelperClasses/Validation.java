package com.example.refugees.HelperClasses;

import com.google.android.material.textfield.TextInputLayout;

public class Validation {

    public static Boolean validateName(TextInputLayout name) {
        String val = name.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            name.setError("Field cannot be empty");
            return false;
        }
        else {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }

    public static Boolean validateEmail(TextInputLayout email) {
        String val = email.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(val).matches()) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    public static Boolean validatePassword(TextInputLayout password) {
        String val = password.getEditText().getText().toString();
        String passwordVal = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=_])" +    //at least 1 special character
                "(?=\\\\S+$).{6,}$";    //at least 6 characters + no white spaces

        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            password.setError("Password is too weak");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    public static Boolean validatePhoneNo(TextInputLayout phone) {
        String val = phone.getEditText().getText().toString();

        if (val.isEmpty()) {
            phone.setError("Field cannot be empty");
            return false;
        } else {
            phone.setError(null);
            phone.setErrorEnabled(false);
            return true;
        }
    }

    public static Boolean validateNotEmpty(TextInputLayout ss) {
        String val = ss.getEditText().getText().toString().trim();

        if (val.isEmpty()) {
            ss.setError("Field cannot be empty");
            return false;
        }
        else {
            ss.setError(null);
            ss.setErrorEnabled(false);
            return true;
        }
    }

}
