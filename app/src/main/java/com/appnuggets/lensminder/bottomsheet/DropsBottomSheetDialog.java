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
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.TimeZone;

public class DropsBottomSheetDialog extends BottomSheetDialogFragment {

    private AutoCompleteTextView autoCompleteTextView;

    private TextInputEditText dropsStartDate;
    private TextInputEditText dropsExpDate;

    MaterialDatePicker startDatePicker;
    MaterialDatePicker expDatePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.drops_bottom_sheet_layout, container, false);

        autoCompleteTextView = (AutoCompleteTextView) v.findViewById(R.id.autoComplete_drops);
        completeDropdownList();


        dropsStartDate = (TextInputEditText) v.findViewById(R.id.dropsStartDate);
        dropsExpDate = (TextInputEditText) v.findViewById(R.id.dropsExpDate);
        setCalendar();

        dropsStartDate.setOnClickListener(v1 -> startDatePicker.show(getFragmentManager(), "DATE_PICKER"));

        dropsStartDate.setOnFocusChangeListener((v12, hasFocus) -> {
            if (hasFocus) {
                startDatePicker.show(getFragmentManager(), "DATE_PICKER");
            }
        });

        startDatePicker.addOnPositiveButtonClickListener(selection -> {
            dropsStartDate.setText(startDatePicker.getHeaderText());

            /*String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            startDateDrops.setText(sdf.format(materialDatePicker.getHeaderText()));*/
        });

        dropsExpDate.setOnClickListener(v15 -> expDatePicker.show(getFragmentManager(), "DATE_PICKER"));

        dropsExpDate.setOnFocusChangeListener((v13, hasFocus) -> {
            if (hasFocus) {
                expDatePicker.show(getFragmentManager(), "DATE_PICKER");
            }
        });

        expDatePicker.addOnPositiveButtonClickListener(selection -> dropsExpDate.setText(expDatePicker.getHeaderText()));

        MaterialButton saveButton = (MaterialButton) v.findViewById(R.id.dropsSaveButton);
        saveButton.setOnClickListener(v14 -> {
            //TODO
        });

       return v;
    }

    public void setCalendar(){
        dropsStartDate.setInputType(InputType.TYPE_NULL);
        dropsStartDate.setKeyListener(null);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        long today = MaterialDatePicker.todayInUtcMilliseconds();

                /*calendar.roll(Calendar.MONTH, Calendar.JANUARY);
                long january = calendar.getTimeInMillis();
                calendar.roll(Calendar.MONTH, Calendar.DECEMBER);
                long december = calendar.getTimeInMillis();*/

        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
                /*constraintBuilder.setStart(january);
                constraintBuilder.setEnd(december);*/

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        // builder.setTheme(R.style.MyDatePickerDialogTheme);
        builder.setTitleText("Select start date");
        builder.setSelection(today);
        builder.setCalendarConstraints(constraintBuilder.build());
        startDatePicker = builder.build();

        dropsExpDate.setInputType(InputType.TYPE_NULL);
        dropsExpDate.setKeyListener(null);
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