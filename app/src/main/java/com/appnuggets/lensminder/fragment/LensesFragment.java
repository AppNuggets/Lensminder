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
import com.appnuggets.lensminder.adapter.LensesAdapter;
import com.appnuggets.lensminder.adapter.LensesStockAdapter;
import com.appnuggets.lensminder.bottomsheet.LensesBottomSheetDialog;
import com.appnuggets.lensminder.bottomsheet.LensesStockBottomSheetDialog;
import com.appnuggets.lensminder.database.AppDatabase;
import com.appnuggets.lensminder.database.entity.Lenses;
import com.appnuggets.lensminder.database.entity.State;
import com.appnuggets.lensminder.model.NotificationCode;
import com.appnuggets.lensminder.model.UsageProcessor;
import com.appnuggets.lensminder.service.NotificationService;
import com.appnuggets.lensminder.service.UpdateDisplayService;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class LensesFragment extends Fragment implements LensesStockAdapter.OnLensListener,
        RefreshInterface {

    private TextView lensesLeftDaysCount;
    private CircularProgressBar lensesProgressbar;

    private List<Lenses> stockLensesList;
    private RecyclerView lensesStockRecyclerView;
    private RecyclerView lensesHistoryRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lenses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton lensesShowAddCurrent = view.findViewById(R.id.showAddLenses);
        FloatingActionButton lensesShowAddStock = view.findViewById(R.id.showAddLensesToStock);
        MaterialButton deleteCurrentLenses = view.findViewById(R.id.lenses_delete_button);
        lensesProgressbar = view.findViewById(R.id.lenses_progressbar);
        lensesLeftDaysCount = view.findViewById(R.id.lenses_days_count);
        lensesHistoryRecyclerView = view.findViewById(R.id.lenses_history_recycler_view);
        lensesStockRecyclerView = view.findViewById(R.id.lenses_stock_recycler_view);

        // Add new current lenses listener
        lensesShowAddCurrent.setOnClickListener(v -> {
            LensesBottomSheetDialog lensesBottomSheetDialog =
                    new LensesBottomSheetDialog(this);
            lensesBottomSheetDialog.show(getChildFragmentManager(), "bottomSheetLenses");
        });

        // Add lenses to stock listener
        lensesShowAddStock.setOnClickListener(v -> {
            LensesStockBottomSheetDialog lensesStockBottomSheetDialog =
                    new LensesStockBottomSheetDialog(this);
            lensesStockBottomSheetDialog.show(getChildFragmentManager(),
                    "bottomSheetLensesStock");
        });

        // Delete current lenses listener
        deleteCurrentLenses.setOnClickListener(v -> {
            AppDatabase db = AppDatabase.getInstance(getContext(  ));
            Lenses inUseLenses = db.lensesDao().getInUse();
            if(null != inUseLenses) {
                inUseLenses.state = State.IN_HISTORY;
                UsageProcessor usageProcessor = new UsageProcessor();
                Long leftDays = usageProcessor.calculateUsageLeft(inUseLenses.startDate,
                        inUseLenses.expirationDate, inUseLenses.useInterval);
                if( leftDays > 0) {
                    try {
                        Date today = new Date();
                        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd.MM.yyyy",
                                Locale.UK);
                        inUseLenses.endDate = simpleFormat.parse(simpleFormat.format(today));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                db.lensesDao().update(inUseLenses);

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                        this.getContext());
                boolean enabledNotification = prefs.getBoolean("notify", false);
                if(enabledNotification) {
                    NotificationService.cancelNotification(getContext(),
                            NotificationCode.LENSES_EXPIRED);
                }
                Toast.makeText(getContext(), R.string.delete_current_lenses_confirmation,
                        Toast.LENGTH_SHORT).show();
                refreshData();
            }
            else {
                Toast.makeText(getContext(), R.string.delete_current_lenses_error,
                        Toast.LENGTH_SHORT).show();
            }

        });

        Context context = getContext();
        AppDatabase db = AppDatabase.getInstance(context);
        List<Lenses> lensesList = db.lensesDao().getAllNotInUse();
        setLensesHistoryRecyclerView(lensesList);
        stockLensesList = db.lensesDao().getAllInStock();
        setLensesStockRecyclerView(stockLensesList);
    }

    @Override
    public void onStart() {
        super.onStart();

        AppDatabase db = AppDatabase.getInstance(getContext());
        Lenses lensesInUse = db.lensesDao().getInUse();
        updateLensesSummary(lensesInUse);
    }

    @Override
    public void refreshData() {
        AppDatabase db = AppDatabase.getInstance(getContext());
        Lenses lensesInUse = db.lensesDao().getInUse();
        updateLensesSummary(lensesInUse);

        Context context = getContext();
        LensesAdapter lensesAdapter = new LensesAdapter(context, db.lensesDao().getAllNotInUse());
        lensesHistoryRecyclerView.setAdapter(lensesAdapter);

        LensesStockAdapter lensesStockAdapterAdapter = new LensesStockAdapter(context,
                db.lensesDao().getAllInStock(), this);
        lensesStockRecyclerView.setAdapter(lensesStockAdapterAdapter);
    }

    private void setLensesHistoryRecyclerView(List<Lenses> lenses) {
        Context context = getContext();
        LensesAdapter lensesAdapter = new LensesAdapter(context, lenses);
        lensesHistoryRecyclerView.setHasFixedSize(true);
        lensesHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        lensesHistoryRecyclerView.setAdapter(lensesAdapter);
    }

    private void setLensesStockRecyclerView(List<Lenses> lenses) {
        Context context = getContext();
        LensesStockAdapter lensesStockAdapter = new LensesStockAdapter(context, lenses,
                this);
        lensesStockRecyclerView.setHasFixedSize(true);
        lensesStockRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        lensesStockRecyclerView.setAdapter(lensesStockAdapter);
    }

    private void updateLensesSummary(Lenses lenses) {
        if(null == lenses) {
            UpdateDisplayService.updateProgressBar(lensesProgressbar, lensesLeftDaysCount);
        }
        else {
            UpdateDisplayService.updateProgressBar(lensesProgressbar, lensesLeftDaysCount,
                    lenses.expirationDate, lenses.startDate, lenses.useInterval );
        }
    }

    public void onLensClick(int position) {
        MaterialDatePicker<Long> startDatePicker;

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText(R.string.start_date_picker_title);
        builder.setSelection(today);
        startDatePicker = builder.build();
        startDatePicker.show(getParentFragmentManager(), "DATE_PICKER");

        startDatePicker.addOnPositiveButtonClickListener(selection -> {
            AppDatabase db = AppDatabase.getInstance(getContext());
            Lenses inUseLenses = db.lensesDao().getInUse();
            SimpleDateFormat simpleFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
            if(inUseLenses != null)
            {
                inUseLenses.state = State.IN_HISTORY;
                try {
                    Date today1 = new Date();

                    inUseLenses.endDate = simpleFormat.parse(simpleFormat.format(today1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                db.lensesDao().update(inUseLenses);
            }
            stockLensesList = db.lensesDao().getAllInStock();
            Lenses lenses = stockLensesList.get(position);
            lenses.state = State.IN_USE;
            if(startDatePicker.getSelection()!=null){
                Date date = new Date(startDatePicker.getSelection());
                String dateValue = simpleFormat.format(date);
                try {
                    lenses.startDate = simpleFormat.parse(dateValue);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                UsageProcessor usageProcessor = new UsageProcessor();
                lenses.endDate = usageProcessor.calculateEndDate(lenses.startDate,
                        lenses.expirationDate, lenses.useInterval);
                db.lensesDao().update(lenses);
            }

            UsageProcessor usageProcessor = new UsageProcessor();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
            boolean enabledNotification = prefs.getBoolean("notify", false);
            if(enabledNotification) {
                NotificationService.createNotification(getContext(),
                        usageProcessor.calculateUsageLeft(lenses.startDate,
                                lenses.expirationDate, lenses.useInterval),
                        NotificationCode.LENSES_EXPIRED);
            }
            refreshData();
        });
    }
}