package com.example.applicationmarsh.Activities.LeftMenu;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.example.applicationmarsh.R;
import com.example.applicationmarsh.Utilities.Global;
import com.example.applicationmarsh.Utilities.LocaleHelper;

public class ContactActivity extends AppCompatActivity {

    public static String contact_phone = "+420770193790";
    public static String contact_email = "jan.pavlat@marsh.com";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        //Set toolbar - backarrow
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Init textview
        TextView phone = findViewById(R.id.contact_phone);
        TextView email = findViewById(R.id.contact_email);

        //Phone hypertext
        String contactPhoneLink = "<a href=\"tel:" + contact_phone + "\">" + contact_phone + "</a>";
        phone.setText(HtmlCompat.fromHtml(contactPhoneLink, HtmlCompat.FROM_HTML_MODE_LEGACY));
        phone.setMovementMethod(LinkMovementMethod.getInstance());

        //Email hypertext
        String contactEmailLink = "<a href=\"mailto:" + contact_email + "\">" + contact_email + "</a>";
        email.setText(HtmlCompat.fromHtml(contactEmailLink, HtmlCompat.FROM_HTML_MODE_LEGACY));
        email.setMovementMethod(LinkMovementMethod.getInstance());

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.wrap(newBase, Global.languague));
    }
}
