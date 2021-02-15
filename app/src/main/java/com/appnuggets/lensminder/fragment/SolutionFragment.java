package com.appnuggets.lensminder.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.activity.RefreshInterface;
import com.appnuggets.lensminder.adapter.ContainerAdapter;
import com.appnuggets.lensminder.adapter.SolutionAdapter;
import com.appnuggets.lensminder.bottomsheet.ContainerBottomSheetDialog;
import com.appnuggets.lensminder.bottomsheet.SolutionBottomSheetDialog;
import com.appnuggets.lensminder.database.AppDatabase;
import com.appnuggets.lensminder.database.entity.Container;
import com.appnuggets.lensminder.database.entity.Solution;
import com.appnuggets.lensminder.model.NotificationCode;
import com.appnuggets.lensminder.model.UsageProcessor;
import com.appnuggets.lensminder.service.NotificationService;
import com.appnuggets.lensminder.service.UpdateDisplayService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SolutionFragment extends Fragment implements RefreshInterface {

    private CircularProgressBar solutionProgressbar;
    private TextView solutionLeftDaysCount;
    private CircularProgressBar containerProgressbar;
    private TextView containerLeftDaysCount;

    private RecyclerView solutionHistoryRecyclerView;
    private RecyclerView containerHistoryRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_solution, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialButton deleteCurrentSolution = view.findViewById(R.id.delete_solution_button);
        FloatingActionButton solutionShowAddCurrent = view.findViewById(R.id.showAddSolution);
        solutionProgressbar = view.findViewById(R.id.solution_progressbar);
        solutionLeftDaysCount = view.findViewById(R.id.solution_days_count);
        solutionHistoryRecyclerView = view.findViewById(R.id.solutions_history_recycler_view);

        MaterialButton deleteCurrentContainer = view.findViewById(R.id.delete_container_button);
        FloatingActionButton containerShowAddCurrent = view.findViewById(R.id.showAddContainer);
        containerProgressbar = view.findViewById(R.id.container_progressbar);
        containerLeftDaysCount = view.findViewById(R.id.container_days_count);
        containerHistoryRecyclerView = view.findViewById(R.id.container_history_recycler_view);

        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
        setSolutionHistoryRecyclerView();
        setContainerHistoryRecyclerView();

        deleteCurrentSolution.setOnClickListener(v -> {
            AppDatabase db = AppDatabase.getInstance(getContext());
            Solution inUseSolution = db.solutionDao().getInUse();
            if(null != inUseSolution) {
                inUseSolution.inUse = false;
                UsageProcessor usageProcessor = new UsageProcessor();
                Long leftDays = usageProcessor.calculateUsageLeft(inUseSolution.startDate,
                        inUseSolution.expirationDate, inUseSolution.useInterval);
                if( leftDays > 0) {
                    try {
                        Date today = new Date();
                        inUseSolution.endDate = simpleFormat.parse(simpleFormat.format(today));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                db.solutionDao().update(inUseSolution);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                        this.getContext());
                boolean enabledNotification = prefs.getBoolean("notify", false);
                if(enabledNotification) {
                    NotificationService.cancelNotification(getContext(),
                            NotificationCode.SOLUTION_EXPIRED);
                }

                Toast.makeText(getContext(), R.string.delete_current_solution_confirmation,
                        Toast.LENGTH_SHORT).show();
                refreshData();
            }
            else {
                Toast.makeText(getContext(), R.string.delete_current_solution_error,
                        Toast.LENGTH_SHORT).show();
            }
        });

        solutionShowAddCurrent.setOnClickListener(v -> {
            SolutionBottomSheetDialog solutionBottomSheetDialog =
                    new SolutionBottomSheetDialog(this);
            solutionBottomSheetDialog.show(getChildFragmentManager(), "bottomSheetSolution");
        });

        deleteCurrentContainer.setOnClickListener(v -> {
            AppDatabase db = AppDatabase.getInstance(getContext());
            Container inUseContainer = db.containerDao().getInUse();
            if(null != inUseContainer) {
                inUseContainer.inUse = false;
                UsageProcessor usageProcessor = new UsageProcessor();
                Long leftDays = usageProcessor.calculateUsageLeft(inUseContainer.startDate,
                        null, inUseContainer.useInterval);
                if( leftDays > 0) {
                    try {
                        Date today = new Date();
                        inUseContainer.endDate = simpleFormat.parse(simpleFormat.format(today));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                db.containerDao().update(inUseContainer);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                        this.getContext());
                boolean enabledNotification = prefs.getBoolean("notify", false);
                if(enabledNotification) {
                    NotificationService.cancelNotification(getContext(),
                            NotificationCode.CONTAINER_EXPIRED);
                }

                Toast.makeText(getContext(), R.string.delete_current_container_confirmation,
                        Toast.LENGTH_SHORT).show();
                refreshData();
            }
            else {
                Toast.makeText(getContext(), R.string.delete_current_container_error,
                        Toast.LENGTH_SHORT).show();
            }
        });

        containerShowAddCurrent.setOnClickListener(v -> {
            ContainerBottomSheetDialog containerBottomSheetDialog =
                    new ContainerBottomSheetDialog(this);
            containerBottomSheetDialog.show(getChildFragmentManager(), "bottomSheetContainer");
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        AppDatabase db = AppDatabase.getInstance(getContext());

        Solution solutionInUse = db.solutionDao().getInUse();
        updateSolutionSummary(solutionInUse);

        Container containerInUse = db.containerDao().getInUse();
        updateContainerSummary(containerInUse);
    }

    @Override
    public void refreshData() {
        AppDatabase db = AppDatabase.getInstance(getContext());
        Solution solutionInUse = db.solutionDao().getInUse();
        updateSolutionSummary(solutionInUse);
        Container containerInUse = db.containerDao().getInUse();
        updateContainerSummary(containerInUse);

        Context context = getContext();
        SolutionAdapter solutionAdapter = new SolutionAdapter(context,
                db.solutionDao().getAllNotInUse());
        solutionHistoryRecyclerView.setAdapter(solutionAdapter);

        ContainerAdapter containerAdapter = new ContainerAdapter(context,
                db.containerDao().getAllNotInUse());
        containerHistoryRecyclerView.setAdapter(containerAdapter);
    }

    private void setSolutionHistoryRecyclerView(){
        Context context = getContext();
        AppDatabase db = AppDatabase.getInstance(context);
        SolutionAdapter solutionAdapter = new SolutionAdapter(context,
                db.solutionDao().getAllNotInUse());

        solutionHistoryRecyclerView.setHasFixedSize(true);
        solutionHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        solutionHistoryRecyclerView.setAdapter(solutionAdapter);
    }

    private void setContainerHistoryRecyclerView() {
        Context context = getContext();
        AppDatabase db = AppDatabase.getInstance(context);
        ContainerAdapter containerAdapter = new ContainerAdapter(context,
                db.containerDao().getAllNotInUse());

        containerHistoryRecyclerView.setHasFixedSize(true);
        containerHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        containerHistoryRecyclerView.setAdapter(containerAdapter);
    }

    private void updateSolutionSummary(Solution solution) {
        if(null == solution) {
            UpdateDisplayService.updateProgressBar(solutionProgressbar, solutionLeftDaysCount);
        }
        else {
            UpdateDisplayService.updateProgressBar(solutionProgressbar, solutionLeftDaysCount,
                    solution.expirationDate, solution.startDate, solution.useInterval );
        }
    }

    private void updateContainerSummary(Container container) {
        if(null == container) {
            UpdateDisplayService.updateProgressBar(containerProgressbar, containerLeftDaysCount);
        }
        else {
            UpdateDisplayService.updateProgressBar(containerProgressbar, containerLeftDaysCount,
                    null, container.startDate, container.useInterval );
        }
    }

}