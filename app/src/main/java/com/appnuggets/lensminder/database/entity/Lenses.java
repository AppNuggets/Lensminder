package com.appnuggets.lensminder.database.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "lenses")
public class Lenses {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "state")
    public State state;

    @ColumnInfo(name = "expiration_date")
    public Date expirationDate;

    @ColumnInfo(name = "start_date")
    public Date startDate;

    @ColumnInfo(name = "use_interval")
    public Long useInterval;

    public Lenses(String name, State state, Date expirationDate, Date startDate, Long useInterval) {
        this.name = name;
        this.state = state;
        this.expirationDate = expirationDate;
        this.startDate = startDate;
        this.useInterval = useInterval;
    }
}
