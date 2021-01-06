package com.appnuggets.lensminder.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.appnuggets.lensminder.database.entity.Lenses;

import java.util.List;

@Dao
public interface LensesDao {
    @Query("SELECT * FROM lenses WHERE in_use = 1")
    Lenses getInUse();

    @Query("SELECT * FROM lenses WHERE in_use = 0")
    List<Lenses> getAllNotInUse();

    @Query("SELECT * FROM lenses")
    List<Lenses> getAll();

    @Insert
    void insert(Lenses lenses);

    @Update
    void update(Lenses lenses);

    @Delete
    void delete(Lenses lenses);
}
