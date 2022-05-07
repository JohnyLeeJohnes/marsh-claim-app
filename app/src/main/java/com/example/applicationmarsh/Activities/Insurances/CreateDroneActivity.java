package com.example.applicationmarsh.Activities.Insurances;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.applicationmarsh.DBC.DatabaseRequester;
import com.example.applicationmarsh.R;
import com.example.applicationmarsh.Utilities.Global;
import com.example.applicationmarsh.Utilities.LocaleHelper;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class CreateDroneActivity extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener dateFromDialog;
    private DatePickerDialog.OnDateSetListener dateToDialog;
    private Calendar dateFromCalendar;
    private Calendar dateToCalendar;

    private EditText datumOd = null;
    private EditText datumDo = null;
    private EditText policyNumber = null;

    private Map<EditText, TextInputLayout> inputs;
    private Map<String, String> reqParams;

    public ProgressBar pb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_drone);

        //Init items
        initItems();
        initDates();

        Button button = findViewById(R.id.create_drone_insurance);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Global.checkErrors(inputs)) {
                    //Init params
                    initParams();

                    //Send request -> save to DB
                    DatabaseRequester.sendRegisterDroneRequest(reqParams, DatabaseRequester.Action.RegisterDrone, CreateDroneActivity.this);
                }
            }
        });
    }

    //Init req params for DB
    private void initParams() {
        String format = "yyyy-MM-dd";
        SimpleDateFormat date = new SimpleDateFormat(format, Locale.US);
        reqParams = new HashMap<>();
        reqParams.put("datumOd", date.format(dateFromCalendar.getTime()));
        reqParams.put("datumDo", date.format(dateToCalendar.getTime()));
        reqParams.put("policyNumber", policyNumber.getText().toString());
        reqParams.put("username", Global.loggedUser.getUsername());
    }

    //Init items on layout
    private void initItems() {
        //Init layouts
        TextInputLayout datumOdLayout = findViewById(R.id.InputLayoutClaimDateFrom);
        TextInputLayout datumDoLayout = findViewById(R.id.InputLayoutClaimDateTo);
        TextInputLayout policyNumberLayout = findViewById(R.id.InputLayoutPolicyNumber);

        //Init edits
        datumOd = findViewById(R.id.insurance_date_from);
        datumDo = findViewById(R.id.insurance_date_to);
        policyNumber = findViewById(R.id.policy_number);

        //Set policy number from logged user
        policyNumber.setText(String.valueOf(Global.loggedUser.getPolicyNumber()));

        //Init progress bar
        pb = findViewById(R.id.drone_register_loading);

        //Create hash map for inputs
        inputs = new HashMap<>();
        inputs.put(datumOd, datumOdLayout);
        inputs.put(datumDo, datumDoLayout);
        inputs.put(policyNumber, policyNumberLayout);
    }

    //Init date pickers
    private void initDates() {
        //Date from dialog
        dateFromCalendar = Calendar.getInstance();
        dateFromDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (isAfterDate(year, monthOfYear, dayOfMonth, Calendar.getInstance())) {
                    if (isBeforeDate(year, monthOfYear, dayOfMonth, dateToCalendar) || isDateEqual(year, monthOfYear, dayOfMonth, dateToCalendar)) {
                        dateFromCalendar.set(Calendar.YEAR, year);
                        dateFromCalendar.set(Calendar.MONTH, monthOfYear);
                        dateFromCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateFrom();
                    } else {
                        Toast.makeText(CreateDroneActivity.this, getString(R.string.message_invalid_date), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CreateDroneActivity.this, getString(R.string.message_date_must_before), Toast.LENGTH_SHORT).show();
                }
            }
        };
        datumOd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateDroneActivity.this, dateFromDialog, dateFromCalendar.get(Calendar.YEAR), dateFromCalendar.get(Calendar.MONTH), dateFromCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Date to dialog
        dateToCalendar = Calendar.getInstance();
        dateToDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                if (isAfterDate(year, monthOfYear, dayOfMonth, dateFromCalendar) || isDateEqual(year, monthOfYear, dayOfMonth, dateToCalendar)) {
                    dateToCalendar.set(Calendar.YEAR, year);
                    dateToCalendar.set(Calendar.MONTH, monthOfYear);
                    dateToCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateTo();
                } else {
                    Toast.makeText(CreateDroneActivity.this, getString(R.string.message_invalid_date), Toast.LENGTH_SHORT).show();
                }
            }
        };
        datumDo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateDroneActivity.this, dateToDialog, dateToCalendar.get(Calendar.YEAR), dateToCalendar.get(Calendar.MONTH), dateToCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Update -> default value
        updateDateFrom();
        updateDateTo();
    }

    //Update date from view
    private void updateDateFrom() {
        String dateFormat = "dd.MM.yy";
        SimpleDateFormat date = new SimpleDateFormat(dateFormat, Locale.US);
        datumOd.setText(date.format(dateFromCalendar.getTime()));
    }

    //Update date to view
    private void updateDateTo() {
        String dateFormat = "dd.MM.yy";
        SimpleDateFormat date = new SimpleDateFormat(dateFormat, Locale.US);
        datumDo.setText(date.format(dateToCalendar.getTime()));
    }

    //If my date is before calendar date
    private static boolean isBeforeDate(int year, int month, int day, Calendar calendar) {
        Calendar myDate = Calendar.getInstance();
        myDate.set(year, month, day);

        return myDate.before(calendar);
    }

    //If my date is after calendar date
    private static boolean isAfterDate(int year, int month, int day, Calendar calendar) {
        Calendar myDate = Calendar.getInstance();
        myDate.set(year, month, day);

        return myDate.after(calendar);
    }

    //Are both dates equal
    private static boolean isDateEqual(int year, int month, int day, Calendar calendar) {
        return calendar.get(Calendar.YEAR) == year && calendar.get(Calendar.MONTH) == month && calendar.get(Calendar.DAY_OF_MONTH) == day;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.wrap(newBase, Global.languague));
    }
}
