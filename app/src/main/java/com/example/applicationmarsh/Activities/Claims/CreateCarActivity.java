package com.example.applicationmarsh.Activities.Claims;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;

import com.example.applicationmarsh.Fragments.Claim.CarRegisterStep1;
import com.example.applicationmarsh.Fragments.Claim.CarRegisterStep2;
import com.example.applicationmarsh.Fragments.Claim.CarRegisterStep3;
import com.example.applicationmarsh.R;
import com.example.applicationmarsh.Utilities.Adapters.PagerAdapter;
import com.example.applicationmarsh.Utilities.Global;
import com.example.applicationmarsh.Utilities.LocaleHelper;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

public class CreateCarActivity extends AppCompatActivity {

    private ArrayList<Fragment> fragments = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_car);

        //Init fragments for Pager
        initFragments();

        //Init pager & adapter
        ViewPager viewPager = findViewById(R.id.create_car_pager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        //Create tablay
        TabLayout tabLayout = findViewById(R.id.create_car_tab);
        tabLayout.setupWithViewPager(viewPager, true);

        // Add your fragments in adapter.
        for (Fragment f : fragments) {
            adapter.addFragment(f);
        }

        //Set adapter
        viewPager.setAdapter(adapter);
    }

    //Init list of fragments
    private void initFragments() {
        fragments = new ArrayList<>();
        fragments.add(new CarRegisterStep1());
        fragments.add(new CarRegisterStep2());
        fragments.add(new CarRegisterStep3());
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
