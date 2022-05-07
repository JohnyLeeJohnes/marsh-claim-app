package com.example.applicationmarsh.Activities.LeftMenu;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.applicationmarsh.DBC.DatabaseRequester;
import com.example.applicationmarsh.R;
import com.example.applicationmarsh.Utilities.Global;
import com.example.applicationmarsh.Utilities.LocaleHelper;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    //Text views - init
    private EditText email = null;
    private EditText oldPass = null;
    private EditText newPass = null;
    private TextView passHeader = null;
    private TextView emailHeader = null;
    private TextInputLayout oldPassLayout = null;
    private TextInputLayout newPassLayout = null;
    private TextInputLayout emailLayout = null;

    public ProgressBar pb;

    public enum UpdatedItem {Email, Password}

    public static UpdatedItem updatedItem = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Init progress_bar bar
        pb = findViewById(R.id.profileLoading);

        //Set toolbar - backarrow
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Init Textviews
        initActivityViews();

        Button applyButton = findViewById(R.id.profile_apply_button);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Check if something is changed
                if (updatedItem == null) {
                    return;
                }

                //Init hash map of elements
                Map<EditText, TextInputLayout> inputs = new HashMap<>();
                //Init params
                Map<String, String> reqParams = new HashMap<>();

                //Check if empty -> init params
                switch (updatedItem) {
                    case Email:
                        inputs.put(email, emailLayout);
                        inputs.put(oldPass, oldPassLayout);
                        reqParams.put("email", email.getText().toString());
                        break;
                    case Password:
                        inputs.put(oldPass, oldPassLayout);
                        inputs.put(newPass, newPassLayout);
                        reqParams.put("newPass", newPass.getText().toString());
                        break;
                }

                if (!Global.checkErrors(inputs)) {
                    //Insert communal params
                    reqParams.put("oldPass", oldPass.getText().toString());
                    reqParams.put("username", Global.loggedUser.getUsername());

                    //Send DB request to server
                    DatabaseRequester.sendChangeProfileRequest(reqParams, DatabaseRequester.Action.ProfileChange, ProfileActivity.this, updatedItem);
                }
            }
        });
    }

    //Init User Textviews
    private void initActivityViews() {
        //Init user info
        TextView fullname = findViewById(R.id.date);
        TextView username = findViewById(R.id.profile_username);
        email = findViewById(R.id.profile_email);
        oldPass = findViewById(R.id.profile_old_password);
        newPass = findViewById(R.id.profile_new_password);

        //Init headers
        emailHeader = findViewById(R.id.change_email_header);
        passHeader = findViewById(R.id.change_password_header);

        //Init layouts
        oldPassLayout = findViewById(R.id.InputLayoutOldPass);
        newPassLayout = findViewById(R.id.InputLayoutNewPass);
        emailLayout = findViewById(R.id.InputLayoutEmail);


        //Asign User Values
        fullname.setText(String.format("%s %s", Global.loggedUser.getFirstname(), Global.loggedUser.getLastname()));
        username.setText(Global.loggedUser.getUsername());
        email.setText(Global.loggedUser.getEmail());

        //Create listeners
        email.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!email.getText().toString().trim().equals(Global.loggedUser.getEmail().trim())) {
                    passHeader.setVisibility(View.GONE);
                    newPassLayout.setVisibility(View.GONE);
                    updatedItem = UpdatedItem.Email;
                } else {
                    passHeader.setVisibility(View.VISIBLE);
                    newPassLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        newPass.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!newPass.getText().toString().trim().equals("")) {
                    emailLayout.setVisibility(View.GONE);
                    emailHeader.setVisibility(View.GONE);
                    updatedItem = UpdatedItem.Password;
                } else {
                    emailLayout.setVisibility(View.VISIBLE);
                    emailHeader.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        //End activity
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        //End activity
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.wrap(newBase, Global.languague));
    }

}
