package com.appnuggets.lensminder.service;

import android.graphics.Color;
import android.widget.TextView;

import com.appnuggets.lensminder.model.DateProcessor;
import com.appnuggets.lensminder.model.UsageProcessor;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.Date;
import java.util.Locale;

public class UpdateDisplayService {

    public static void updateProgressBar(CircularProgressBar progressBar,  TextView leftDaysView,
                                         Date expDate, Date startDate, Long useInterval) {
        UsageProcessor usageProcessor = new UsageProcessor();
        Long leftDays = usageProcessor.calculateUsageLeft(startDate, expDate, useInterval);

        progressBar.setProgressMax(useInterval);
        progressBar.setProgressWithAnimation( Math.max(leftDays, 0) , 1000L);

        leftDaysView.setText(String.format(Locale.getDefault(), "%d", leftDays));
        if( leftDays <= 0) {
            progressBar.setBackgroundProgressBarColor(Color.RED);
        }
        else {
            progressBar.setBackgroundProgressBarColor(0xffcfd8dc);
        }
    }

    public static void updateProgressBar(CircularProgressBar progressBar,  TextView leftDaysView) {
        progressBar.setBackgroundProgressBarColor(0xffcfd8dc);
        progressBar.setProgressMax(100f);
        progressBar.setProgress(0f);
        leftDaysView.setText("-");
    }

    public static void updateExpirationDate(TextView expDateView, Date expDate){
        DateProcessor dateProcessor = new DateProcessor();
        expDateView.setText(dateProcessor.dateToString(expDate));
    }

    public static void updateExpirationDate(TextView expDateView){
        expDateView.setText("-");
    }

    public static void updateDaysUsed(TextView daysUsedView, Date startDate){
        UsageProcessor usageProcessor = new UsageProcessor();
        Long daysUsed = usageProcessor.calculateCurrentUsage(startDate);
        daysUsedView.setText(String.format(Locale.getDefault(), "%d", daysUsed));
    }

    public static void updateDaysUsed(TextView daysUsedView ){
        daysUsedView.setText("-");
    }
}
