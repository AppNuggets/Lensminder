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
import com.appnuggets.lensminder.bottomsheet.BottomSheetDialogDrops;
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

    RecyclerView recyclerView;
    DropsAdapter dropsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drops);

        MaterialButton deleteDrops = findViewById(R.id.deleteDropsButton);
        showAddDropsButton = findViewById(R.id.showAddDrops);

        CircularProgressBar lensesProgressBar = findViewById(R.id.dropsProgressBar);
        lensesProgressBar.setProgressMax(31f);
        lensesProgressBar.setProgressWithAnimation(2f, (long) 1000); // =1s

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

        showAddDropsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BottomSheetDialogDrops bottomSheetDialogDrops = new BottomSheetDialogDrops();
                bottomSheetDialogDrops.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme);
                bottomSheetDialogDrops.show(getSupportFragmentManager(), "bottomSheetDrops");
            }
        });


        deleteDrops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete this drops
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
        list.add(new Drops(new Date("06/10/2021"), new Date("12/01/2020"),63, "A"));
        list.add(new Drops(new Date("06/10/2021"), new Date("12/01/2020"),63, "B"));
        list.add(new Drops(new Date("06/10/2021"), new Date("12/01/2020"),63, "C"));
        list.add(new Drops(new Date("06/10/2021"), new Date("12/01/2020"),63, "D"));
        list.add(new Drops(new Date("06/10/2021"), new Date("12/01/2020"),63, "E"));
        return list;
    }
}