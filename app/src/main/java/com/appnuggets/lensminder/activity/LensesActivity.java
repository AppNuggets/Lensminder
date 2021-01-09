package com.appnuggets.lensminder.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.adapter.LensesAdapter;
import com.appnuggets.lensminder.bottomsheet.LensesBottomSheetDialog;
import com.appnuggets.lensminder.bottomsheet.LensesStockBottomSheetDialog;
import com.appnuggets.lensminder.database.AppDatabase;
import com.appnuggets.lensminder.database.entity.Lenses;
import com.appnuggets.lensminder.model.UsageProcessor;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class LensesActivity extends AppCompatActivity {

    private CircularProgressBar lensesProgressBar;
    private MaterialButton deleteLenses;
    private RecyclerView lensesRecycleView;
    private RecyclerView lensesStockRecycleView;
    private LensesAdapter lensesAdapter;
    private FloatingActionButton showAddLensesToStockButton;
    private FloatingActionButton showAddLensesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lenses);

        deleteLenses = findViewById(R.id.lensesDeleteButton);
        showAddLensesButton = findViewById(R.id.showAddLenses);

        updateLensesSummary();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.Lenses);

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

        showAddLensesButton = (FloatingActionButton) findViewById(R.id.showAddLenses);
        showAddLensesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LensesBottomSheetDialog lensesBottomSheetDialog = new LensesBottomSheetDialog();
                //lensesBottomSheetDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme);
                lensesBottomSheetDialog.show(getSupportFragmentManager(), "bottomSheetLenses");
            }
        });

        showAddLensesToStockButton = (FloatingActionButton) findViewById(R.id.showAddLensesToStock);
        showAddLensesToStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LensesStockBottomSheetDialog lensesStockBottomSheetDialog = new LensesStockBottomSheetDialog();
                //lensesStockBottomSheetDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme);
                lensesStockBottomSheetDialog.show(getSupportFragmentManager(), "bottomSheetLensesStock");
            }
        });

        deleteLenses = findViewById(R.id.lensesDeleteButton);
        deleteLenses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        lensesRecycleView = findViewById(R.id.lensesRecycleView);
        lensesStockRecycleView = findViewById(R.id.stockRecycleView);
        setRecyclerView();
    }

    private void setRecyclerView() {
        AppDatabase db = AppDatabase.getInstance(this);
        lensesAdapter = new LensesAdapter(this, db.lensesDao().getAllNotInUse());
        lensesRecycleView.setHasFixedSize(true);
        lensesRecycleView.setLayoutManager(new LinearLayoutManager(this));
        lensesRecycleView.setAdapter(lensesAdapter);

        //TODO add lenses stock
    }

    private void updateLensesSummary() {
        CircularProgressBar progressBar = findViewById(R.id.lenses_card_progressbar);
        TextView leftDays = findViewById(R.id.lensesLeftDays);
        AppDatabase db = AppDatabase.getInstance(this);

        Lenses currentLenses = db.lensesDao().getInUse();
        if (null == currentLenses) {
            progressBar.setProgressMax(100f);
            progressBar.setProgressWithAnimation(0f, (long) 1000); // =1s
            leftDays.setText("-");
        }
        else {
            UsageProcessor usageProcessor = new UsageProcessor();
            Long daysLeft = usageProcessor.calculateUsageLeft(currentLenses.startDate,
                    currentLenses.expirationDate, currentLenses.useInterval);

            progressBar.setProgressMax(currentLenses.useInterval);
            progressBar.setProgressWithAnimation(currentLenses.useInterval - daysLeft,
                    1000L);

            leftDays.setText(daysLeft.toString());
        }
    }
}