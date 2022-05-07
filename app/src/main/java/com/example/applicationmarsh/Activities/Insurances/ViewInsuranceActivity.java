package com.example.applicationmarsh.Activities.Insurances;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.example.applicationmarsh.Fragments.BottomMenu.InsurancesFragment;
import com.example.applicationmarsh.R;
import com.example.applicationmarsh.Utilities.Entities.DroneInsurance;
import com.example.applicationmarsh.Utilities.Global;
import com.example.applicationmarsh.Utilities.LocaleHelper;

public class ViewInsuranceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_insurance);

        initItems();
    }

    private void initItems() {
        //Init views
        TextView policyNumber = findViewById(R.id.policy_number);
        TextView datumOd = findViewById(R.id.date_from);
        TextView datumDo = findViewById(R.id.date_to);

        //Load claim
        DroneInsurance insurance = InsurancesFragment.chosenInsurance;

        //Fill textviews
        policyNumber.setText(String.valueOf(insurance.getPolicyNumber()));
        datumOd.setText(insurance.getDatumOd());
        datumDo.setText(insurance.getDatumDo());
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
