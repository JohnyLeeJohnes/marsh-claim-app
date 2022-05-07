package com.example.applicationmarsh.Fragments.BottomMenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.applicationmarsh.Activities.Help.CarHelpActivity;
import com.example.applicationmarsh.Activities.Help.FaqActivity;
import com.example.applicationmarsh.R;

public class DashboardFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //Car accident on click event
        ConstraintLayout carAccident = view.findViewById(R.id.car_accident_layout);
        carAccident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashboardFragment.this.startActivity(new Intent(DashboardFragment.this.getActivity(), CarHelpActivity.class));
            }
        });

        ConstraintLayout faqLayout = view.findViewById(R.id.faq_layout);
        faqLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DashboardFragment.this.startActivity(new Intent(DashboardFragment.this.getActivity(), FaqActivity.class));
            }
        });

        return view;
    }
}
