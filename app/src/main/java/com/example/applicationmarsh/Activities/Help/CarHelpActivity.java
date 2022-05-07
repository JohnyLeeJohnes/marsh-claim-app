package com.example.applicationmarsh.Activities.Help;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.applicationmarsh.Fragments.Help.CarAccidentHelp1;
import com.example.applicationmarsh.Fragments.Help.CarAccidentHelp2;
import com.example.applicationmarsh.Fragments.Help.CarAccidentHelp3;
import com.example.applicationmarsh.R;
import com.example.applicationmarsh.Utilities.Adapters.PagerAdapter;
import com.example.applicationmarsh.Utilities.Global;
import com.example.applicationmarsh.Utilities.LocaleHelper;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class CarHelpActivity extends AppCompatActivity {

    private ArrayList<Fragment> fragments = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_help);

        //Init items
        initItems();

        //Init pager & adapter
        ViewPager viewPager = findViewById(R.id.help_car_pager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        //Create tablay
        TabLayout tabLayout = findViewById(R.id.help_car_tab);
        tabLayout.setupWithViewPager(viewPager, true);

        // Add your fragments in adapter.
        for (Fragment f: fragments) { adapter.addFragment(f); }

        //Set adapter
        viewPager.setAdapter(adapter);
    }

    //Init list of fragments + toolbar
    private void initItems(){
        //Add fragments
        fragments = new ArrayList<>();
        fragments.add(new CarAccidentHelp1());
        fragments.add(new CarAccidentHelp2());
        fragments.add(new CarAccidentHelp3());

        //Set toolbar - backarrow
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
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
