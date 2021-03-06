package com.appnuggets.lensminder.bottomsheet;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.activity.RefreshInterface;
import com.appnuggets.lensminder.database.AppDatabase;
import com.appnuggets.lensminder.database.entity.Solution;
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

public class SolutionBottomSheetDialog extends BottomSheetDialogFragment {

    private TextInputEditText solutionStartDate;
    private TextInputEditText solutionExpDate;

    MaterialDatePicker<Long> startDatePicker;
    MaterialDatePicker<Long> expDatePicker;

    private final RefreshInterface refreshInterface;

    public SolutionBottomSheetDialog(RefreshInterface refreshInterface){
        this.refreshInterface = refreshInterface;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.solution_bottom_sheet_layout, container,
                false);

        solutionStartDate = v.findViewById(R.id.solutionStartDate);
        solutionExpDate = v.findViewById(R.id.solutionExpDate);
        MaterialButton saveButton = v.findViewById(R.id.solutionSaveButton);
        TextInputEditText solutionName = v.findViewById(R.id.solutionName);

        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
        setCalendar();

        solutionStartDate.setOnClickListener(v1 -> startDatePicker.show(getParentFragmentManager(),
                "DATE_PICKER"));

        solutionStartDate.setOnFocusChangeListener((v12, hasFocus) -> {
            if (hasFocus) {
                startDatePicker.show(getParentFragmentManager(), "DATE_PICKER");
            }
        });

        startDatePicker.addOnPositiveButtonClickListener(selection -> {
            if(startDatePicker.getSelection() != null) {
                Date date = new Date(startDatePicker.getSelection());
                solutionStartDate.setText(simpleFormat.format(date));
            }
        });

        solutionExpDate.setOnClickListener(v13 -> expDatePicker.show(getParentFragmentManager(),
                "DATE_PICKER"));

        solutionExpDate.setOnFocusChangeListener((v14, hasFocus) -> {
            if (hasFocus) {
                expDatePicker.show(getParentFragmentManager(), "DATE_PICKER");
            }
        });

        expDatePicker.addOnPositiveButtonClickListener(selection -> {
            if(expDatePicker.getSelection() != null) {
                Date date = new Date(expDatePicker.getSelection());
                solutionExpDate.setText(simpleFormat.format(date));
            }
        });

        saveButton.setOnClickListener(v15 -> {

            if(Objects.requireNonNull(solutionExpDate.getText()).toString().isEmpty() ||
                    Objects.requireNonNull(solutionStartDate.getText()).toString().isEmpty() ||
                    Objects.requireNonNull(solutionName.getText()).toString().isEmpty()) {
                dismiss();
                Toast.makeText(getContext(), R.string.non_empty_required_message,
                        Toast.LENGTH_SHORT).show();
            }
            else
            {
                try {
                    Solution solution = new Solution(Objects.requireNonNull(
                            solutionName.getText()).toString(),
                            true,
                            simpleFormat.parse(Objects.requireNonNull(
                                    solutionExpDate.getText()).toString()),
                            simpleFormat.parse(Objects.requireNonNull(
                                    solutionStartDate.getText()).toString()),
                            null,
                            93L);
                    UsageProcessor usageProcessor = new UsageProcessor();
                    solution.endDate = usageProcessor.calculateEndDate(solution.startDate,
                            solution.expirationDate, solution.useInterval);
                    AppDatabase db = AppDatabase.getInstance(getContext());

                    Solution inUseSolution = db.solutionDao().getInUse();
                    if(inUseSolution != null)
                    {
                        inUseSolution.inUse = false;
                        Long leftDays = usageProcessor.calculateUsageLeft(inUseSolution.startDate,
                                inUseSolution.expirationDate, inUseSolution.useInterval);
                        if( leftDays > 0) {
                            try {
                                Date today = new Date();
                                inUseSolution.endDate = simpleFormat.parse(
                                        simpleFormat.format(today));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        db.solutionDao().update(inUseSolution);
                    }
                    db.solutionDao().insert(solution);

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(
                            this.getContext());
                    boolean enabledNotification = prefs.getBoolean("notify", false);
                    if(enabledNotification) {
                        NotificationService.createNotification(getContext(),
                                usageProcessor.calculateUsageLeft(solution.startDate,
                                        solution.expirationDate, solution.useInterval),
                                NotificationCode.SOLUTION_EXPIRED);
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
        solutionStartDate.setInputType(InputType.TYPE_NULL);
        solutionStartDate.setKeyListener(null);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        long today = MaterialDatePicker.todayInUtcMilliseconds();

        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText(R.string.start_date_picker_title);
        builder.setSelection(today);
        builder.setCalendarConstraints(constraintBuilder.build());
        startDatePicker = builder.build();

        solutionExpDate.setInputType(InputType.TYPE_NULL);
        solutionExpDate.setKeyListener(null);
        constraintBuilder.setValidator(DateValidatorPointForward.now());
        MaterialDatePicker.Builder<Long> builderExp = MaterialDatePicker.Builder.datePicker();
        builderExp.setTitleText(R.string.exp_date_picker_title);
        builderExp.setCalendarConstraints(constraintBuilder.build());
        expDatePicker = builderExp.build();
    }

}
