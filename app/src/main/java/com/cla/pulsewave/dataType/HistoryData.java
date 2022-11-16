package com.cla.pulsewave.dataType;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class HistoryData {
    @PrimaryKey
    public int num;

    public String date;
    public String userState;
    public String avgBPM;
}
