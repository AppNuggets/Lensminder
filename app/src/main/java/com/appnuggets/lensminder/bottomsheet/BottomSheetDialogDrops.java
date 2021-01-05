package com.appnuggets.lensminder.bottomsheet;

import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

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

public class BottomSheetDialogDrops extends BottomSheetDialogFragment {

    private TextInputLayout textInputLayout;
    private AutoCompleteTextView autoCompleteTextView;

    private TextInputEditText startDateDrops;
    private TextInputEditText expDateDrops;
    private MaterialButton saveButton;

    MaterialDatePicker startDatePicker;
    MaterialDatePicker expDatePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_drops_layout, container, false);

        textInputLayout = (TextInputLayout) v.findViewById(R.id.expPeriod);
        autoCompleteTextView = (AutoCompleteTextView) v.findViewById(R.id.autoComplete_drops);

        String[] items = new String[] {
                "Month",
                "Three months"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.dropdown_item_drops,
                items
        );

        autoCompleteTextView.setAdapter(adapter);


        startDateDrops = (TextInputEditText) v.findViewById(R.id.startDateDrops);
        startDateDrops.setInputType(InputType.TYPE_NULL);
        startDateDrops.setKeyListener(null);

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

        constraintBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
       // builder.setTheme(R.style.MyDatePickerDialogTheme);
        builder.setTitleText("Select start date");
        builder.setSelection(today);
        builder.setCalendarConstraints(constraintBuilder.build());
        startDatePicker = builder.build();

        startDateDrops.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startDatePicker.show(getFragmentManager(), "DATE_PICKER");
            }
        });

        startDateDrops.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                startDateDrops.setText(startDatePicker.getHeaderText());

                /*String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                startDateDrops.setText(sdf.format(materialDatePicker.getHeaderText()));*/
            }
        });

        expDateDrops = (TextInputEditText) v.findViewById(R.id.expDateDrops);
        expDateDrops.setInputType(InputType.TYPE_NULL);
        expDateDrops.setKeyListener(null);
        MaterialDatePicker.Builder builderExp = MaterialDatePicker.Builder.datePicker();
        builderExp.setTitleText("Select exp. date");
        builderExp.setCalendarConstraints(constraintBuilder.build());
        expDatePicker = builderExp.build();

        expDateDrops.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                expDatePicker.show(getFragmentManager(), "DATE_PICKER");
            }
        });

        expDateDrops.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                expDateDrops.setText(expDatePicker.getHeaderText());
            }
        });

        saveButton = (MaterialButton) v.findViewById(R.id.saveDrops);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //zapis do bazy
            }
        });

       return v;
    }
}