package com.example.applicationmarsh.Fragments.Claim;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TimePicker;

import com.example.applicationmarsh.R;
import com.example.applicationmarsh.Utilities.Global;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class CarRegisterStep1 extends Fragment {

    public static Map<String, String> stepOneItems = new HashMap<>();
    public static Map<EditText, TextInputLayout> inputs = new HashMap<>();

    private DatePickerDialog.OnDateSetListener dateDialog;
    private TimePickerDialog.OnTimeSetListener timeDialog;
    private static Calendar calendar;

    private EditText claimDate;
    private EditText claimTime;
    private static EditText policyNumber;
    private static EditText place;

    private TextInputLayout claimDateLayout;
    private TextInputLayout claimTimeLayout;
    private TextInputLayout policyNumberLayout;
    private TextInputLayout placeLayout;

    private LocationManager locationManager;
    private LocationListener locationListener;
    private ProgressBar progressBarLocation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Create fragment view
        View view = inflater.inflate(R.layout.fragment_car_register_step_1, container, false);

        //Init
        initItems(view);
        initDateTime();

        //Set location manager
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                place.setText(getLocation(location.getLatitude(), location.getLongitude()));
                locationManager.removeUpdates(locationListener);
                Global.setLoadingVisible(progressBarLocation, false);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Global.setLoadingVisible(progressBarLocation, false);
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        //fill location button
        locationButton(view);

        return view;
    }

    //Init location button
    private void locationButton(View view) {
        Button button = view.findViewById(R.id.get_location);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10);
                    return;
                }
                Global.setLoadingVisible(progressBarLocation, true);
                locationManager.requestLocationUpdates("gps", 0, 0, locationListener);
            }
        });
    }

    //Get adress -> GPS
    private String getLocation(double lat, double lon) {
        String location = "";

        Geocoder geocoder = new Geocoder(this.getContext(), Locale.getDefault());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation(lat, lon, 10);
            if (addressList.size() > 0) {
                location = addressList.get(0).getAddressLine(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return location;
    }

    //Init layout items
    private void initItems(View view) {
        //Inputs
        progressBarLocation = view.findViewById(R.id.recyclerLoading);
        claimDate = view.findViewById(R.id.claim_date);
        claimTime = view.findViewById(R.id.claim_time);
        policyNumber = view.findViewById(R.id.policy_number);
        place = view.findViewById(R.id.claim_place);

        //Add policy number
        policyNumber.setText(String.valueOf(Global.loggedUser.getPolicyNumber()));

        //Layouts
        claimDateLayout = view.findViewById(R.id.InputLayoutClaimDate);
        claimTimeLayout = view.findViewById(R.id.InputLayoutClaimTime);
        policyNumberLayout = view.findViewById(R.id.InputLayoutPolicyNumber);
        placeLayout = view.findViewById(R.id.InputLayoutClaimPlace);

        //Add to global list
        inputs.clear();
        inputs.put(claimDate, claimDateLayout);
        inputs.put(claimTime, claimTimeLayout);
        inputs.put(policyNumber, policyNumberLayout);
        inputs.put(place, placeLayout);
    }

    //Init dialogs & pickers
    private void initDateTime() {
        calendar = Calendar.getInstance();

        //Date dialog
        dateDialog = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateTime();
            }
        };

        //Time dialog
        timeDialog = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                updateDateTime();
            }
        };

        //Date picker
        claimDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateDialog, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Time picker
        claimTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(getActivity(), timeDialog, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
            }
        });

        //Update datetime
        updateDateTime();
    }

    //Update date & time labels
    private void updateDateTime() {
        String dateFormat = "dd.MM.yy";
        String timeFormat = "HH:mm";

        SimpleDateFormat date = new SimpleDateFormat(dateFormat, Locale.US);
        SimpleDateFormat time = new SimpleDateFormat(timeFormat, Locale.US);

        claimDate.setText(date.format(calendar.getTime()));
        claimTime.setText(time.format(calendar.getTime()));
    }

    //Init elements to global array
    public static void getElements() {
        //Add to global list
        stepOneItems.clear();
        String format = "yyyy-MM-dd HH:mm.ss";
        SimpleDateFormat date = new SimpleDateFormat(format, Locale.US);
        stepOneItems.put("claimDate", date.format(calendar.getTime()));
        stepOneItems.put("policyNumber", policyNumber.getText().toString());
        stepOneItems.put("place", place.getText().toString());
    }
}
