package com.example.vubook;


import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.vubook.Authentication.LoginActivity;
import com.example.vubook.ProfileCR.CRProfileActivity;
import com.example.vubook.ProfileTeacher.TeacherProfileActivity;
import com.example.vubook.Tab_Layout_Files.ViewPagerAdapter;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarTitle;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    private String accountType;

    private static final String PREF_ACCOUNT_TYPE = "pref_account";
    private static final String PREF_DEPARTMENT = "pref_dept";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbar_title);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitle.setText("Home");

        tabLayout = findViewById(R.id.tabLayoutID);
        viewPager = findViewById(R.id.viewPager);

        navigationView = findViewById(R.id.navigation_view_left);
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open, R.string.close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        NavigationItems();

//  Tab Layout

        tabLayout.setupWithViewPager(viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
    }


    public void NavigationItems() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.profile:
//                        Intent teacher = new Intent(MainActivity.this, TeacherProfileActivity.class);
//                        startActivity(teacher);

                        openProfile();

                        break;
//                    case R.id.cr:
//                        Intent cr = new Intent(MainActivity.this, CRProfileActivity.class);
//                        startActivity(cr);
//                        break;
                    case R.id.settings:
                        Intent settings = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(settings);
                        break;
                    case R.id.feedback:

                        Intent feedback = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "ahamed_lincon@outlook.com"));

                        try {
                            startActivity(Intent.createChooser(feedback, "Choose an e-mail client"));
                            finish();
                        } catch (android.content.ActivityNotFoundException e) {
                            Toast.makeText(MainActivity.this, "There is no e-mail clint installed!", Toast.LENGTH_SHORT).show();

                        }
//                        Intent feedback = new Intent(HomeActivity.this,FeedbackActivity.class);
//                        startActivity(feedback);
                        break;
                    case R.id.about:
                        break;
                    case R.id.logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent signOut = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(signOut);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    private void openProfile() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        accountType = sharedPreferences.getString(PREF_ACCOUNT_TYPE, "");

        switch (accountType) {
            case "Teacher":
                Intent teacher = new Intent(MainActivity.this, TeacherProfileActivity.class);
                startActivity(teacher);
                break;
            case "CR":
                Intent cr = new Intent(MainActivity.this, CRProfileActivity.class);
                startActivity(cr);
                break;
            case "Student":
                break;
            case "Office staff":
                break;
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 1000);
        }
    }

}