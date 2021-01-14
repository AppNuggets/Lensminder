package com.appnuggets.lensminder.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.adapter.LensesAdapter;
import com.appnuggets.lensminder.adapter.LensesStockAdapter;
import com.appnuggets.lensminder.bottomsheet.LensesBottomSheetDialog;
import com.appnuggets.lensminder.bottomsheet.LensesStockBottomSheetDialog;
import com.appnuggets.lensminder.database.AppDatabase;
import com.appnuggets.lensminder.database.entity.Lenses;
import com.appnuggets.lensminder.database.entity.State;
import com.appnuggets.lensminder.model.UsageProcessor;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

public class LensesFragment extends Fragment implements LensesStockAdapter.OnLensListener {

    private CircularProgressBar lensesProgressbar;
    private TextView lensesLeftDaysCount;

    private RecyclerView lensesHistoryRecyclerView;
    private RecyclerView lensesStockRecyclerView;

    private List<Lenses> lensesList;
    private List<Lenses> stockLensesList;

    public LensesFragment() {
        // Required empty public constructor
        System.out.println("LensesFragment constructor called!");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
            LensesBottomSheetDialog lensesBottomSheetDialog = new LensesBottomSheetDialog();
            //lensesBottomSheetDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme);
            lensesBottomSheetDialog.show(getChildFragmentManager(), "bottomSheetLenses");
        });

        // Add lenses to stock listener
        lensesShowAddStock.setOnClickListener(v -> {
            LensesStockBottomSheetDialog lensesStockBottomSheetDialog = new LensesStockBottomSheetDialog();
            //lensesStockBottomSheetDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetTheme);
            lensesStockBottomSheetDialog.show(getChildFragmentManager(), "bottomSheetLensesStock");
        });

        // Delete current lenses listener
        deleteCurrentLenses.setOnClickListener(v -> {
            AppDatabase db = AppDatabase.getInstance(getContext(  ));
            Lenses inUseLenses = db.lensesDao().getInUse();
            inUseLenses.state = State.IN_HISTORY;
            db.lensesDao().update(inUseLenses);
            Toast.makeText(getContext(), "Lenses deleted", Toast.LENGTH_SHORT).show();
        });

        Context context = getContext();
        AppDatabase db = AppDatabase.getInstance(context);
        lensesList = db.lensesDao().getAllNotInUse();
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

    private void setLensesHistoryRecyclerView(List<Lenses> lenses) {
        Context context = getContext();
        LensesAdapter lensesAdapter = new LensesAdapter(context, lenses);
        lensesHistoryRecyclerView.setHasFixedSize(true);
        lensesHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        lensesHistoryRecyclerView.setAdapter(lensesAdapter);
    }

    private void setLensesStockRecyclerView(List<Lenses> lenses) {
        Context context = getContext();
        LensesStockAdapter lensesStockAdapter = new LensesStockAdapter(context, lenses, this);

        lensesStockRecyclerView.setHasFixedSize(true);
        lensesStockRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        lensesStockRecyclerView.setAdapter(lensesStockAdapter);
    }

    private void updateLensesSummary(Lenses lenses) {
        if (null == lenses) {
            lensesProgressbar.setProgressMax(100f);
            lensesProgressbar.setProgressWithAnimation(0f, (long) 1000); // =1s
            lensesLeftDaysCount.setText("-");
        }
        else {
            UsageProcessor usageProcessor = new UsageProcessor();
            Long daysLeft = usageProcessor.calculateUsageLeft(lenses.startDate,
                    lenses.expirationDate, lenses.useInterval);

            lensesProgressbar.setProgressMax(lenses.useInterval);
            lensesProgressbar.setProgressWithAnimation(Math.max(daysLeft, 0),
                    1000L);

            lensesLeftDaysCount.setText(daysLeft.toString());
        }
    }

    @Override
    public void onLensClick(int position) {
        MaterialDatePicker startDatePicker;

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select start date");
        builder.setSelection(today);
        startDatePicker = builder.build();
        startDatePicker.show(getFragmentManager(), "DATE_PICKER");

        startDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                AppDatabase db = AppDatabase.getInstance(getContext());
                Lenses inUseLenses = db.lensesDao().getInUse();
                if(inUseLenses != null)
                {
                    inUseLenses.state = State.IN_HISTORY;
                    db.lensesDao().update(inUseLenses);
                }
                Lenses lenses = stockLensesList.get(position);
                lenses.state = State.IN_USE;
                Date date = new Date((Long) startDatePicker.getSelection());
                SimpleDateFormat simpleFormat = new SimpleDateFormat("dd.MM.yyyy");
                String dateValue = simpleFormat.format(date);
                try {
                    lenses.startDate = new SimpleDateFormat("dd.MM.yyyy").parse(dateValue);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                db.lensesDao().update(lenses);
            }
        });
    }
}