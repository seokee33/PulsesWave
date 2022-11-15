package com.cla.pulsewave.util;

import java.util.HashMap;

public class SampleGattAttributes {
    private static HashMap<String, String> attributes = new HashMap();
    public static String BLE_UUID = "19b10000-e8f2-537e-4f6c-d104768a1214";
    public static String BLE_CLIENT_CHARACTERISTIC_READ = "19b10000-e8f2-537e-4f6c-d104768a1214";
    public static String BLE_CLIENT_CHARACTERISTIC_WRITE = "19b10001-e8f2-537e-4f6c-d104768a1214";

    static {
        // Services.
        attributes.put(BLE_UUID, "Heart Rate Service");
        attributes.put("0000180a-0000-1000-8000-00805f9b34fb", "Device Information Service");
        // Sample Characteristics.
        attributes.put(BLE_CLIENT_CHARACTERISTIC_READ, "CHARACTERISTIC_READ");
        attributes.put(BLE_CLIENT_CHARACTERISTIC_WRITE, "CHARACTERISTIC_WRITE");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}