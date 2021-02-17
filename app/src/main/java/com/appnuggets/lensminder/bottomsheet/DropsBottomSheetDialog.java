package com.appnuggets.lensminder.bottomsheet;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.activity.RefreshInterface;
import com.appnuggets.lensminder.database.AppDatabase;
import com.appnuggets.lensminder.database.entity.Drops;
import com.appnuggets.lensminder.model.NotificationCode;
import com.appnuggets.lensminder.model.UsageProcessor;
import com.appnuggets.lensminder.service.NotificationService;
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
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class DropsBottomSheetDialog extends BottomSheetDialogFragment {

    private AutoCompleteTextView dropsExpPeriod;

    private TextInputEditText dropsStartDate;
    private TextInputEditText dropsExpDate;
    private String[] items;

    MaterialDatePicker<Long> startDatePicker;
    MaterialDatePicker<Long> expDatePicker;

    private final RefreshInterface refreshInterface;

    public DropsBottomSheetDialog(RefreshInterface refreshInterface){
        this.refreshInterface = refreshInterface;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.drops_bottom_sheet_layout, container, false);

        dropsExpPeriod = v.findViewById(R.id.autoComplete_drops);
        dropsStartDate =  v.findViewById(R.id.dropsStartDate);
        dropsExpDate =  v.findViewById(R.id.dropsExpDate);
        MaterialButton saveButton = v.findViewById(R.id.dropsSaveButton);
        TextInputEditText dropsName = v.findViewById(R.id.dropsName);

        setCalendar();
        completeDropdownList();

        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);

        dropsStartDate.setOnClickListener(v1 -> startDatePicker.show(getParentFragmentManager(), "DATE_PICKER"));

        dropsStartDate.setOnFocusChangeListener((v12, hasFocus) -> {
            if (hasFocus) {
                startDatePicker.show(getParentFragmentManager(), "DATE_PICKER");
            }
        });

        startDatePicker.addOnPositiveButtonClickListener(selection -> {
            if(startDatePicker.getSelection() != null) {
                Date date = new Date(startDatePicker.getSelection());
                dropsStartDate.setText(simpleFormat.format(date));
            }
        });

        dropsExpDate.setOnClickListener(v15 -> expDatePicker.show(getParentFragmentManager(), "DATE_PICKER"));

        dropsExpDate.setOnFocusChangeListener((v13, hasFocus) -> {
            if (hasFocus) {
                expDatePicker.show(getParentFragmentManager(), "DATE_PICKER");
            }
        });

        expDatePicker.addOnPositiveButtonClickListener(selection -> {
            if(expDatePicker.getSelection() != null) {
                Date date = new Date(expDatePicker.getSelection());
                dropsExpDate.setText(simpleFormat.format(date));
            }
        });

        saveButton.setOnClickListener(v14 -> {

            if(dropsExpPeriod.getText().toString().isEmpty() ||
                    Objects.requireNonNull(dropsExpDate.getText()).toString().isEmpty() ||
                    Objects.requireNonNull(dropsStartDate.getText()).toString().isEmpty() ||
                    Objects.requireNonNull(dropsName.getText()).toString().isEmpty()) {
                dismiss();
                Toast.makeText(getContext(), R.string.non_empty_required_message,
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                long expPeriod = 0L;

                if(dropsExpPeriod.getText().toString().equals(items[0])) expPeriod = 31L;
                else if(dropsExpPeriod.getText().toString().equals(items[1])) expPeriod = 93L;
                else if(dropsExpPeriod.getText().toString().equals(items[2])) expPeriod = 186L;

                try {
                    Drops drops = new Drops(Objects.requireNonNull(dropsName.getText()).toString(), true,
                            simpleFormat.parse(Objects.requireNonNull(dropsExpDate.getText()).toString()),
                            simpleFormat.parse(Objects.requireNonNull(dropsStartDate.getText()).toString()),
                            null,
                            expPeriod);
                    UsageProcessor usageProcessor = new UsageProcessor();
                    drops.endDate = usageProcessor.calculateEndDate(drops.startDate,
                            drops.expirationDate, drops.useInterval);
                    AppDatabase db = AppDatabase.getInstance(getContext());

                    Drops inUseDrops = db.dropsDao().getInUse();
                    if(inUseDrops != null)
                    {
                        inUseDrops.inUse = false;
                        Long leftDays = usageProcessor.calculateUsageLeft(inUseDrops.startDate,
                                inUseDrops.expirationDate, inUseDrops.useInterval);
                        if( leftDays > 0) {
                            try {
                                Date today = new Date();
                                inUseDrops.endDate = simpleFormat.parse(simpleFormat.format(today));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        db.dropsDao().update(inUseDrops);
                    }
                    db.dropsDao().insert(drops);

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getContext());
                    boolean enabledNotification = prefs.getBoolean("notify", false);
                    if(enabledNotification) {
                        NotificationService.createNotification(getContext(),
                                usageProcessor.calculateUsageLeft(drops.startDate,
                                        drops.expirationDate, drops.useInterval),
                                NotificationCode.DROPS_EXPIRED);
                    }

                    dismiss();
                    refreshInterface.refreshData();
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

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        // builder.setTheme(R.style.MyDatePickerDialogTheme);
        builder.setTitleText(R.string.start_date_picker_title);
        builder.setSelection(today);
        builder.setCalendarConstraints(constraintBuilder.build());
        startDatePicker = builder.build();

        dropsExpDate.setInputType(InputType.TYPE_NULL);
        dropsExpDate.setKeyListener(null);
        constraintBuilder.setValidator(DateValidatorPointForward.now());
        MaterialDatePicker.Builder<Long> builderExp = MaterialDatePicker.Builder.datePicker();
        builderExp.setTitleText(R.string.exp_date_picker_title);
        builderExp.setCalendarConstraints(constraintBuilder.build());
        expDatePicker = builderExp.build();
    }

    public void completeDropdownList() {
        items = new String[] {
                getString(R.string.one_month_label),
                getString(R.string.three_months_label),
                getString(R.string.six_months_label)
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.dropdown_items,
                items
        );

        dropsExpPeriod.setAdapter(adapter);
    }
}