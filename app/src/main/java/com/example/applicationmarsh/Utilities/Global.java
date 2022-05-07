package com.example.applicationmarsh.Utilities;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.IconCompat;

import com.example.applicationmarsh.R;
import com.example.applicationmarsh.Utilities.Entities.User;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

public class Global {

    @SuppressLint("StaticFieldLeak")
    public static Context context;
    public static String languague = "cs";

    public Global(Context con) {
        context = con;
    }

    //Date + Time formats
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat dateTimeFormatIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat dateTimeFormatOut = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat dateFormatIn = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat dateFormatOut = new SimpleDateFormat("dd.MM.yyyy");

    //Logged user
    public static User loggedUser = null;

    //Create user from DB - store as global
    public static void createUser(String username, String email, String firstname, String lastname, String passwordHash, int policyNumber) {
        Global.loggedUser = new User(username, email, firstname, lastname, passwordHash, policyNumber);
    }

    //Method to check if input is empty
    public static boolean checkErrors(Map<EditText, TextInputLayout> inputs) {
        boolean isError = false;

        for (Map.Entry<EditText, TextInputLayout> entry : inputs.entrySet()) {
            if (entry.getKey().getText().toString().isEmpty()) {
                entry.getValue().setError(context.getString(R.string.not_empty));
                isError = true;
            } else {
                entry.getValue().setErrorEnabled(false);
                entry.getValue().setError(null);
            }
        }
        return isError;
    }

    //Progress bar visibility control
    public static void setLoadingVisible(ProgressBar progressBar, boolean visibility) {
        if (visibility)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.INVISIBLE);
    }

}
