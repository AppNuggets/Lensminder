package com.appnuggets.lensminder.model;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UsageProcessor {

    public Long calculateCurrentUsage(Date startDate) {
        Date now = Calendar.getInstance().getTime();
        long diffInMillis = now.getTime() - startDate.getTime();
        /* If resource is scheduled for future use return 0 instead of negative count to start */
        if (diffInMillis < 0) {
            diffInMillis = 0;
        }
        return TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
    }

    public Long calculateUsageLeft(Date startDate, Date expirationDate, Long useInterval ){
        Date now = Calendar.getInstance().getTime();
        long daysLeftForUsage = useInterval - calculateCurrentUsage(startDate);
        if( null != expirationDate ) {
            long daysToExpiration =  TimeUnit.DAYS.
                    convert((expirationDate.getTime() - now.getTime()), TimeUnit.MILLISECONDS);
            return Math.min(daysToExpiration, daysLeftForUsage);
        }
        else {
            return daysLeftForUsage;
        }
    }

    public Date calculateEndDate(Date startDate, Date expirationDate, Long useInterval){
        Date now = Calendar.getInstance().getTime();
        Long daysLeft = calculateUsageLeft(startDate, expirationDate, useInterval);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, daysLeft.intValue());

        return calendar.getTime();
    }

}
