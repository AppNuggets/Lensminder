package com.appnuggets.lensminder.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "drops")
public class Drops {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public final String name;

    @ColumnInfo(name = "in_use")
    public Boolean inUse;

    @ColumnInfo(name = "expiration_date")
    public final Date expirationDate;

    @ColumnInfo(name = "start_date")
    public final Date startDate;

    @ColumnInfo(name = "end_date")
    public Date endDate;

    @ColumnInfo(name = "use_interval")
    public final Long useInterval;

    public Drops(String name, Boolean inUse, Date expirationDate, Date startDate, Date endDate,
                 Long useInterval) {
        this.name = name;
        this.inUse = inUse;
        this.expirationDate = expirationDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.useInterval = useInterval;
    }
}
