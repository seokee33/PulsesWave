package com.cla.pulsewave.util;

public class BluetoothModule {
    public static BluetoothModule _instance;
    public BluetoothModule(){}

    public static BluetoothModule getInstance(){
        if(_instance == null){
            synchronized (BluetoothModule.class){
                _instance = new BluetoothModule();
            }
        }
        return _instance;
    }
}
