package com.appnuggets.lensminder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.fragment.DashboardFragment;
import com.appnuggets.lensminder.fragment.DropsFragment;
import com.appnuggets.lensminder.fragment.LensesFragment;
import com.appnuggets.lensminder.fragment.SolutionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements NavigationInterface {

    private BottomNavigationView bottomNavigationView;

    private Fragment dashboardFragment;
    private Fragment lensesFragment;
    private Fragment solutionFragment;
    private Fragment dropsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean darkModeEnabled = prefs.getBoolean("dark_mode", false);
        if(darkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        dashboardFragment = new DashboardFragment();
        lensesFragment = new LensesFragment();
        solutionFragment = new SolutionFragment();
        dropsFragment = new DropsFragment();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container_view, dashboardFragment);
        ft.commit();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.Dashboard) {
                navigateDashboard();
                return true;
            } else if (itemId == R.id.Lenses) {
                navigateLenses();
                return true;
            } else if (itemId == R.id.Solution) {
                navigateSolution();
                return true;
            } else if (itemId == R.id.Drops) {
                navigateDrops();
                return true;
            } else if (itemId == R.id.Settings) {
                startActivity(new Intent(getApplicationContext(),
                        SettingsActivity.class));
                overridePendingTransition(0, 0);
                return false;   // Do not make settings icon highlighted
            }
            return false;
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        bottomNavigationView.setSelectedItemId(R.id.Dashboard);
    }

    public void navigateToFragmentLenses() {
        bottomNavigationView.setSelectedItemId(R.id.Lenses);
    }

    public void navigateToFragmentSolution() {
        bottomNavigationView.setSelectedItemId(R.id.Solution);
    }

    public void navigateToFragmentDrops() {
        bottomNavigationView.setSelectedItemId(R.id.Drops);
    }

    private void navigateDashboard() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, dashboardFragment);
        fragmentTransaction.commit();
    }

    private void navigateLenses() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, lensesFragment);
        fragmentTransaction.commit();
    }

    private void navigateSolution() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, solutionFragment);
        fragmentTransaction.commit();
    }

    private void navigateDrops() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, dropsFragment);
        fragmentTransaction.commit();
    }

    private void createNotificationChannel() {
        CharSequence name = "LensminderNotificationChannel";
        String description = "Channel for lensminder notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel notificationChannel = new NotificationChannel(
                getString(R.string.notification_channel_id), name, importance);
        notificationChannel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(notificationChannel);
    }
}