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

public class LensesStockBottomSheetDialog extends BottomSheetDialogFragment {

    private TextInputLayout stockLensesWearCycle;
    private AutoCompleteTextView autoCompleteTextView;
    private TextInputEditText stockLensesExpDate;
    private MaterialButton saveButton;

    MaterialDatePicker expDatePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lenses_stock_bottom_sheret_layout, container, false);

        stockLensesWearCycle = (TextInputLayout) v.findViewById(R.id.stockLensesWearCycle);
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

        stockLensesExpDate = (TextInputEditText) v.findViewById(R.id.stockLensesExpDate);
        setCalendar();

        stockLensesExpDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                expDatePicker.show(getFragmentManager(), "DATE_PICKER");
            }
        });

        stockLensesExpDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                stockLensesExpDate.setText(expDatePicker.getHeaderText());
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
}
