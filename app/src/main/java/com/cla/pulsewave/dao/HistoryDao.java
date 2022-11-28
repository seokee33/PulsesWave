package com.cla.pulsewave.dao;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.cla.pulsewave.datatype.HistoryData;

import java.util.List;

@Dao
public interface HistoryDao {
    @Query("SELECT * FROM history")
    List<HistoryData> getAll();

    @Insert(onConflict = REPLACE)
    void insert(HistoryData historyData);

    @Delete
    void delete(HistoryData historyData);

    @Delete
    void reset(List<HistoryData> historyData);

    @Query("UPDATE history SET date = :sDate , userState= :sUserState, avgBPM= :sAvgBPM WHERE num = :sID")
    void update(int sID, String sDate, String sUserState, String sAvgBPM);
}
