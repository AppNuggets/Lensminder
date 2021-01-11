package com.appnuggets.lensminder.bottomsheet;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.appnuggets.lensminder.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.TimeZone;

public class ContainerBottomSheetDialog extends BottomSheetDialogFragment {

    private TextInputEditText containerStartDate;
    private MaterialDatePicker startDatePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.container_bottom_sheet_layout, container, false);

        containerStartDate = (TextInputEditText) v.findViewById(R.id.containerStartDate);
        setCalendar();

        containerStartDate.setOnClickListener(v1 -> startDatePicker.show(getFragmentManager(), "DATE_PICKER"));

        containerStartDate.setOnFocusChangeListener((v12, hasFocus) -> {
            if (hasFocus) {
                startDatePicker.show(getFragmentManager(), "DATE_PICKER");
            }
        });

        startDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                containerStartDate.setText(startDatePicker.getHeaderText());
            }
        });

        MaterialButton saveButton = (MaterialButton) v.findViewById(R.id.containerSaveButton);
        saveButton.setOnClickListener(v13 -> {
            //TODO
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
