package com.appnuggets.lensminder.bottomsheet;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appnuggets.lensminder.R;
import com.appnuggets.lensminder.database.AppDatabase;
import com.appnuggets.lensminder.database.entity.Container;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class ContainerBottomSheetDialog extends BottomSheetDialogFragment {

    private TextInputEditText containerStartDate;
    private MaterialDatePicker startDatePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.container_bottom_sheet_layout, container, false);

        containerStartDate = v.findViewById(R.id.containerStartDate);
        SimpleDateFormat simpleFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.UK);
        setCalendar();

        containerStartDate.setOnClickListener(v1 -> startDatePicker.show(getParentFragmentManager(), "DATE_PICKER"));

        containerStartDate.setOnFocusChangeListener((v12, hasFocus) -> {
            if (hasFocus) {
                startDatePicker.show(getParentFragmentManager(), "DATE_PICKER");
            }
        });

        startDatePicker.addOnPositiveButtonClickListener(selection -> {
            Date date = new Date((Long) startDatePicker.getSelection()) ;
            containerStartDate.setText(simpleFormat.format(date));
        });

        MaterialButton saveButton = v.findViewById(R.id.containerSaveButton);
        TextInputEditText containerName = v.findViewById(R.id.containerName);
        saveButton.setOnClickListener(v13 -> {

            if(Objects.requireNonNull(containerStartDate.getText()).toString().isEmpty() ||
                    Objects.requireNonNull(containerName.getText()).toString().isEmpty()) {
                dismiss();
                Toast.makeText(getContext(), "Fields must not be empty", Toast.LENGTH_SHORT).show();
            }
            else {
                try {
                    Container newContainer = new Container(Objects.requireNonNull
                            (containerName.getText()).toString(), true,
                             simpleFormat.parse(Objects.requireNonNull(containerStartDate.
                                     getText()).toString()),
                            null,
                            93L);

                    AppDatabase db = AppDatabase.getInstance(getContext());

                    Container inUseContainer = db.containerDao().getInUse();
                    if(inUseContainer != null)
                    {
                        inUseContainer.inUse = false;
                        try {
                            Date today = new Date();
                            inUseContainer.endDate = simpleFormat.parse(simpleFormat.format(today));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        db.containerDao().update(inUseContainer);
                    }

                    db.containerDao().insert(newContainer);
                    dismiss();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        return v;
    }

    public void setCalendar(){
        containerStartDate.setInputType(InputType.TYPE_NULL);
        containerStartDate.setKeyListener(null);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        long today = MaterialDatePicker.todayInUtcMilliseconds();

        MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Select start date");
        builder.setSelection(today);
        startDatePicker = builder.build();
    }

}
