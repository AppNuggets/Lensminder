package com.appnuggets.lensminder.bottomsheet;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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

import java.util.Calendar;
import java.util.TimeZone;

public class SolutionBottomSheetDialog extends BottomSheetDialogFragment {

    private TextInputEditText solutionStartDate;
    private TextInputEditText solutionExpDate;
    private MaterialButton saveButton;

    MaterialDatePicker startDatePicker;
    MaterialDatePicker expDatePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.solution_bottom_sheet_layout, container, false);

        solutionStartDate = (TextInputEditText) v.findViewById(R.id.solutionStartDate);
        solutionExpDate = (TextInputEditText) v.findViewById(R.id.solutionExpDate);
        setCalendar();

        solutionStartDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startDatePicker.show(getFragmentManager(), "DATE_PICKER");
            }
        });

        solutionStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                solutionStartDate.setText(startDatePicker.getHeaderText());
            }
        });

        solutionExpDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                expDatePicker.show(getFragmentManager(), "DATE_PICKER");
            }
        });

        solutionExpDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                solutionExpDate.setText(expDatePicker.getHeaderText());
            }
        });

        saveButton = (MaterialButton) v.findViewById(R.id.solutionSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        return v;
    }

    public void setCalendar(){
        solutionStartDate.setInputType(InputType.TYPE_NULL);
        solutionStartDate.setKeyListener(null);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        long today = MaterialDatePicker.todayInUtcMilliseconds();

        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select start date");
        builder.setSelection(today);
        builder.setCalendarConstraints(constraintBuilder.build());
        startDatePicker = builder.build();

        solutionExpDate.setInputType(InputType.TYPE_NULL);
        solutionExpDate.setKeyListener(null);
        constraintBuilder.setValidator(DateValidatorPointForward.now());
        MaterialDatePicker.Builder builderExp = MaterialDatePicker.Builder.datePicker();
        builderExp.setTitleText("Select exp. date");
        builderExp.setCalendarConstraints(constraintBuilder.build());
        expDatePicker = builderExp.build();
    }

}
