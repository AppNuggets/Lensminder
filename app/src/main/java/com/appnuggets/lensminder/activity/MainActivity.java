package com.appnuggets.lensminder.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.database.AppDatabase;
import com.appnuggets.lensminder.database.entity.Container;
import com.appnuggets.lensminder.database.entity.Drops;
import com.appnuggets.lensminder.database.entity.Lenses;
import com.appnuggets.lensminder.database.entity.Solution;
import com.appnuggets.lensminder.fragment.DashboardFragment;
import com.appnuggets.lensminder.fragment.DropsFragment;
import com.appnuggets.lensminder.fragment.LensesFragment;
import com.appnuggets.lensminder.fragment.SolutionFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    private Fragment dashboardFragment;
    private Fragment lensesFragment;
    private Fragment solutionFragment;
    private Fragment dropsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dashboardFragment = new DashboardFragment();
        lensesFragment = new LensesFragment();
        solutionFragment = new SolutionFragment();
        dropsFragment = new DropsFragment();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container_view, dashboardFragment);
        ft.commit();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
                    switch (item.getItemId()) {
                        case R.id.Dashboard:
                            navigateToFragmentDashboard();
                            return true;
                        case R.id.Lenses:
                            navigateToFragmentLenses();
                            return true;
                        case R.id.Solution:
                            navigateToFragmentSolution();
                            return true;
                        case R.id.Drops:
                            navigateToFragmentDrops();
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

    private void navigateToFragmentDashboard() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, dashboardFragment);
        fragmentTransaction.commit();
    }

    private void navigateToFragmentLenses() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, lensesFragment);
        fragmentTransaction.commit();
    }

    private void navigateToFragmentSolution() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, solutionFragment);
        fragmentTransaction.commit();
    }

    private void navigateToFragmentDrops() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_view, dropsFragment);
        fragmentTransaction.commit();
    }

    private void DEBUG_populateDataBase(AppDatabase db) {
        try {
            Lenses lenses1 = new Lenses("lenses", true,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("15.12.2020"),
                    31L);

            Lenses lenses2 = new Lenses("lenses", false,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("03.01.2020"),
                    31L);

            Lenses lenses3 = new Lenses("lenses", false,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("08.02.2020"),
                    31L);

            Lenses lenses4 = new Lenses("lenses", false,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("10.03.2020"),
                    31L);

            Container container1 = new Container("container", true,
                    new SimpleDateFormat("dd.MM.yyyy").parse("29.01.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("01.01.2021"),
                    93L);

            Container container2 = new Container("lenses", false,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("11.01.2020"),
                    93L);

            Container container3 = new Container("lenses", false,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("17.02.2020"),
                    93L);

            Container container4 = new Container("lenses", false,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("02.03.2020"),
                    93L);

            Drops drops1 = new Drops("drops", true,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("24.12.2020"),
                    93L);

            Drops drops2 = new Drops("lenses", false,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("12.02.2020"),
                    93L);

            Drops drops3 = new Drops("lenses", false,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("17.03.2020"),
                    93L);

            Drops drops4 = new Drops("lenses", false,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("07.06.2020"),
                    93L);

            Solution solution1 = new Solution("solution", true,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("04.01.2021"),
                    93L);

            Solution solution2 = new Solution("lenses", false,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("12.02.2019"),
                    93L);

            Solution solution3 = new Solution("lenses", false,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("17.03.2019"),
                    93L);

            Solution solution4 = new Solution("lenses", false,
                    new SimpleDateFormat("dd.MM.yyyy").parse("06.08.2021"),
                    new SimpleDateFormat("dd.MM.yyyy").parse("07.06.2019"),
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