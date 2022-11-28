package com.cla.pulsewave.datatype;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history")
public class HistoryData {
    @PrimaryKey(autoGenerate = true)
    private int num;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "userState")
    private String userState;

    @ColumnInfo(name = "avgBPM")
    private String avgBPM;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public String getAvgBPM() {
        return avgBPM;
    }

    public void setAvgBPM(String avgBPM) {
        this.avgBPM = avgBPM;
    }
}
