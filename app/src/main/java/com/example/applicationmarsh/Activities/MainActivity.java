package com.example.applicationmarsh.Activities;

import android.content.Context;
import android.content.DialogInterface;

import com.example.applicationmarsh.Activities.LeftMenu.AboutActivity;
import com.example.applicationmarsh.Activities.LeftMenu.ContactActivity;
import com.example.applicationmarsh.Activities.LeftMenu.ProfileActivity;
import com.example.applicationmarsh.Fragments.BottomMenu.ClaimsFragment;
import com.example.applicationmarsh.Fragments.BottomMenu.DashboardFragment;
import com.example.applicationmarsh.Fragments.BottomMenu.InsurancesFragment;
import com.example.applicationmarsh.Utilities.Global;
import com.example.applicationmarsh.Utilities.LocaleHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.applicationmarsh.R;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private DialogInterface.OnClickListener dialogListener;
    private BottomNavigationView bottomNav;
    private Toolbar leftMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init view items and texts
        initItems();
        //Init menu header values
        initListeners();

        //Left menu - assign listener
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Left menu - create toggle
        ActionBarDrawerToggle menuToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.menu_open, R.string.menu_close);
        drawerLayout.addDrawerListener(menuToggle);
        menuToggle.setDrawerIndicatorEnabled(true);
        menuToggle.syncState();
    }
    
    //Initiate items and texts
    private void initItems() {
        //Get menu view
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View menuView = navigationView.getHeaderView(0);

        //Left menu - set email and name
        TextView menuUsername = menuView.findViewById(R.id.menu_username);
        TextView menuEmail = menuView.findViewById(R.id.menu_email);
        if (Global.loggedUser != null) {
            menuUsername.setText(String.format("%s %s", Global.loggedUser.getFirstname(), Global.loggedUser.getLastname()));
            menuEmail.setText(Global.loggedUser.getEmail());
        }
    }

    //Initiate left menu + dialog listener
    private void initListeners() {

        //Set toolbar visible
        leftMenu = findViewById(R.id.toolbar);
        setSupportActionBar(leftMenu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Add action -> click on toolbar icon
        leftMenu.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });


        //Bottom Menu - Listener
        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.dashboard:
                        selectedFragment = new DashboardFragment();
                        break;
                    case R.id.claims:
                        selectedFragment = new ClaimsFragment();
                        break;
                    case R.id.insurances:
                        selectedFragment = new InsurancesFragment();
                        break;
                }
                //Set selected fragment
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }
                return true;
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit(); //Default fragment - dashboard


        //Log out - Button Listener
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });


        //Dialog listener - log out
        dialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        logOut();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
    }

    //Log out method
    private void logOut() {
        //Delete user data
        Global.loggedUser = null;

        //Redirect to login
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Class activityClass = null;

        switch (menuItem.getItemId()) {
            case R.id.menu_profile:
                activityClass = ProfileActivity.class;
                break;
            case R.id.menu_contact:
                activityClass = ContactActivity.class;
                break;
            case R.id.menu_about:
                activityClass = AboutActivity.class;
                break;
        }

        //Switch activity
        MainActivity.this.startActivity(new Intent(MainActivity.this, activityClass));
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
            builder.setMessage("Do you want to log out?").setPositiveButton("Yes", dialogListener).setNegativeButton("No", dialogListener).show();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.wrap(newBase, Global.languague));
    }
}
