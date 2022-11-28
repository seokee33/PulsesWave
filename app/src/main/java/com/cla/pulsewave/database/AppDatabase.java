package com.cla.pulsewave.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cla.pulsewave.dao.HistoryDao;
import com.cla.pulsewave.datatype.HistoryData;

@Database(entities = {HistoryData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase database;

    private static String DATABASE_NAME = "database";
    public synchronized static AppDatabase getInstance(Context context){
        if(database == null){
            database = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }
    public abstract HistoryDao historyDao();

}
