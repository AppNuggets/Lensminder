package com.appnuggets.lensminder.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.appnuggets.lensminder.database.entity.Solution;

import java.util.List;

@Dao
public interface SolutionDao {
    @Query("SELECT * FROM solution WHERE in_use = 1")
    Solution getInUse();

    @Query("SELECT * FROM solution WHERE in_use = 0 ORDER BY start_date DESC")
    List<Solution> getAllNotInUse();

    @Query("SELECT * FROM solution")
    List<Solution> getAll();

    @Insert
    void insert(Solution solution);

    @Update
    void update(Solution solution);

    @Delete
    void delete(Solution solution);
}
