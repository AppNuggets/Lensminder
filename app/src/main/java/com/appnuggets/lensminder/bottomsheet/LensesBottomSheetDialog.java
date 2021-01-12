package com.appnuggets.lensminder.bottomsheet;

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
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class LensesBottomSheetDialog extends BottomSheetDialogFragment {

    private AutoCompleteTextView autoCompleteTextView;
    private TextInputEditText lensesStartDate;
    private TextInputEditText lensesExpDate;

    MaterialDatePicker startDatePicker;
    MaterialDatePicker expDatePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lenses_bottom_sheet_layout, container, false);

        autoCompleteTextView = (AutoCompleteTextView) v.findViewById(R.id.autoComplete_lenses);
        completeDropdownList();

        lensesStartDate = (TextInputEditText) v.findViewById(R.id.lensesStartDate);
        lensesExpDate = (TextInputEditText) v.findViewById(R.id.lensesExpDate);
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
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO
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
        String[] items = new String[] {
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

        autoCompleteTextView.setAdapter(adapter);
    }
}
