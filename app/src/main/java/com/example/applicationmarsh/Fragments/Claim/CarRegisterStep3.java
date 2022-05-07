package com.example.applicationmarsh.Fragments.Claim;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.applicationmarsh.Activities.Claims.CreateCarActivity;
import com.example.applicationmarsh.DBC.DatabaseRequester;
import com.example.applicationmarsh.R;
import com.example.applicationmarsh.Utilities.Global;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class CarRegisterStep3 extends Fragment {

    private TextInputLayout spzLayout;
    private TextInputLayout carTypeLayout;
    private TextInputLayout driverFirstnameLayout;
    private TextInputLayout driverLastnameLayout;
    private TextInputLayout driverEmailLayout;
    private TextInputLayout driverPhoneLayout;

    private EditText spz;
    private EditText carType;
    private EditText driverFirstname;
    private EditText driverLastname;
    private EditText driverEmail;
    private EditText driverPhone;

    private ProgressBar pb;
    private Map<String, String> reqParams;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_register_step_3, container, false);

        //Init layout items
        initItems(view);

        //Create hash map for inputs
        final Map<EditText, TextInputLayout> inputs = new HashMap<>();
        inputs.put(spz, spzLayout);
        inputs.put(carType, carTypeLayout);
        inputs.put(driverFirstname, driverFirstnameLayout);
        inputs.put(driverLastname, driverLastnameLayout);
        inputs.put(driverEmail, driverEmailLayout);
        inputs.put(driverPhone, driverPhoneLayout);

        //Check errors
        Button button = view.findViewById(R.id.create_drone_insurance);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarRegisterStep1.getElements();
                if (!Global.checkErrors(inputs) && !Global.checkErrors(CarRegisterStep1.inputs)) {
                    //Init params
                    initParams();

                    //Send request -> save to DB
                    DatabaseRequester.sendRegisterCarRequest(reqParams, DatabaseRequester.Action.RegisterCar, (CreateCarActivity) getActivity(), pb);
                } else {
                    Toast.makeText(getActivity(), "Some items are missing.\nPlease control the whole form.", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    //Init items
    private void initItems(View view) {
        //Init edittext
        spz = view.findViewById(R.id.car_spz);
        carType = view.findViewById(R.id.car_type);
        driverFirstname = view.findViewById(R.id.driver_firstname);
        driverLastname = view.findViewById(R.id.driver_lastname);
        driverEmail = view.findViewById(R.id.driver_email);
        driverPhone = view.findViewById(R.id.driver_phone);

        //Init layouts
        spzLayout = view.findViewById(R.id.InputLayoutSPZ);
        carTypeLayout = view.findViewById(R.id.InputLayoutCarType);
        driverFirstnameLayout = view.findViewById(R.id.InputLayoutDriverFirstname);
        driverLastnameLayout = view.findViewById(R.id.InputLayoutDriverLastname);
        driverEmailLayout = view.findViewById(R.id.InputLayoutDriverEmail);
        driverPhoneLayout = view.findViewById(R.id.InputLayoutDriverPhone);

        //Progress bar
        pb = view.findViewById(R.id.car_register_loading);

        //Fill known info
        driverFirstname.setText(Global.loggedUser.getFirstname());
        driverLastname.setText(Global.loggedUser.getLastname());
        driverEmail.setText(Global.loggedUser.getEmail());
    }

    //Init req params for DB
    private void initParams() {
        reqParams = new HashMap<>();

        //Init step one items
        for (Map.Entry<String, String> entry : CarRegisterStep1.stepOneItems.entrySet()) {
            reqParams.put(entry.getKey(), entry.getValue());
        }

        //Init step two items
        for (Map.Entry<String, CheckBox> entry : CarRegisterStep2.checkBoxes.entrySet()) {
            reqParams.put(entry.getKey(), String.valueOf(entry.getValue().isChecked() ? 1 : 0));
        }
        CarRegisterStep2.generatePhotosMap();
        for (Map.Entry<String, String> entry : CarRegisterStep2.damagePhotos.entrySet()) {
            reqParams.put(entry.getKey(), entry.getValue());
        }

        //Init step three items
        reqParams.put("spz", spz.getText().toString());
        reqParams.put("carType", carType.getText().toString());
        reqParams.put("driverFirstname", driverFirstname.getText().toString());
        reqParams.put("driverLastname", driverLastname.getText().toString());
        reqParams.put("driverEmail", driverEmail.getText().toString());
        reqParams.put("driverPhone", driverPhone.getText().toString());

        //Insert user
        reqParams.put("username", Global.loggedUser.getUsername());
    }
}
