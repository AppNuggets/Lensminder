package com.appnuggets.lensminder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.adapter.ContainerAdapter;
import com.appnuggets.lensminder.adapter.SolutionAdapter;
import com.appnuggets.lensminder.bottomsheet.ContainerBottomSheetDialog;
import com.appnuggets.lensminder.bottomsheet.SolutionBottomSheetDialog;
import com.appnuggets.lensminder.database.AppDatabase;
import com.appnuggets.lensminder.database.entity.Container;
import com.appnuggets.lensminder.database.entity.Solution;
import com.appnuggets.lensminder.model.UsageProcessor;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class SolutionActivity extends AppCompatActivity {

    private CircularProgressBar solutionProgressBar;
    private CircularProgressBar containerProgressBar;
    private MaterialButton deleteSolution;
    private MaterialButton deleteContainer;
    private RecyclerView solutionsRecycleView;
    private RecyclerView containersRecycleView;

    private SolutionAdapter solutionAdapter;
    private ContainerAdapter containerAdapter;

    private FloatingActionButton showAddSolutionButton;
    private FloatingActionButton showAddContainerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);

        updateSolutionSummary();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.Solution);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.Dashboard:
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Lenses:
                        startActivity(new Intent(getApplicationContext(), LensesActivity.class));
                        overridePendingTransition(0,0);
                    case R.id.Solution:
                        return true;
                    case R.id.Drops:
                        startActivity(new Intent(getApplicationContext(), DropsActivity.class));
                        overridePendingTransition(0,0);
                    case R.id.Settings:
                        return true;
                }
                return false;
            }
        });

        deleteSolution = findViewById(R.id.deleteSolutionButton);
        deleteSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        deleteContainer = findViewById(R.id.deleteContainerButton);
        deleteContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        solutionsRecycleView = findViewById(R.id.solutionsRecycleView);
        containersRecycleView = findViewById(R.id.containersRecycleView);
        setRecyclerViews();

        showAddSolutionButton = findViewById(R.id.showAddSolution);
        showAddSolutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SolutionBottomSheetDialog solutionBottomSheetDialog = new SolutionBottomSheetDialog();
                solutionBottomSheetDialog.show(getSupportFragmentManager(), "bottomSheetSolution");
            }
        });

        showAddContainerButton = findViewById(R.id.showAddContainer);
        showAddContainerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContainerBottomSheetDialog containerBottomSheetDialog = new ContainerBottomSheetDialog();
                containerBottomSheetDialog.show(getSupportFragmentManager(), "bottomSheetContainer");
            }
        });
    }

    private void setRecyclerViews() {
        AppDatabase db = AppDatabase.getInstance(this);

        solutionsRecycleView.setHasFixedSize(true);
        solutionsRecycleView.setLayoutManager(new LinearLayoutManager(this));
        solutionAdapter = new SolutionAdapter(this, db.solutionDao().getAllNotInUse());
        solutionsRecycleView.setAdapter(solutionAdapter);

        containersRecycleView.setHasFixedSize(true);
        containersRecycleView.setLayoutManager(new LinearLayoutManager(this));
        containerAdapter = new ContainerAdapter(this, db.containerDao().getAllNotInUse());
        containersRecycleView.setAdapter(containerAdapter);
    }

    private void updateSolutionSummary() {
        CircularProgressBar solutionProgressBar = findViewById(R.id.solution_card_progressbar);
        TextView leftDays = findViewById(R.id.solutionLeftDays);
        AppDatabase db = AppDatabase.getInstance(this);

        Solution currentSolution = db.solutionDao().getInUse();
        if (null == currentSolution) {
            solutionProgressBar.setProgressMax(100f);
            solutionProgressBar.setProgressWithAnimation(0f, (long) 1000); // =1s
            leftDays.setText("-");
        }
        else {
            UsageProcessor usageProcessor = new UsageProcessor();
            Long daysLeft = usageProcessor.calculateUsageLeft(currentSolution.startDate,
                    currentSolution.expirationDate, currentSolution.useInterval);

            solutionProgressBar.setProgressMax(currentSolution.useInterval);
            solutionProgressBar.setProgressWithAnimation(currentSolution.useInterval - daysLeft,
                    1000L);

            leftDays.setText(daysLeft.toString());
        }

        containerProgressBar = findViewById(R.id.container_card_progressbar);
        leftDays = findViewById(R.id.containerLeftDays);

        Container currentContainer = db.containerDao().getInUse();
        if (null == currentContainer) {
            containerProgressBar.setProgressMax(100f);
            containerProgressBar.setProgressWithAnimation(0f, (long) 1000); // =1s
            leftDays.setText("-");
        }
        else {
            UsageProcessor usageProcessor = new UsageProcessor();
            Long daysLeft = usageProcessor.calculateUsageLeft(currentContainer.startDate,
                    currentContainer.expirationDate, currentContainer.useInterval);

            containerProgressBar.setProgressMax(currentContainer.useInterval);
            containerProgressBar.setProgressWithAnimation(currentContainer.useInterval - daysLeft,
                    1000L);

            leftDays.setText(daysLeft.toString());
        }
    }
}