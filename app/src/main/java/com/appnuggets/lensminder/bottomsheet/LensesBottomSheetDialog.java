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
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.TimeZone;

public class LensesBottomSheetDialog extends BottomSheetDialogFragment {

    private TextInputLayout lensesWearCycle;
    private AutoCompleteTextView autoCompleteTextView;
    private TextInputEditText lensesStartDate;
    private TextInputEditText lensesExpDate;
    private MaterialButton saveButton;

    MaterialDatePicker startDatePicker;
    MaterialDatePicker expDatePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lenses_bottom_sheet_layout, container, false);

        lensesWearCycle = (TextInputLayout) v.findViewById(R.id.lensesWearCycle);
        autoCompleteTextView = (AutoCompleteTextView) v.findViewById(R.id.autoComplete_lenses);

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

        lensesStartDate = (TextInputEditText) v.findViewById(R.id.dropsStartDate);
        lensesExpDate = (TextInputEditText) v.findViewById(R.id.dropsExpDate);
        setCalendar();

        lensesStartDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startDatePicker.show(getFragmentManager(), "DATE_PICKER");
            }
        });

        lensesStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    startDatePicker.show(getFragmentManager(), "DATE_PICKER");
                }
            }
        });

        startDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                lensesStartDate.setText(startDatePicker.getHeaderText());
            }
        });

        lensesExpDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                expDatePicker.show(getFragmentManager(), "DATE_PICKER");
            }
        });

        lensesExpDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    expDatePicker.show(getFragmentManager(), "DATE_PICKER");
                }
            }
        });

        expDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                lensesExpDate.setText(expDatePicker.getHeaderText());
            }
        });

        saveButton = (MaterialButton) v.findViewById(R.id.lensesSaveButton);
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

}
