package com.appnuggets.lensminder.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.adapter.LensesAdapter;
import com.appnuggets.lensminder.bottomsheet.LensesBottomSheetDialog;
import com.appnuggets.lensminder.bottomsheet.LensesStockBottomSheetDialog;
import com.appnuggets.lensminder.database.AppDatabase;
import com.appnuggets.lensminder.database.entity.Lenses;
import com.appnuggets.lensminder.model.UsageProcessor;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

public class LensesFragment extends Fragment {

    private CircularProgressBar lensesProgressbar;
    private TextView lensesLeftDaysCount;

    private RecyclerView lensesHistoryRecyclerView;
    private RecyclerView lensesStockRecyclerView;

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
            // TODO Implement method for current lenses deletion
        });

        setLensesHistoryRecyclerView();
        setLensesStockRecyclerView();
    }

    @Override
    public void onStart() {
        super.onStart();

        AppDatabase db = AppDatabase.getInstance(getContext());
        Lenses lensesInUse = db.lensesDao().getInUse();
        updateDropsSummary(lensesInUse);
    }

    private void setLensesHistoryRecyclerView() {
        Context context = getContext();
        AppDatabase db = AppDatabase.getInstance(context);
        LensesAdapter lensesAdapter = new LensesAdapter(context, db.lensesDao().getAllNotInUse());

        lensesHistoryRecyclerView.setHasFixedSize(true);
        lensesHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        lensesHistoryRecyclerView.setAdapter(lensesAdapter);
    }

    private void setLensesStockRecyclerView() {
        Context context = getContext();
        AppDatabase db = AppDatabase.getInstance(context);
        //TODO Implement method
    }

    private void updateDropsSummary(Lenses lenses) {
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
            lensesProgressbar.setProgressWithAnimation(lenses.useInterval - daysLeft,
                    1000L);

            lensesLeftDaysCount.setText(daysLeft.toString());
        }
    }
}