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

import com.example.applicationmarsh.Activities.Claims.CreateCarActivity;
import com.example.applicationmarsh.Activities.Claims.ViewClaimActivity;
import com.example.applicationmarsh.DBC.DatabaseRequester;
import com.example.applicationmarsh.R;
import com.example.applicationmarsh.Utilities.Adapters.ClaimAdapter;
import com.example.applicationmarsh.Utilities.Entities.CarClaim;
import com.example.applicationmarsh.Utilities.Global;
import com.example.applicationmarsh.Utilities.Recycler.RecyclerItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClaimsFragment extends Fragment {

    private static ArrayList<CarClaim> carClaimList;
    private static ArrayList<RecyclerItem> claims;
    private static ClaimAdapter claimAdapter;
    private static ProgressBar progressBar;

    public static CarClaim chosenClaim;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //Create fragment view
        View view = inflater.inflate(R.layout.fragment_claims, container, false);

        //Init recycler
        initItemList();
        initRecyclerView(view);

        //Button -> redirect to register car accident
        Button registerClaim = view.findViewById(R.id.car_register);
        registerClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClaimsFragment.this.startActivity(new Intent(ClaimsFragment.this.getActivity(), CreateCarActivity.class));
            }
        });

        //Return view
        return view;
    }

    //Update item list
    public static void updateItemList(){
        if(carClaimList != null){
            //Create items
            for (CarClaim c: carClaimList) {
                claims.add(new RecyclerItem(R.drawable.ic_carcrash, c.getSPZ(), c.getDatum()));
            }

            //Update adapter
            claimAdapter.notifyDataSetChanged();

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
        claims = new ArrayList<>();
        carClaimList = DatabaseRequester.getCarClaims(reqParams, DatabaseRequester.Action.SelectClaim);
    }

    //Init item recycler + manager + adapter
    private void initRecyclerView(View view){
        //Init progress bar
        progressBar = view.findViewById(R.id.recyclerLoading);
        progressBar.setVisibility(View.VISIBLE);

        //Init recycler
        RecyclerView claimRecycler = view.findViewById(R.id.claims_recycler);
        claimRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager claimManager = new LinearLayoutManager(view.getContext());
        claimAdapter = new ClaimAdapter(claims);

        //Set manager & adapter to recycler
        claimRecycler.setLayoutManager(claimManager);
        claimRecycler.setAdapter(claimAdapter);

        //Set on click listener
        claimAdapter.setOnItemClickListner(new ClaimAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(carClaimList != null) {
                    //Set chosen claim
                    chosenClaim = carClaimList.get(position);
                    //Redirect to view claim
                    ClaimsFragment.this.startActivity(new Intent(ClaimsFragment.this.getActivity(), ViewClaimActivity.class));
                }
            }
        });
    }

}
