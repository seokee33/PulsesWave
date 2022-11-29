package com.cla.pulsewave.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.cla.pulsewave.dao.BluetoothDao;
import com.cla.pulsewave.datatype.BluetoothData;

@Database(entities = {BluetoothData.class}, version = 1)
public abstract class BleDatabase extends RoomDatabase {

    private static BleDatabase database;

    private static String DATABASE_NAME = "bleDatabase";
    public synchronized static BleDatabase getInstance(Context context){
        if(database == null){
            database = Room.databaseBuilder(context.getApplicationContext(), BleDatabase.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract BluetoothDao bluetoothDao();
}
