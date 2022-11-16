package com.cla.pulsewave.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cla.pulsewave.dataType.HistoryData;

import java.util.List;

@Dao
public interface HistoryDao {
//    @Query("SELECT * FROM history")
//    List<HistoryData> getAll();

    @Insert
    void insertAll(HistoryData... historyData);
}
