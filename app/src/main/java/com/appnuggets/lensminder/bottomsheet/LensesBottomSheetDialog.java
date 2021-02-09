package com.appnuggets.lensminder.bottomsheet;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.activity.NavigationInterface;
import com.appnuggets.lensminder.activity.RefreshInterface;
import com.appnuggets.lensminder.database.AppDatabase;
import com.appnuggets.lensminder.database.entity.Lenses;
import com.appnuggets.lensminder.database.entity.State;
import com.appnuggets.lensminder.model.UsageProcessor;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class LensesBottomSheetDialog extends BottomSheetDialogFragment {

    private RefreshInterface refreshInterface;

    private AutoCompleteTextView lensesWearCycle;
    private TextInputEditText lensesStartDate;
    private TextInputEditText lensesExpDate;
    String[] items;

    MaterialDatePicker<Long> startDatePicker;
    MaterialDatePicker<Long> expDatePicker;

    public LensesBottomSheetDialog(RefreshInterface refreshInterface){
        this.refreshInterface = refreshInterface;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lenses_bottom_sheet_layout, container, false);

        lensesWearCycle = v.findViewById(R.id.autoComplete_lenses);
        lensesStartDate = v.findViewById(R.id.lensesStartDate);
        lensesExpDate = v.findViewById(R.id.lensesExpDate);
        MaterialButton saveButton = v.findViewById(R.id.lensesSaveButton);
        TextInputEditText lensesName = v.findViewById(R.id.lensesName);

        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
        setCalendar();
        completeDropdownList();

        lensesStartDate.setOnClickListener(v1 -> startDatePicker.show(getParentFragmentManager(), "DATE_PICKER"));

        lensesStartDate.setOnFocusChangeListener((v12, hasFocus) -> {
            if (hasFocus) {
                startDatePicker.show(getParentFragmentManager(), "DATE_PICKER");
            }
        });

        startDatePicker.addOnPositiveButtonClickListener(selection -> {
            if(startDatePicker.getSelection() != null) {
                Date date = new Date(startDatePicker.getSelection());
                lensesStartDate.setText(simpleFormat.format(date));
            }
        });

        lensesExpDate.setOnClickListener(v13 -> expDatePicker.show(getParentFragmentManager(), "DATE_PICKER"));

        lensesExpDate.setOnFocusChangeListener((v14, hasFocus) -> {
            if (hasFocus) {
                expDatePicker.show(getParentFragmentManager(), "DATE_PICKER");
            }
        });

        expDatePicker.addOnPositiveButtonClickListener(selection -> {
            if(expDatePicker.getSelection() != null) {
                Date date = new Date(expDatePicker.getSelection());
                lensesExpDate.setText(simpleFormat.format(date));
            }
        });

        saveButton.setOnClickListener(v15 -> {

            if(lensesWearCycle.getText().toString().isEmpty() ||
                    Objects.requireNonNull(lensesExpDate.getText()).toString().isEmpty() ||
                    Objects.requireNonNull(lensesStartDate.getText()).toString().isEmpty() ||
                    Objects.requireNonNull(lensesName.getText()).toString().isEmpty()) {
                dismiss();
                Toast.makeText(getContext(), "Fields must not be empty", Toast.LENGTH_SHORT).show();
            }
            else
            {
                long expPeriod = 0L;

                if(lensesWearCycle.getText().toString().equals(items[0])) expPeriod = 14L;
                else if(lensesWearCycle.getText().toString().equals(items[1])) expPeriod = 31L;
                else if(lensesWearCycle.getText().toString().equals(items[2])) expPeriod = 93L;
                else if(lensesWearCycle.getText().toString().equals(items[3])) expPeriod = 186L;

                try {
                    Lenses lenses = new Lenses(Objects.requireNonNull(lensesName.getText()).
                            toString(), State.IN_USE,
                            simpleFormat.parse(Objects.requireNonNull(lensesExpDate.getText())
                                    .toString()),
                            simpleFormat.parse(Objects.requireNonNull(lensesStartDate.getText())
                                    .toString()),
                            null,
                            expPeriod);
                    UsageProcessor usageProcessor = new UsageProcessor();
                    lenses.endDate = usageProcessor.calculateEndDate(lenses.startDate,
                            lenses.expirationDate, lenses.useInterval);
                    AppDatabase db = AppDatabase.getInstance(getContext());
                    Lenses inUseLenses = db.lensesDao().getInUse();
                    if(inUseLenses != null)
                    {
                        inUseLenses.state = State.IN_HISTORY;
                        Long leftDays = usageProcessor.calculateUsageLeft(inUseLenses.startDate,
                                inUseLenses.expirationDate, inUseLenses.useInterval);
                        if( leftDays > 0) {
                            try {
                                Date today = new Date();
                                inUseLenses.endDate = simpleFormat.parse(simpleFormat.format(today));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        db.lensesDao().update(inUseLenses);
                    }

                    db.lensesDao().insert(lenses);
                    dismiss();
                        refreshInterface.refreshData();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return v;
    }

    public void setCalendar(){
        lensesStartDate.setInputType(InputType.TYPE_NULL);
        lensesStartDate.setKeyListener(null);
        lensesExpDate.setInputType(InputType.TYPE_NULL);
        lensesExpDate.setKeyListener(null);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        long today = MaterialDatePicker.todayInUtcMilliseconds();

        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select start date");
        builder.setSelection(today);
        builder.setCalendarConstraints(constraintBuilder.build());

        startDatePicker = builder.build();

        constraintBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder<Long> builderExp = MaterialDatePicker.Builder.datePicker();
        builderExp.setTitleText("Select exp. date");
        builderExp.setCalendarConstraints(constraintBuilder.build());

        expDatePicker = builderExp.build();
    }

    public void completeDropdownList() {
        items = new String[] {
                "Two Weeks",
                "One month",
                "Three months",
                "Six months"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.dropdown_items,
                items
        );

        lensesWearCycle.setAdapter(adapter);
    }
}
