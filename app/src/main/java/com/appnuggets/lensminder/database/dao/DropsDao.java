package com.appnuggets.lensminder.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.appnuggets.lensminder.database.entity.Drops;

import java.util.List;

@Dao
public interface DropsDao {
    @Query("SELECT * FROM drops WHERE in_use = 1")
    Drops getInUse();

    @Query("SELECT * FROM drops WHERE in_use = 0")
    List<Drops> getAllNotInUse();

    @Query("SELECT * FROM drops")
    List<Drops> getAll();

    @Insert
    void insert(Drops drops);

    @Update
    void update(Drops drops);

    @Delete
    void delete(Drops drops);
}
