package com.appnuggets.lensminder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.database.AppDatabase;
import com.appnuggets.lensminder.database.entity.Container;
import com.appnuggets.lensminder.database.entity.Drops;
import com.appnuggets.lensminder.database.entity.Lenses;
import com.appnuggets.lensminder.database.entity.Solution;
import com.appnuggets.lensminder.database.entity.State;
import com.appnuggets.lensminder.fragment.DashboardFragment;
import com.appnuggets.lensminder.fragment.DropsFragment;
import com.appnuggets.lensminder.fragment.LensesFragment;
import com.appnuggets.lensminder.fragment.SolutionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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
        // Check notification preferences
        boolean enabledNotification = prefs.getBoolean("notify", false);

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
                    switch (item.getItemId()) {
                        case R.id.Dashboard:
                            navigateDashboard();
                            return true;
                        case R.id.Lenses:
                            navigateLenses();
                            return true;
                        case R.id.Solution:
                            navigateSolution();
                            return true;
                        case R.id.Drops:
                            navigateDrops();
                            return true;
                        case R.id.Settings:
                            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                            overridePendingTransition(0,0);
                            return false;   // Do not make settings icon highlighted
                    }
                    return false;
                });

        /*AppDatabase db = AppDatabase.getInstance(this);
        DEBUG_populateDataBase(db);*/
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.BASE) {
            CharSequence name = "LensminderNotificationChannel";
            String description = "Channel for lensminder notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("lensminderNotification", name, importance);
            notificationChannel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @SuppressLint("SimpleDateFormat")
    private void DEBUG_populateDataBase(AppDatabase db) {
        try {
            Lenses lenses1 = new Lenses("lenses", State.IN_USE,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("15.12.2020"),
                    null,
                    31L);

            Lenses lenses2 = new Lenses("lenses", State.IN_HISTORY,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("03.01.2020"),
                    null,
                    31L);

            Lenses lenses3 = new Lenses("lenses", State.IN_HISTORY,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("08.02.2020"),
                    null,
                    31L);

            Lenses lenses4 = new Lenses("lenses", State.IN_STOCK,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("10.03.2020"),
                    null,
                    31L);

            Container container1 = new Container("container", true,
                    new SimpleDateFormat("dd.MM.yyyy").parse("01.01.2021"),
                    null,
                    93L);

            Container container2 = new Container("lenses", false,
                    new SimpleDateFormat("dd.MM.yyyy").parse("11.01.2020"),
                    null,
                    93L);

            Container container3 = new Container("lenses", false,
                    new SimpleDateFormat("dd.MM.yyyy").parse("17.02.2020"),
                    null,
                    93L);

            Container container4 = new Container("lenses", false,
                    new SimpleDateFormat("dd.MM.yyyy").parse("02.03.2020"),
                    null,
                    93L);

            Drops drops1 = new Drops("drops", true,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("24.12.2020"),
                    null,
                    93L);

            Drops drops2 = new Drops("lenses", false,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("12.02.2020"),
                    null,
                    93L);

            Drops drops3 = new Drops("lenses", false,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("17.03.2020"),
                    null,
                    93L);

            Drops drops4 = new Drops("lenses", false,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("07.06.2020"),
                    null,
                    93L);

            Solution solution1 = new Solution("solution", true,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("04.01.2021"),
                    null,
                    93L);

            Solution solution2 = new Solution("lenses", false,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("12.02.2019"),
                    null,
                    93L);

            Solution solution3 = new Solution("lenses", false,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("17.03.2019"),
                    null,
                    93L);

            Solution solution4 = new Solution("lenses", false,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("07.06.2019"),
                    null,
                    93L);

            db.lensesDao().insert(lenses1);
            db.lensesDao().insert(lenses2);
            db.lensesDao().insert(lenses3);
            db.lensesDao().insert(lenses4);

            db.containerDao().insert(container1);
            db.containerDao().insert(container2);
            db.containerDao().insert(container3);
            db.containerDao().insert(container4);

            db.dropsDao().insert(drops1);
            db.dropsDao().insert(drops2);
            db.dropsDao().insert(drops3);
            db.dropsDao().insert(drops4);

            db.solutionDao().insert(solution1);
            db.solutionDao().insert(solution2);
            db.solutionDao().insert(solution3);
            db.solutionDao().insert(solution4);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}