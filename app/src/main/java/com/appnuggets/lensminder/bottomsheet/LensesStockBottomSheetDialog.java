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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class LensesStockBottomSheetDialog extends BottomSheetDialogFragment {

    private AutoCompleteTextView autoCompleteTextView;
    private TextInputEditText stockLensesExpDate;

    MaterialDatePicker expDatePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lenses_stock_bottom_sheret_layout, container, false);

        autoCompleteTextView = (AutoCompleteTextView) v.findViewById(R.id.autoComplete_stockLenses);
        completeDropdownList();

        stockLensesExpDate = (TextInputEditText) v.findViewById(R.id.stockLensesExpDate);
        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd.MM.yyyy");
        setCalendar();

        stockLensesExpDate.setOnClickListener(v1 -> expDatePicker.show(getFragmentManager(), "DATE_PICKER"));

        stockLensesExpDate.setOnFocusChangeListener((v12, hasFocus) -> {
            if (hasFocus) {
                expDatePicker.show(getFragmentManager(), "DATE_PICKER");
            }
        });

        expDatePicker.addOnPositiveButtonClickListener(selection -> {
            Date date = new Date((Long) expDatePicker.getSelection()) ;
            stockLensesExpDate.setText(simpleFormat.format(date));
        });

        MaterialButton saveButton = (MaterialButton) v.findViewById(R.id.stockLensesSaveButton);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //TODO
            }
        });

        return v;
    }

    public void setCalendar(){
        stockLensesExpDate.setInputType(InputType.TYPE_NULL);
        stockLensesExpDate.setKeyListener(null);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();

        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
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
