package com.appnuggets.lensminder.bottomsheet;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.database.AppDatabase;
import com.appnuggets.lensminder.database.entity.Lenses;
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
import java.util.Objects;
import java.util.TimeZone;

public class LensesBottomSheetDialog extends BottomSheetDialogFragment {

    private AutoCompleteTextView lensesWearCycle;
    private TextInputEditText lensesStartDate;
    private TextInputEditText lensesExpDate;
    String[] items;

    MaterialDatePicker startDatePicker;
    MaterialDatePicker expDatePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lenses_bottom_sheet_layout, container, false);

        lensesWearCycle = v.findViewById(R.id.autoComplete_lenses);
        completeDropdownList();

        lensesStartDate = v.findViewById(R.id.lensesStartDate);
        lensesExpDate = v.findViewById(R.id.lensesExpDate);
        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd.MM.yyyy");
        setCalendar();

        lensesStartDate.setOnClickListener(v1 -> startDatePicker.show(getFragmentManager(), "DATE_PICKER"));

        lensesStartDate.setOnFocusChangeListener((v12, hasFocus) -> {
            if (hasFocus) {
                startDatePicker.show(getFragmentManager(), "DATE_PICKER");
            }
        });

        startDatePicker.addOnPositiveButtonClickListener(selection -> {
            Date date = new Date((Long) startDatePicker.getSelection()) ;
            lensesStartDate.setText(simpleFormat.format(date));
        });

        lensesExpDate.setOnClickListener(v13 -> expDatePicker.show(getFragmentManager(), "DATE_PICKER"));

        lensesExpDate.setOnFocusChangeListener((v14, hasFocus) -> {
            if (hasFocus) {
                expDatePicker.show(getFragmentManager(), "DATE_PICKER");
            }
        });

        expDatePicker.addOnPositiveButtonClickListener(selection -> {
            Date date = new Date((Long) expDatePicker.getSelection()) ;
            lensesExpDate.setText(simpleFormat.format(date));
        });

        MaterialButton saveButton = (MaterialButton) v.findViewById(R.id.lensesSaveButton);
        TextInputEditText lensesName = v.findViewById(R.id.lensesName);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(lensesWearCycle.getText().toString().isEmpty() ||
                        lensesExpDate.getText().toString().isEmpty() ||
                        lensesStartDate.getText().toString().isEmpty() ||
                        lensesName.getText().toString().isEmpty()) {
                    dismiss();
                    new AlertDialog.Builder(getContext())
                            .setTitle("Fields must not be empty")
                            .setMessage("").show();
                }
                else
                {
                    long expPeriod = 0L;

                    if(lensesWearCycle.getText().toString().equals(items[0])) expPeriod = 14L;
                    else if(lensesWearCycle.getText().toString().equals(items[1])) expPeriod = 31L;
                    else if(lensesWearCycle.getText().toString().equals(items[2])) expPeriod = 93L;
                    else if(lensesWearCycle.getText().toString().equals(items[3])) expPeriod = 186L;

                    try {
                        Lenses lenses = new Lenses(Objects.requireNonNull(lensesName.getText()).toString(), true,
                                new SimpleDateFormat("dd.MM.yyyy").parse(Objects.requireNonNull(lensesExpDate.getText()).toString()),
                                new SimpleDateFormat("dd.MM.yyyy").parse(Objects.requireNonNull(lensesStartDate.getText()).toString()),
                                expPeriod);

                        AppDatabase db = AppDatabase.getInstance(getContext());
                        Lenses inUseLenses = db.lensesDao().getInUse();
                        if(inUseLenses != null)
                        {
                            inUseLenses.inUse = false;
                            db.lensesDao().update(inUseLenses);
                        }

                        db.lensesDao().insert(lenses);
                        dismiss();

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

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select start date");
        builder.setSelection(today);
        builder.setCalendarConstraints(constraintBuilder.build());

        startDatePicker = builder.build();

        constraintBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder builderExp = MaterialDatePicker.Builder.datePicker();
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
