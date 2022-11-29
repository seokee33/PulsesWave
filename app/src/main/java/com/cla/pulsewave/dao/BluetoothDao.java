package com.cla.pulsewave.dao;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.cla.pulsewave.datatype.BluetoothData;

import java.util.List;

@Dao
public interface BluetoothDao  {
    @Query("SELECT * FROM bluetooth")
    List<BluetoothData> getAll();

    @Insert(onConflict = REPLACE)
    void insert(BluetoothData bluetoothData);

    @Delete
    void delete(BluetoothData bluetoothData);

    @Delete
    void reset(List<BluetoothData> bluetoothData);

    @Query("UPDATE bluetooth SET device = :sDevice , write= :sWrite, read= :sRead WHERE num = :sID")
    void update(int sID, String sDevice, String sWrite, String sRead);
}