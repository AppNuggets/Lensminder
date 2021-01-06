package com.appnuggets.lensminder.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.appnuggets.lensminder.database.dao.ContainerDao;
import com.appnuggets.lensminder.database.dao.DropsDao;
import com.appnuggets.lensminder.database.dao.LensesDao;
import com.appnuggets.lensminder.database.dao.SolutionDao;
import com.appnuggets.lensminder.database.entity.Container;
import com.appnuggets.lensminder.database.entity.Drops;
import com.appnuggets.lensminder.database.entity.Lenses;
import com.appnuggets.lensminder.database.entity.Solution;

@Database(entities = {Container.class, Drops.class, Lenses.class, Solution.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "lensminder.db";
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (null == instance) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DB_NAME).allowMainThreadQueries().build();
        }
        return instance;
    }
    public abstract ContainerDao containerDao();

    public abstract DropsDao dropsDao();

    public abstract LensesDao lensesDao();

    public abstract SolutionDao solutionDao();

}
