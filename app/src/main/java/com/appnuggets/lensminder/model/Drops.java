package com.appnuggets.lensminder.model;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Drops {

    private final Date expirationDate;
    private final Date openDate;
    private final Integer duration;
    private final String name;

    public Drops(Date expirationDate, Date openDate, Integer duration, String name){
        this.expirationDate = expirationDate;
        this.openDate = openDate;
        this.duration = duration;
        this.name = name;
    }

    public String getExpirationDateString() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(expirationDate);

        int year = cal.get(Calendar.YEAR);
        String sYear = Integer.toString(year);

        int month = cal.get(Calendar.MONTH);
        String sMonth;
        if(month < 10) sMonth = "0" + month;
        else sMonth =  Integer.toString(month);

        int day = cal.get(Calendar.DAY_OF_MONTH);
        String sDay;
        if(day < 10) sDay = "0" + day;
        else sDay =  Integer.toString(day);

        return sDay + "." + sMonth + "." + sYear;
    }

    /* Returns max number of days solution can be used for*/
    public Integer getDuration() {
        return duration;
    }

    /* Returns real number of days solution can be used for */
    public Long getUsageLeft() {
        Date now = Calendar.getInstance().getTime();

        long daysToExpiration =  TimeUnit.DAYS.convert((expirationDate.getTime() - now.getTime()),
                TimeUnit.MILLISECONDS);
        long daysLeftForUsage = duration - getCurrentUsage();

        return Math.min(daysToExpiration, daysLeftForUsage);
    }

    /*Returns number of days solution has been used for*/
    public long getCurrentUsage() {
        Date now = Calendar.getInstance().getTime();
        long diffInMillis = now.getTime() - openDate.getTime();
        if (diffInMillis < 0) {
            diffInMillis = 0;
        }
        return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public String getOpenDateString() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(openDate);

        int year = cal.get(Calendar.YEAR);
        String sYear = Integer.toString(year);

        int month = cal.get(Calendar.MONTH);
        String sMonth;
        if(month < 10) sMonth = "0" + month;
        else sMonth =  Integer.toString(month);

        int day = cal.get(Calendar.DAY_OF_MONTH);
        String sDay;
        if(day < 10) sDay = "0" + day;
        else sDay =  Integer.toString(day);

        return sDay + "." + sMonth + "." + sYear;
    }

    public String getName() {
        return name;
    }
}
