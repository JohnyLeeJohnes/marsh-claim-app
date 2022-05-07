package com.example.applicationmarsh.Fragments.Help;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.applicationmarsh.Activities.Claims.CreateCarActivity;
import com.example.applicationmarsh.R;

public class CarAccidentHelp3 extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_car_accident_help3, container, false);

        //Create button -> register claim activity
        Button button = view.findViewById(R.id.button_register);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarAccidentHelp3.this.startActivity(new Intent(CarAccidentHelp3.this.getActivity(), CreateCarActivity.class));
            }
        });

        return view;
    }
}
