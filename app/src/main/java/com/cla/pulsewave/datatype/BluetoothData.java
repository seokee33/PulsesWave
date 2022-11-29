package com.cla.pulsewave.datatype;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "bluetooth")
public class BluetoothData implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int Num;

    public BluetoothData(String device, String write, String read) {
        this.device = device;
        this.write = write;
        this.read = read;
    }

    @ColumnInfo(name = "device")
    private String device;

    @ColumnInfo(name = "write")
    private String write;

    @ColumnInfo(name = "read")
    private String read;

    public int getNum() {
        return Num;
    }

    public void setNum(int num) {
        Num = num;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getWrite() {
        return write;
    }

    public void setWrite(String write) {
        this.write = write;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }
}
