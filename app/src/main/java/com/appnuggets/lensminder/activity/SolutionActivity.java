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

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.adapter.ContainerAdapter;
import com.appnuggets.lensminder.adapter.SolutionAdapter;
import com.appnuggets.lensminder.bottomsheet.ContainerBottomSheetDialog;
import com.appnuggets.lensminder.bottomsheet.DropsBottomSheetDialog;
import com.appnuggets.lensminder.bottomsheet.SolutionBottomSheetDialog;
import com.appnuggets.lensminder.model.Container;
import com.appnuggets.lensminder.model.Solution;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SolutionActivity extends AppCompatActivity {

    private CircularProgressBar solutionProgressBar;
    private CircularProgressBar containerProgressBar;
    private MaterialButton deleteSolution;
    private MaterialButton deleteContainer;
    private RecyclerView solutionsRecycleView;
    private RecyclerView containersRecycleView;

    SolutionAdapter solutionAdapter;
    ContainerAdapter containerAdapter;

    FloatingActionButton showAddSolutionButton;
    FloatingActionButton showAddContainerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);

        solutionProgressBar = findViewById(R.id.solutionProgressBar);
        solutionProgressBar.setProgressMax(31f);
        solutionProgressBar.setProgressWithAnimation(2f, (long) 1000); // =1s

        containerProgressBar = findViewById(R.id.containerProgressBar);
        containerProgressBar.setProgressMax(31f);
        containerProgressBar.setProgressWithAnimation(2f, (long) 1000); // =1s

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
                        return true;
                    case R.id.Solution:
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
        solutionsRecycleView.setHasFixedSize(true);
        solutionsRecycleView.setLayoutManager(new LinearLayoutManager(this));
        solutionAdapter = new SolutionAdapter(this, getSolutionsList());
        solutionsRecycleView.setAdapter(solutionAdapter);

        containersRecycleView.setHasFixedSize(true);
        containersRecycleView.setLayoutManager(new LinearLayoutManager(this));
        containerAdapter = new ContainerAdapter(this, getContainersList());
        containersRecycleView.setAdapter(containerAdapter);
    }

    private List<Solution> getSolutionsList(){
        List<Solution> list = new ArrayList<>();
        list.add(new Solution(new Date("06/10/2021"), new Date("12/01/2020"),63, "A"));
        list.add(new Solution(new Date("06/10/2021"), new Date("12/01/2020"),63, "B"));
        list.add(new Solution(new Date("06/10/2021"), new Date("12/01/2020"),63, "C"));
        list.add(new Solution(new Date("06/10/2021"), new Date("12/01/2020"),63, "D"));
        list.add(new Solution(new Date("06/10/2021"), new Date("12/01/2020"),63, "E"));
        return list;
    }

    private List<Container> getContainersList(){
        List<Container> list = new ArrayList<>();
        list.add(new Container(new Date("06/10/2021"), new Date("12/01/2020"),63, "F"));
        list.add(new Container(new Date("06/10/2021"), new Date("12/01/2020"),63, "G"));
        list.add(new Container(new Date("06/10/2021"), new Date("12/01/2020"),63, "H"));
        list.add(new Container(new Date("06/10/2021"), new Date("12/01/2020"),63, "I"));
        list.add(new Container(new Date("06/10/2021"), new Date("12/01/2020"),63, "J"));
        return list;
    }
}