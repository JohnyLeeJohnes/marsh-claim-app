package com.example.applicationmarsh.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.applicationmarsh.DBC.DatabaseRequester;
import com.example.applicationmarsh.R;
import com.example.applicationmarsh.Utilities.Global;
import com.example.applicationmarsh.Utilities.LocaleHelper;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private DialogInterface.OnClickListener dialogListener;
    public ProgressBar pb;

    private TextInputLayout firstnameLayout;
    private TextInputLayout lastnameLayout;
    private TextInputLayout usernameLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;

    private EditText firstname;
    private EditText lastname;
    private EditText username;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Init progress_bar bar
        pb = findViewById(R.id.registerLoading);

        //Create dialog listener
        dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };


        //Add hyperlink -> login form -> back
        TextView loginLink = findViewById(R.id.login_link);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Confirm registration button
        Button registerConfirm = findViewById(R.id.create_drone_insurance);
        registerConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get data
                bindInputs();

                //Init hash map of elements
                Map<EditText, TextInputLayout> registerInputs = new HashMap<>();
                registerInputs.put(firstname, firstnameLayout);
                registerInputs.put(lastname, lastnameLayout);
                registerInputs.put(username, usernameLayout);
                registerInputs.put(email, emailLayout);
                registerInputs.put(password, passwordLayout);

                if (!Global.checkErrors(registerInputs)) {

                    //Create request object
                    Map<String, String> reqParams = new HashMap<>();
                    reqParams.put("firstname", firstname.getText().toString());
                    reqParams.put("lastname", lastname.getText().toString());
                    reqParams.put("username", username.getText().toString());
                    reqParams.put("email", email.getText().toString());
                    reqParams.put("password", password.getText().toString());

                    //Send DB request to server
                    DatabaseRequester.sendRegisterRequest(reqParams, DatabaseRequester.Action.Register, RegisterActivity.this);
                }
            }
        });
    }

    //Init data from layout
    private void bindInputs() {
        //Edit texts
        firstname = findViewById(R.id.register_firstname);
        lastname = findViewById(R.id.register_lastname);
        username = findViewById(R.id.register_username);
        email = findViewById(R.id.register_email);
        password = findViewById(R.id.register_password);

        //Layouts
        firstnameLayout = findViewById(R.id.InputLayoutFirstname);
        lastnameLayout = findViewById(R.id.InputLayoutLastname);
        usernameLayout = findViewById(R.id.InputLayoutUsername);
        emailLayout = findViewById(R.id.InputLayoutEmail);
        passwordLayout = findViewById(R.id.InputLayoutPassword);
    }

    @Override
    public void onBackPressed() {
        //Bind input data
        bindInputs();

        //If something is filled -> ask if really wants to leave
        if (!firstname.getText().toString().isEmpty() || !lastname.getText().toString().isEmpty() || !username.getText().toString().isEmpty() || !email.getText().toString().isEmpty() || !password.getText().toString().isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
            builder.setMessage("Are you sure you want to leave?").setPositiveButton("Yes", dialogListener).setNegativeButton("No", dialogListener).show();
        } else {
            finish();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.wrap(newBase, Global.languague));
    }

}
