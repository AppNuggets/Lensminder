package com.appnuggets.lensminder.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "container")
public class Container {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "in_use")
    public Boolean inUse;

    @ColumnInfo(name = "expiration_date")
    public Date expirationDate;

    @ColumnInfo(name = "start_date")
    public Date startDate;

    @ColumnInfo(name = "use_interval")
    public Long useInterval;

    public Container(String name, Boolean inUse, Date expirationDate, Date startDate, Long useInterval) {
        this.name = name;
        this.inUse = inUse;
        this.expirationDate = expirationDate;
        this.startDate = startDate;
        this.useInterval = useInterval;
    }
}
