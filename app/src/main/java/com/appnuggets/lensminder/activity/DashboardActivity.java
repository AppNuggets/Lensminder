package com.appnuggets.lensminder.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.database.AppDatabase;
import com.appnuggets.lensminder.database.entity.Container;
import com.appnuggets.lensminder.database.entity.Drops;
import com.appnuggets.lensminder.database.entity.Lenses;
import com.appnuggets.lensminder.database.entity.Solution;
import com.appnuggets.lensminder.model.DateProcessor;
import com.appnuggets.lensminder.model.UsageProcessor;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean darkModeEnabled = prefs.getBoolean("dark_mode", false);
        if( true ==  darkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.Dashboard);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.Dashboard:
                                return true;
                            case R.id.Lenses:
                                startActivity(new Intent(getApplicationContext(), LensesActivity.class));
                                overridePendingTransition(0,0);
                                return true;
                            case R.id.Solution:
                                startActivity(new Intent(getApplicationContext(), SolutionActivity.class));
                                overridePendingTransition(0,0);
                                return true;
                            case R.id.Drops:
                                startActivity(new Intent(getApplicationContext(), DropsActivity.class));
                                overridePendingTransition(0,0);
                                return true;
                            case R.id.Settings:
                                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                                overridePendingTransition(0,0);
                        }
                        return false;
                    }
                });

        MaterialCardView lensesCardView = findViewById(R.id.card_lenses);
        lensesCardView.setOnClickListener(new MaterialCardView.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Lenses selected!");
            }
        });

        MaterialCardView containerCardView = findViewById(R.id.card_container);
        containerCardView.setOnClickListener(new MaterialCardView.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Container selected!");
            }
        });

        MaterialCardView dropsCardView = findViewById(R.id.dropsCard);
        dropsCardView.setOnClickListener(new MaterialCardView.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Drops selected!");
                startActivity(new Intent(getApplicationContext(), DropsActivity.class));
                overridePendingTransition(0,0);
            }
        });

        MaterialCardView solutionCardView = findViewById(R.id.solutionCard);
        solutionCardView.setOnClickListener(new MaterialCardView.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Solution selected!");
                startActivity(new Intent(getApplicationContext(), SolutionActivity.class));
                overridePendingTransition(0,0);
            }
        });
    }

    @Override
    protected void onResume() {

        super.onResume();

        AppDatabase db = AppDatabase.getInstance(this);

        //DEBUG_populateDataBase(db);

        Lenses lensesInUse = db.lensesDao().getInUse();
        updateLensesSummary(lensesInUse);

        Container containerInUse = db.containerDao().getInUse();
        updateContainerSummary(containerInUse);

        Drops dropsInUse = db.dropsDao().getInUse();
        updateDropsSummary(dropsInUse);

        Solution solutionsInUse = db.solutionDao().getInUse();
        updateSolutionSummary(solutionsInUse);
    }

    private void updateLensesSummary(Lenses lenses) {
        CircularProgressBar progressBar = findViewById(R.id.lenses_card_progressbar);
        TextView leftDaysCount = findViewById(R.id.lenses_card_day_count);
        TextView expDate = findViewById(R.id.lenses_card_expiration_date);
        TextView daysUsed = findViewById(R.id.lenses_card_day_usage);
        if(null == lenses) {
            updateCardInfoUnavailable(progressBar, leftDaysCount, daysUsed, expDate);
        }
        else {
            updateCardInfoAvailable(progressBar, leftDaysCount, daysUsed, expDate,
                    lenses.expirationDate, lenses.startDate, lenses.useInterval);
        }
    }

    private void updateContainerSummary(Container container) {
        CircularProgressBar progressBar = findViewById(R.id.container_card_progressbar);
        TextView leftDaysCount = findViewById(R.id.container_card_day_count);
        TextView expDate = findViewById(R.id.container_card_expiration_date);
        TextView daysUsed = findViewById(R.id.container_card_day_usage);
        if(null == container) {
            updateCardInfoUnavailable(progressBar, leftDaysCount, daysUsed, expDate);
        }
        else {
            updateCardInfoAvailable(progressBar, leftDaysCount, daysUsed, expDate,
                    container.expirationDate, container.startDate, container.useInterval);
        }
    }

    private void updateDropsSummary(Drops drops) {
        CircularProgressBar progressBar = findViewById(R.id.drops_card_progressbar);
        TextView leftDaysCount = findViewById(R.id.drops_card_day_count);
        TextView expDate = findViewById(R.id.drops_card_expiration_date);
        TextView daysUsed = findViewById(R.id.drops_card_day_usage);
        if(null == drops) {
            updateCardInfoUnavailable(progressBar, leftDaysCount, daysUsed, expDate);
        }
        else {
            updateCardInfoAvailable(progressBar, leftDaysCount, daysUsed, expDate,
                    drops.expirationDate, drops.startDate, drops.useInterval);
        }
    }

    private void updateSolutionSummary(Solution solution) {
        CircularProgressBar progressBar = findViewById(R.id.solution_card_progressbar);
        TextView leftDaysCount = findViewById(R.id.solution_card_day_count);
        TextView expDate = findViewById(R.id.solution_card_expiration_date);
        TextView daysUsed = findViewById(R.id.solution_card_day_usage);
        if(null == solution) {
            updateCardInfoUnavailable(progressBar, leftDaysCount, daysUsed, expDate);
        }
        else {
            updateCardInfoAvailable(progressBar, leftDaysCount, daysUsed, expDate,
                    solution.expirationDate, solution.startDate, solution.useInterval);
        }
    }

    private void updateCardInfoUnavailable(CircularProgressBar progressBar, TextView leftDaysView,
                                           TextView daysUsedView, TextView expDateView) {
        progressBar.setProgressMax(100f);
        progressBar.setProgressWithAnimation(0f,1000L);
        daysUsedView.setText("-");
        expDateView.setText("-");
        leftDaysView.setText("-");
    }

    private void updateCardInfoAvailable(CircularProgressBar progressBar, TextView leftDaysView,
                                         TextView daysUsedView, TextView expDateView,
                                         Date expDate, Date startDate, Long useInterval) {
        DateProcessor dateProcessor = new DateProcessor();
        UsageProcessor usageProcessor = new UsageProcessor();

        Long leftDays = usageProcessor.calculateUsageLeft(startDate, expDate, useInterval);
        Long daysUsed = usageProcessor.calculateCurrentUsage(startDate);

        leftDaysView.setText(leftDays.toString());
        daysUsedView.setText(daysUsed.toString());
        expDateView.setText(dateProcessor.dateToString(expDate));

        progressBar.setProgressMax(useInterval);
        progressBar.setProgressWithAnimation(useInterval - leftDays, 1000L);

        if( leftDays <= 0) {
            // Set progressbar red
        }
        else {
            // Set progress bar default color
        }
    }
}