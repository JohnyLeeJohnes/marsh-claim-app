package com.example.applicationmarsh.Fragments.BottomMenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationmarsh.Activities.Insurances.CreateDroneActivity;
import com.example.applicationmarsh.Activities.Insurances.ViewInsuranceActivity;
import com.example.applicationmarsh.DBC.DatabaseRequester;
import com.example.applicationmarsh.R;
import com.example.applicationmarsh.Utilities.Adapters.ClaimAdapter;
import com.example.applicationmarsh.Utilities.Adapters.InsuranceAdapter;
import com.example.applicationmarsh.Utilities.Entities.DroneInsurance;
import com.example.applicationmarsh.Utilities.Global;
import com.example.applicationmarsh.Utilities.Recycler.RecyclerItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InsurancesFragment extends Fragment {

    private static ArrayList<DroneInsurance> droneInsurances;
    private static ArrayList<RecyclerItem> insurances;
    private static InsuranceAdapter insuranceAdapter;
    private static ProgressBar progressBar;

    public static DroneInsurance chosenInsurance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insurances, container, false);

        //Init recycler
        initItemList();
        initRecyclerView(view);

        //Button -> redirect to register drone insurance
        Button createDroneIns = view.findViewById(R.id.drone_insurance_create);
        createDroneIns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsurancesFragment.this.startActivity(new Intent(InsurancesFragment.this.getActivity(), CreateDroneActivity.class));
            }
        });

        return view;
    }

    //Update insurance list
    public static void updateInsuranceList(){
        if(droneInsurances != null){
            //Create items
            for (DroneInsurance c: droneInsurances) {
                insurances.add(new RecyclerItem(R.drawable.ic_drone, String.format("%s",c.getPolicyNumber()), String.format("%s : %s", c.getDatumOd(), c.getDatumDo())));
            }

            //Update adapter
            insuranceAdapter.notifyDataSetChanged();

            //Hide progress bar
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    //Init array of items
    private void initItemList(){
        //Init params
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("username", Global.loggedUser.getUsername());
        reqParams.put("password", Global.loggedUser.getPasswordHash());

        //Send DB request to server
        insurances = new ArrayList<>();
        droneInsurances = DatabaseRequester.getDroneInsurances(reqParams, DatabaseRequester.Action.SelectInsurance);
    }

    //Init item recycler + manager + adapter
    private void initRecyclerView(View view){
        //Init progress bar
        progressBar = view.findViewById(R.id.recyclerLoading);
        progressBar.setVisibility(View.VISIBLE);

        //Init recycler
        RecyclerView insuranceRecycler = view.findViewById(R.id.insurances_recycler);
        insuranceRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager insuranceManager = new LinearLayoutManager(view.getContext());
        insuranceAdapter = new InsuranceAdapter(insurances);

        //Set manager & adapter to recycler
        insuranceRecycler.setLayoutManager(insuranceManager);
        insuranceRecycler.setAdapter(insuranceAdapter);

        //Set on click listener
        insuranceAdapter.setOnItemClickListner(new ClaimAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //Set chosen claim
                chosenInsurance = droneInsurances.get(position);
                //Redirect to view claim
                InsurancesFragment.this.startActivity(new Intent(InsurancesFragment.this.getActivity(), ViewInsuranceActivity.class));
            }
        });
    }
}
