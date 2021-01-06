package com.appnuggets.lensminder.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
                                return true;
                        }
                        return false;
                    }
                });

        MaterialCardView lensesCardView = findViewById(R.id.lensesCard);
        lensesCardView.setOnClickListener(new MaterialCardView.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Lenses selected!");
            }
        });

        MaterialCardView containerCardView = findViewById(R.id.containerCard);
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
        CircularProgressBar progressBar = findViewById(R.id.lensesProgressBar);
        TextView leftDaysCount = findViewById(R.id.lensesDaysCount);
        TextView expDate = findViewById(R.id.lensesExpDateTextView);
        TextView daysUsed = findViewById(R.id.lensesDaysUsedTextView);
        if(null == lenses) {
            updateCardInfoUnavailable(progressBar, leftDaysCount, daysUsed, expDate);
        }
        else {
            updateCardInfoAvailable(progressBar, leftDaysCount, daysUsed, expDate,
                    lenses.expirationDate, lenses.startDate, lenses.useInterval);
        }
    }

    private void updateContainerSummary(Container container) {
        CircularProgressBar progressBar = findViewById(R.id.containerProgressBar);
        TextView leftDaysCount = findViewById(R.id.containerDaysCount);
        TextView expDate = findViewById(R.id.containerExpDateTextView);
        TextView daysUsed = findViewById(R.id.containerDaysUsedTextView);
        if(null == container) {
            updateCardInfoUnavailable(progressBar, leftDaysCount, daysUsed, expDate);
        }
        else {
            updateCardInfoAvailable(progressBar, leftDaysCount, daysUsed, expDate,
                    container.expirationDate, container.startDate, container.useInterval);
        }
    }

    private void updateDropsSummary(Drops drops) {
        CircularProgressBar progressBar = findViewById(R.id.dropsProgressBar);
        TextView leftDaysCount = findViewById(R.id.dropsDaysCount);
        TextView expDate = findViewById(R.id.dropsExpDateTextView);
        TextView daysUsed = findViewById(R.id.dropsDaysUsedTextView);
        if(null == drops) {
            updateCardInfoUnavailable(progressBar, leftDaysCount, daysUsed, expDate);
        }
        else {
            updateCardInfoAvailable(progressBar, leftDaysCount, daysUsed, expDate,
                    drops.expirationDate, drops.startDate, drops.useInterval);
        }
    }

    private void updateSolutionSummary(Solution solution) {
        CircularProgressBar progressBar = findViewById(R.id.solutionProgressBar);
        TextView leftDaysCount = findViewById(R.id.solutionDaysCount);
        TextView expDate = findViewById(R.id.solutionExpDateTextView);
        TextView daysUsed = findViewById(R.id.solutionDaysUsedTextView);
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