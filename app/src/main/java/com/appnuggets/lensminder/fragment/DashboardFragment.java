package com.appnuggets.lensminder.fragment;

import android.app.AlertDialog;
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

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.activity.MainActivity;
import com.appnuggets.lensminder.activity.NavigationInterface;
import com.appnuggets.lensminder.database.AppDatabase;
import com.appnuggets.lensminder.database.entity.Container;
import com.appnuggets.lensminder.database.entity.Drops;
import com.appnuggets.lensminder.database.entity.Lenses;
import com.appnuggets.lensminder.database.entity.Solution;
import com.appnuggets.lensminder.database.entity.State;
import com.appnuggets.lensminder.model.NotificationCode;
import com.appnuggets.lensminder.model.UsageProcessor;
import com.appnuggets.lensminder.service.NotificationService;
import com.appnuggets.lensminder.service.UpdateDisplayService;
import com.google.android.material.card.MaterialCardView;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private NavigationInterface navigationInterface;

    private CircularProgressBar lensesProgressbar;
    private TextView lensesLeftDaysCount;
    private TextView lensesExpirationDate;
    private TextView lensesDaysUsedCount;

    private CircularProgressBar containerProgressbar;
    private TextView containerLeftDaysCount;
    private TextView containerDaysUsedCount;

    private CircularProgressBar dropsProgressbar;
    private TextView dropsLeftDaysCount;
    private TextView dropsExpirationDate;
    private TextView dropsDaysUsedCount;

    private CircularProgressBar solutionProgressbar;
    private TextView solutionLeftDaysCount;
    private TextView solutionExpirationDate;
    private TextView solutionDaysUsedCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navigationInterface = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialCardView lensesCardView = view.findViewById(R.id.card_lenses);
        lensesProgressbar = view.findViewById(R.id.lenses_card_progressbar);
        lensesLeftDaysCount = view.findViewById(R.id.lenses_card_day_count);
        lensesExpirationDate = view.findViewById(R.id.lenses_card_expiration_date);
        lensesDaysUsedCount = view.findViewById(R.id.lenses_card_day_usage);

        MaterialCardView containerCardView = view.findViewById(R.id.card_container);
        containerProgressbar = view.findViewById(R.id.container_card_progressbar);
        containerLeftDaysCount = view.findViewById(R.id.container_card_day_count);
        containerDaysUsedCount = view.findViewById(R.id.container_card_day_usage);

        MaterialCardView dropsCardView = view.findViewById(R.id.dropsCard);
        dropsProgressbar = view.findViewById(R.id.drops_card_progressbar);
        dropsLeftDaysCount = view.findViewById(R.id.drops_card_day_count);
        dropsExpirationDate = view.findViewById(R.id.drops_card_expiration_date);
        dropsDaysUsedCount = view.findViewById(R.id.drops_card_day_usage);


        MaterialCardView solutionCardView = view.findViewById(R.id.solutionCard);
        solutionProgressbar = view.findViewById(R.id.solution_card_progressbar);
        solutionLeftDaysCount = view.findViewById(R.id.solution_card_day_count);
        solutionExpirationDate = view.findViewById(R.id.solution_card_expiration_date);
        solutionDaysUsedCount = view.findViewById(R.id.solution_card_day_usage);


        lensesCardView.setOnClickListener(v -> navigationInterface.navigateToFragmentLenses());

        lensesCardView.setOnLongClickListener(v -> {
            AppDatabase db = AppDatabase.getInstance(getContext());
            Lenses inUseLenses = db.lensesDao().getInUse();
            if(  null != inUseLenses) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.delete_current_lenses_title)
                        .setMessage(R.string.delete_current_lenses_message)
                        .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {

                            UsageProcessor usageProcessor = new UsageProcessor();
                            Long leftDays = usageProcessor.calculateUsageLeft(inUseLenses.startDate,
                                    inUseLenses.expirationDate, inUseLenses.useInterval);
                            if( leftDays > 0) {
                                try {
                                    Date today = new Date();
                                    SimpleDateFormat simpleFormat = new SimpleDateFormat(
                                            "dd.MM.yyyy", Locale.UK);
                                    inUseLenses.endDate = simpleFormat.parse(
                                            simpleFormat.format(today));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            inUseLenses.state = State.IN_HISTORY;
                            db.lensesDao().update(inUseLenses);

                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                                    getContext());
                            boolean enabledNotification = prefs.getBoolean("notify",
                                    false);
                            if(enabledNotification) {
                                NotificationService.cancelNotification(getContext(),
                                        NotificationCode.LENSES_EXPIRED);
                            }

                            updateLensesSummary(null);
                            Toast.makeText(getContext(), R.string.delete_current_lenses_confirmation,
                                    Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
            else {
                Toast.makeText(getContext(), R.string.delete_current_lenses_error,
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        });


        containerCardView.setOnClickListener(v -> navigationInterface.navigateToFragmentSolution());

        containerCardView.setOnLongClickListener(v -> {
            AppDatabase db = AppDatabase.getInstance(getContext());
            Container inUseContainer = db.containerDao().getInUse();
            if(null != inUseContainer) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.delete_current_container_title)
                        .setMessage(R.string.delete_current_container_message)
                        .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                            UsageProcessor usageProcessor = new UsageProcessor();
                            Long leftDays = usageProcessor.calculateUsageLeft(
                                    inUseContainer.startDate,
                                    null, inUseContainer.useInterval);
                            if( leftDays > 0) {
                                try {
                                    Date today = new Date();
                                    SimpleDateFormat simpleFormat = new SimpleDateFormat(
                                            "dd.MM.yyyy", Locale.UK);
                                    inUseContainer.endDate = simpleFormat.parse(
                                            simpleFormat.format(today));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            inUseContainer.inUse = false;
                            db.containerDao().update(inUseContainer);

                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                                    getContext());
                            boolean enabledNotification = prefs.getBoolean("notify",
                                    false);
                            if(enabledNotification) {
                                NotificationService.cancelNotification(getContext(),
                                        NotificationCode.CONTAINER_EXPIRED);
                            }
                            updateContainerSummary(null);
                            Toast.makeText(getContext(), R.string.delete_current_container_confirmation,
                                    Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
            else {
                Toast.makeText(getContext(), R.string.delete_current_container_error,
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        });


        dropsCardView.setOnClickListener(v -> navigationInterface.navigateToFragmentDrops());

        dropsCardView.setOnLongClickListener(v -> {
            AppDatabase db = AppDatabase.getInstance(getContext());
            Drops inUseDrops = db.dropsDao().getInUse();
            if( null !=  inUseDrops) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.delete_current_drops_title)
                        .setMessage(R.string.delete_current_drops_message)
                        .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                            UsageProcessor usageProcessor = new UsageProcessor();
                            Long leftDays = usageProcessor.calculateUsageLeft(inUseDrops.startDate,
                                    inUseDrops.expirationDate, inUseDrops.useInterval);
                            if( leftDays > 0) {
                                try {
                                    Date today = new Date();
                                    SimpleDateFormat simpleFormat = new SimpleDateFormat(
                                            "dd.MM.yyyy", Locale.UK);
                                    inUseDrops.endDate = simpleFormat.parse(
                                            simpleFormat.format(today));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            inUseDrops.inUse = false;
                            db.dropsDao().update(inUseDrops);

                            SharedPreferences prefs =
                                    PreferenceManager.getDefaultSharedPreferences(getContext());
                            boolean enabledNotification = prefs.getBoolean("notify",
                                    false);
                            if(enabledNotification) {
                                NotificationService.cancelNotification(getContext(),
                                        NotificationCode.DROPS_EXPIRED);
                            }
                            updateDropsSummary(null);
                            Toast.makeText(getContext(), R.string.delete_current_drops_confirmation,
                                    Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
            else {
                Toast.makeText(getContext(), R.string.delete_current_drops_error,
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        });


        solutionCardView.setOnClickListener(v -> navigationInterface.navigateToFragmentSolution());

        solutionCardView.setOnLongClickListener(v -> {
            AppDatabase db = AppDatabase.getInstance(getContext());
            Solution inUseSolution = db.solutionDao().getInUse();
            if(null != inUseSolution) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.delete_current_solution_title)
                        .setMessage(R.string.delete_current_solution_message)
                        .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {

                            UsageProcessor usageProcessor = new UsageProcessor();
                            Long leftDays = usageProcessor.calculateUsageLeft(
                                    inUseSolution.startDate, inUseSolution.expirationDate,
                                    inUseSolution.useInterval);
                            if( leftDays > 0) {
                                try {
                                    Date today = new Date();
                                    SimpleDateFormat simpleFormat = new SimpleDateFormat(
                                            "dd.MM.yyyy", Locale.UK);
                                    inUseSolution.endDate = simpleFormat.parse(
                                            simpleFormat.format(today));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            inUseSolution.inUse = false;
                            db.solutionDao().update(inUseSolution);

                            SharedPreferences prefs =
                                    PreferenceManager.getDefaultSharedPreferences(getContext());
                            boolean enabledNotification = prefs.getBoolean("notify",
                                    false);
                            if(enabledNotification) {
                                NotificationService.cancelNotification(getContext(),
                                        NotificationCode.SOLUTION_EXPIRED);
                            }
                            updateSolutionSummary(null);
                            Toast.makeText(getContext(),
                                    R.string.delete_current_solution_confirmation,
                                    Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }
            else {
                Toast.makeText(getContext(), R.string.delete_current_solution_error,
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        AppDatabase db = AppDatabase.getInstance( getContext() );

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
        if(null == lenses) {
            UpdateDisplayService.updateProgressBar(lensesProgressbar, lensesLeftDaysCount);
            UpdateDisplayService.updateDaysUsed(lensesDaysUsedCount);
            UpdateDisplayService.updateExpirationDate(lensesExpirationDate);
        }
        else {
            UpdateDisplayService.updateProgressBar(lensesProgressbar, lensesLeftDaysCount,
                    lenses.expirationDate, lenses.startDate, lenses.useInterval );
            UpdateDisplayService.updateDaysUsed(lensesDaysUsedCount, lenses.startDate);
            UpdateDisplayService.updateExpirationDate(lensesExpirationDate, lenses.expirationDate);
        }
    }

    private void updateContainerSummary(Container container) {
        if(null == container) {
            UpdateDisplayService.updateProgressBar(containerProgressbar, containerLeftDaysCount);
            UpdateDisplayService.updateDaysUsed(containerDaysUsedCount);
        }
        else {
            UpdateDisplayService.updateProgressBar(containerProgressbar, containerLeftDaysCount,
                    null, container.startDate, container.useInterval );
            UpdateDisplayService.updateDaysUsed(containerDaysUsedCount, container.startDate);
        }
    }

    private void updateDropsSummary(Drops drops) {
        if(null == drops) {
            UpdateDisplayService.updateProgressBar(dropsProgressbar, dropsLeftDaysCount);
            UpdateDisplayService.updateDaysUsed(dropsDaysUsedCount);
            UpdateDisplayService.updateExpirationDate(dropsExpirationDate);
        }
        else {
            UpdateDisplayService.updateProgressBar(dropsProgressbar, dropsLeftDaysCount,
                    drops.expirationDate, drops.startDate, drops.useInterval );
            UpdateDisplayService.updateDaysUsed(dropsDaysUsedCount, drops.startDate);
            UpdateDisplayService.updateExpirationDate(dropsExpirationDate, drops.expirationDate);
        }
    }

    private void updateSolutionSummary(Solution solution) {
        if(null == solution) {
            UpdateDisplayService.updateProgressBar(solutionProgressbar, solutionLeftDaysCount);
            UpdateDisplayService.updateDaysUsed(solutionDaysUsedCount);
            UpdateDisplayService.updateExpirationDate(solutionExpirationDate);
        }
        else {
            UpdateDisplayService.updateProgressBar(solutionProgressbar, solutionLeftDaysCount,
                    solution.expirationDate, solution.startDate, solution.useInterval );
            UpdateDisplayService.updateDaysUsed(solutionDaysUsedCount, solution.startDate);
            UpdateDisplayService.updateExpirationDate(solutionExpirationDate,
                    solution.expirationDate);
        }
    }
}