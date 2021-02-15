package com.appnuggets.lensminder.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.appnuggets.lensminder.database.entity.Lenses;

import java.util.List;

@Dao
public interface LensesDao {
    @Query("SELECT * FROM lenses WHERE state = 0")
    Lenses getInUse();

    @Query("SELECT * FROM lenses WHERE state = 1")
    List<Lenses> getAllInStock();

    @Query("SELECT * FROM lenses WHERE state = 2 ORDER BY start_date DESC")
    List<Lenses> getAllNotInUse();

    @Insert
    void insert(Lenses lenses);

    @Update
    void update(Lenses lenses);
}
