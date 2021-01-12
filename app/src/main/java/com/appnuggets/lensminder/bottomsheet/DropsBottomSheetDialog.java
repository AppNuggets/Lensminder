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
import com.appnuggets.lensminder.database.entity.Drops;
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

public class DropsBottomSheetDialog extends BottomSheetDialogFragment {

    private AutoCompleteTextView dropsExpPeriod;

    private TextInputEditText dropsStartDate;
    private TextInputEditText dropsExpDate;
    private String[] items;

    MaterialDatePicker startDatePicker;
    MaterialDatePicker expDatePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.drops_bottom_sheet_layout, container, false);

        dropsExpPeriod = (AutoCompleteTextView) v.findViewById(R.id.autoComplete_drops);
        completeDropdownList();


        dropsStartDate = (TextInputEditText) v.findViewById(R.id.dropsStartDate);
        dropsExpDate = (TextInputEditText) v.findViewById(R.id.dropsExpDate);
        setCalendar();
        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd.MM.yyyy");

        dropsStartDate.setOnClickListener(v1 -> startDatePicker.show(getFragmentManager(), "DATE_PICKER"));

        dropsStartDate.setOnFocusChangeListener((v12, hasFocus) -> {
            if (hasFocus) {
                startDatePicker.show(getFragmentManager(), "DATE_PICKER");
            }
        });

        startDatePicker.addOnPositiveButtonClickListener(selection -> {
            Date date = new Date((Long) startDatePicker.getSelection()) ;
            dropsStartDate.setText(simpleFormat.format(date));
        });

        dropsExpDate.setOnClickListener(v15 -> expDatePicker.show(getFragmentManager(), "DATE_PICKER"));

        dropsExpDate.setOnFocusChangeListener((v13, hasFocus) -> {
            if (hasFocus) {
                expDatePicker.show(getFragmentManager(), "DATE_PICKER");
            }
        });

        expDatePicker.addOnPositiveButtonClickListener(selection -> {
            Date date = new Date((Long) expDatePicker.getSelection()) ;
            dropsExpDate.setText(simpleFormat.format(date));
        });

        MaterialButton saveButton = (MaterialButton) v.findViewById(R.id.dropsSaveButton);
        TextInputEditText dropsName = v.findViewById(R.id.dropsName);
        saveButton.setOnClickListener(v14 -> {

            if(dropsExpPeriod.getText().toString().isEmpty() ||
                    dropsExpDate.getText().toString().isEmpty() ||
                 dropsStartDate.getText().toString().isEmpty() ||
                   dropsName.getText().toString().isEmpty()) {
                dismiss();
                new AlertDialog.Builder(getContext())
                        .setTitle("Fields must not be empty")
                        .setMessage("").show();
            }
            else
            {
                long expPeriod = 0L;

                if(dropsExpPeriod.getText().toString().equals(items[0])) expPeriod = 31L;
                else if(dropsExpPeriod.getText().toString().equals(items[1])) expPeriod = 93L;
                else if(dropsExpPeriod.getText().toString().equals(items[2])) expPeriod = 186L;

                try {
                    Drops drops = new Drops(Objects.requireNonNull(dropsName.getText()).toString(), true,
                            new SimpleDateFormat("dd.MM.yyyy").parse(Objects.requireNonNull(dropsExpDate.getText()).toString()),
                            new SimpleDateFormat("dd.MM.yyyy").parse(Objects.requireNonNull(dropsStartDate.getText()).toString()),
                            expPeriod);

                    AppDatabase db = AppDatabase.getInstance(getContext());

                    Drops inUseDrops = db.dropsDao().getInUse();
                    if(inUseDrops != null)
                    {
                        inUseDrops.inUse = false;
                        db.dropsDao().update(inUseDrops);
                    }

                    db.dropsDao().insert(drops);
                    dismiss();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
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
        items = new String[] {
                "One month",
                "Three months",
                "Six months"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.dropdown_items,
                items
        );

        dropsExpPeriod.setAdapter(adapter);
    }
}