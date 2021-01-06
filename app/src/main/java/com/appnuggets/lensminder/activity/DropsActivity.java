package com.appnuggets.lensminder.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.adapter.DropsAdapter;
import com.appnuggets.lensminder.bottomsheet.DropsBottomSheetDialog;
import com.appnuggets.lensminder.model.Drops;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DropsActivity extends AppCompatActivity {

    private BottomSheetDialog bottomSheetDialog;
    FloatingActionButton showAddDropsButton;
    private TextInputLayout textInputLayout;
    private AutoCompleteTextView autoCompleteTextView;
    private MaterialButton deleteDrops;
    private CircularProgressBar dropsProgressBar;

    RecyclerView recyclerView;
    DropsAdapter dropsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drops);

        dropsProgressBar = findViewById(R.id.dropsProgressBar);
        dropsProgressBar.setProgressMax(31f);
        dropsProgressBar.setProgressWithAnimation(2f, (long) 1000); // =1s

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.Drops);

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
                        return true;
                    case R.id.Settings:
                        return true;
                }
                return false;
            }
        });

        showAddDropsButton = findViewById(R.id.showAddDrops);

        showAddDropsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DropsBottomSheetDialog dropsBottomSheetDialog = new DropsBottomSheetDialog();
                dropsBottomSheetDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme);
                dropsBottomSheetDialog.show(getSupportFragmentManager(), "bottomSheetDrops");
            }
        });

        deleteDrops = findViewById(R.id.deleteDropsButton);

        deleteDrops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        recyclerView = findViewById(R.id.dropsRecycleView);
        setRecyclerView();
    }

    private void setRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dropsAdapter = new DropsAdapter(this, getList());
        recyclerView.setAdapter(dropsAdapter);
    }

    private List<Drops> getList(){
        List<Drops> list = new ArrayList<>();
        list.add(new Drops(new Date("06/10/2021"), new Date("12/01/2020"),63, "K"));
        list.add(new Drops(new Date("06/10/2021"), new Date("12/01/2020"),63, "B"));
        list.add(new Drops(new Date("06/10/2021"), new Date("12/01/2020"),63, "C"));
        list.add(new Drops(new Date("06/10/2021"), new Date("12/01/2020"),63, "D"));
        list.add(new Drops(new Date("06/10/2021"), new Date("12/01/2020"),63, "E"));
        return list;
    }
}