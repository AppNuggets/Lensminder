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

public class DropsBottomSheetDialog extends BottomSheetDialogFragment {

    private TextInputLayout textInputLayout;
    private AutoCompleteTextView autoCompleteTextView;

    private TextInputEditText dropsStartDate;
    private TextInputEditText dropsExpDate;
    private MaterialButton saveButton;

    MaterialDatePicker startDatePicker;
    MaterialDatePicker expDatePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.drops_bottom_sheet_layout, container, false);

        textInputLayout = (TextInputLayout) v.findViewById(R.id.dropsExpPeriod);
        autoCompleteTextView = (AutoCompleteTextView) v.findViewById(R.id.autoComplete_drops);
        completeDropdownList();


        dropsStartDate = (TextInputEditText) v.findViewById(R.id.dropsStartDate);
        dropsExpDate = (TextInputEditText) v.findViewById(R.id.dropsExpDate);
        setCalendar();

        dropsStartDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startDatePicker.show(getFragmentManager(), "DATE_PICKER");
            }
        });

        dropsStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                dropsStartDate.setText(startDatePicker.getHeaderText());

                /*String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                startDateDrops.setText(sdf.format(materialDatePicker.getHeaderText()));*/
            }
        });

        dropsExpDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                expDatePicker.show(getFragmentManager(), "DATE_PICKER");
            }
        });

        dropsExpDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                dropsExpDate.setText(expDatePicker.getHeaderText());
            }
        });

        saveButton = (MaterialButton) v.findViewById(R.id.dropsSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO
            }
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