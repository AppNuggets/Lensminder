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
import com.appnuggets.lensminder.database.entity.State;
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

public class LensesStockBottomSheetDialog extends BottomSheetDialogFragment {

    private AutoCompleteTextView stockLensesWearCycle;
    private TextInputEditText stockLensesExpDate;
    MaterialDatePicker expDatePicker;
    String[] items;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lenses_stock_bottom_sheret_layout, container, false);

        stockLensesWearCycle = (AutoCompleteTextView) v.findViewById(R.id.autoComplete_stockLenses);
        completeDropdownList();

        stockLensesExpDate = (TextInputEditText) v.findViewById(R.id.stockLensesExpDate);
        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd.MM.yyyy");
        setCalendar();

        stockLensesExpDate.setOnClickListener(v1 -> expDatePicker.show(getParentFragmentManager(), "DATE_PICKER"));

        stockLensesExpDate.setOnFocusChangeListener((v12, hasFocus) -> {
            if (hasFocus) {
                expDatePicker.show(getParentFragmentManager(), "DATE_PICKER");
            }
        });

        expDatePicker.addOnPositiveButtonClickListener(selection -> {
            Date date = new Date((Long) expDatePicker.getSelection()) ;
            stockLensesExpDate.setText(simpleFormat.format(date));
        });

        MaterialButton saveButton = (MaterialButton) v.findViewById(R.id.stockLensesSaveButton);
        TextInputEditText lensesName = v.findViewById(R.id.stockLensesName);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(stockLensesWearCycle.getText().toString().isEmpty() ||
                        stockLensesExpDate.getText().toString().isEmpty() ||
                        lensesName.getText().toString().isEmpty()) {
                    dismiss();
                    new AlertDialog.Builder(getContext())
                            .setTitle("Fields must not be empty")
                            .setMessage("").show();
                }
                else
                {
                    long expPeriod = 0L;

                    if(stockLensesWearCycle.getText().toString().equals(items[0])) expPeriod = 14L;
                    else if(stockLensesWearCycle.getText().toString().equals(items[1])) expPeriod = 31L;
                    else if(stockLensesWearCycle.getText().toString().equals(items[2])) expPeriod = 93L;
                    else if(stockLensesWearCycle.getText().toString().equals(items[3])) expPeriod = 186L;

                    try {
                        Lenses lenses = new Lenses(Objects.requireNonNull(lensesName.getText()).toString(),
                                State.IN_STOCK, new SimpleDateFormat("dd.MM.yyyy").
                                parse(Objects.requireNonNull(stockLensesExpDate.getText()).toString()),
                                null,
                                expPeriod);

                        AppDatabase db = AppDatabase.getInstance(getContext());
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

        stockLensesWearCycle.setAdapter(adapter);
    }
}
