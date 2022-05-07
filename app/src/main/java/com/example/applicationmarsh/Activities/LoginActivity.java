package com.example.applicationmarsh.Activities;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.applicationmarsh.DBC.DatabaseRequester;
import com.example.applicationmarsh.R;
import com.example.applicationmarsh.Utilities.Global;
import com.example.applicationmarsh.Utilities.LocaleHelper;
import com.google.android.material.textfield.TextInputLayout;


import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    public ProgressBar pb = null;
    private boolean localeChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Init items
        initItems();
    }

    //Init all layout items
    private void initItems() {
        //Init global
        new Global(this);

        //Get loading screen
        pb = findViewById(R.id.loginLoading);

        //Get spinner/drop down for locale
        final Spinner localeDropDown = findViewById(R.id.locale_dropdown);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.locale_list, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        localeDropDown.setAdapter(adapter);

        //Set default selection
        switch (Global.languague) {
            case "cs":
                localeDropDown.setSelection(0);
                break;
            case "en":
                localeDropDown.setSelection(1);
                break;
        }

        //Set listener
        localeDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (localeChecked) {
                    //Set locale
                    switch (position) {
                        case 0:
                            if (!Global.languague.equals("cs")) {
                                Global.languague = "cs";
                                refreshActivity();
                            }
                            break;
                        case 1:
                            if (!Global.languague.equals("en")) {
                                Global.languague = "en";
                                refreshActivity();
                            }
                            break;
                    }
                } else {
                    localeChecked = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Add login event button
        Button loginBtn = findViewById(R.id.loginButton);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get text fields
                EditText username = findViewById(R.id.userText);
                EditText password = findViewById(R.id.passText);

                //Get layouts
                TextInputLayout usernameLayout = findViewById(R.id.InputLayoutLoginUsername);
                TextInputLayout passwordLayout = findViewById(R.id.InputLayoutLoginPassword);

                //Init hash map of elements
                Map<EditText, TextInputLayout> loginInputs = new HashMap<>();
                loginInputs.put(username, usernameLayout);
                loginInputs.put(password, passwordLayout);

                if (!Global.checkErrors(loginInputs)) {
                    //Create request object
                    Map<String, String> reqParams = new HashMap<>();
                    reqParams.put("username", username.getText().toString());
                    reqParams.put("password", password.getText().toString());

                    //Send DB request to server
                    DatabaseRequester.sendLoginRequest(reqParams, DatabaseRequester.Action.Login, LoginActivity.this);
                }
            }
        });

        //Add hyperlink -> registration form
        TextView registerLink = findViewById(R.id.register_link);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    //Call splash activity again
    private void refreshActivity() {
        this.startActivity(new Intent(LoginActivity.this, SplashActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        //Exit if pressed back button
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.wrap(newBase, Global.languague));
    }

}
