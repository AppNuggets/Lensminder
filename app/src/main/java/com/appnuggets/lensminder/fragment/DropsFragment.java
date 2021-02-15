package com.appnuggets.lensminder.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.activity.RefreshInterface;
import com.appnuggets.lensminder.adapter.DropsAdapter;
import com.appnuggets.lensminder.bottomsheet.DropsBottomSheetDialog;
import com.appnuggets.lensminder.database.AppDatabase;
import com.appnuggets.lensminder.database.entity.Drops;
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

public class DropsFragment extends Fragment implements RefreshInterface {

    private TextView dropsLeftDaysCount;
    private CircularProgressBar dropsProgressbar;
    private RecyclerView dropsHistoryRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drops, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton dropsShowAddSheet = view.findViewById(R.id.drops_show_add_sheet);
        MaterialButton deleteCurrentDrops = view.findViewById(R.id.delete_drops_button);
        dropsProgressbar = view.findViewById(R.id.drops_progressbar);
        dropsLeftDaysCount = view.findViewById(R.id.drops_days_count);
        dropsHistoryRecyclerView = view.findViewById(R.id.drops_recycler_view);

        setRecyclerView();
        dropsShowAddSheet.setOnClickListener(v -> {
            DropsBottomSheetDialog dropsBottomSheetDialog =
                    new DropsBottomSheetDialog(this);
            //dropsBottomSheetDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme);
            dropsBottomSheetDialog.show(getChildFragmentManager(), "bottomSheetDrops");
        });

        deleteCurrentDrops.setOnClickListener(v -> {
            AppDatabase db = AppDatabase.getInstance(getContext());
            Drops inUseDrops = db.dropsDao().getInUse();
            if( null != inUseDrops) {
                inUseDrops.inUse = false;
                UsageProcessor usageProcessor = new UsageProcessor();
                Long leftDays = usageProcessor.calculateUsageLeft(inUseDrops.startDate,
                        inUseDrops.expirationDate, inUseDrops.useInterval);
                if( leftDays > 0) {
                    try {
                        Date today = new Date();
                        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd.MM.yyyy",
                                Locale.UK);
                        inUseDrops.endDate = simpleFormat.parse(simpleFormat.format(today));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                db.dropsDao().update(inUseDrops);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                        this.getContext());
                boolean enabledNotification = prefs.getBoolean("notify", false);
                if(enabledNotification) {
                    NotificationService.cancelNotification(getContext(),
                            NotificationCode.DROPS_EXPIRED);
                }
                refreshData();
                Toast.makeText(getContext(), R.string.delete_current_drops_confirmation,
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getContext(), R.string.delete_current_drops_error,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        AppDatabase db = AppDatabase.getInstance(getContext());
        Drops dropsInUse = db.dropsDao().getInUse();
        updateDropsSummary(dropsInUse);
    }

    @Override
    public void refreshData() {
        AppDatabase db = AppDatabase.getInstance(getContext());
        Drops dropsInUse = db.dropsDao().getInUse();
        updateDropsSummary(dropsInUse);

        Context context = getContext();
        DropsAdapter dropsAdapter = new DropsAdapter(context, db.dropsDao().getAllNotInUse());
        dropsHistoryRecyclerView.setAdapter(dropsAdapter);
    }

    private void setRecyclerView() {
        Context context = getContext();
        AppDatabase db = AppDatabase.getInstance(context);
        DropsAdapter dropsAdapter = new DropsAdapter(context, db.dropsDao().getAllNotInUse());

        dropsHistoryRecyclerView.setHasFixedSize(true);
        dropsHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        dropsHistoryRecyclerView.setAdapter(dropsAdapter);
    }

    private void updateDropsSummary(Drops drops) {
        if (null == drops) {
            UpdateDisplayService.updateProgressBar(dropsProgressbar, dropsLeftDaysCount);
        }
        else {
            UpdateDisplayService.updateProgressBar(dropsProgressbar, dropsLeftDaysCount,
                    drops.expirationDate, drops.startDate, drops.useInterval );
        }
    }

}