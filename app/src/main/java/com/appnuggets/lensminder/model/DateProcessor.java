package com.appnuggets.lensminder.model;

import java.util.Calendar;
import java.util.Date;

public class DateProcessor {

    public String dateToString(Date date) {
        if(date != null){
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            int year = cal.get(Calendar.YEAR);
            String sYear = Integer.toString(year);

            int month = cal.get(Calendar.MONTH) + 1;
            String sMonth;
            if(month < 10) sMonth = "0" + month;
            else sMonth =  Integer.toString(month);

            int day = cal.get(Calendar.DAY_OF_MONTH);
            String sDay;
            if(day < 10) sDay = "0" + day;
            else sDay =  Integer.toString(day);

            return sDay + "." + sMonth + "." + sYear;
        }
        return "";
    }
}
