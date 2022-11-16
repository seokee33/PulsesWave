package com.cla.pulsewave.adapter;

import androidx.annotation.NonNull;

import com.robinhood.spark.SparkAdapter;

import java.util.Random;

public class SparkChartAdapter extends SparkAdapter {
    private int cnt;
    private final float[] yData;

    public SparkChartAdapter() {
        yData = new float[61];
        for(int i =0; i<yData.length;i++){
            yData[i] = 80.f;
        }
        cnt = 0;
    }
    public void addData(float data){
        yData[cnt++] = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return yData.length;
    }

    @NonNull
    @Override
    public Object getItem(int index) {
        return yData[index];
    }

    @Override
    public float getY(int index) {
        return yData[index];
    }
}