package com.cla.pulsewave.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.cla.pulsewave.dao.HistoryDao;
import com.cla.pulsewave.dataType.HistoryData;

@Database(entities = {HistoryData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HistoryDao historyDao();

}
