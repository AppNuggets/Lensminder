package com.appnuggets.lensminder.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.appnuggets.lensminder.database.entity.Container;

import java.util.List;

@Dao
public interface ContainerDao {
    @Query("SELECT * FROM container WHERE in_use = 1")
    Container getInUse();

    @Query("SELECT * FROM container WHERE in_use = 0")
    List<Container> getAllNotInUse();

    @Query("SELECT * FROM container")
    List<Container> getAll();

    @Insert
    void insert(Container container);

    @Update
    void update(Container container);

    @Delete
    void delete(Container container);
}
