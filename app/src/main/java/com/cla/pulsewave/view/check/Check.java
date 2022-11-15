package com.cla.pulsewave.view.check;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.BIND_AUTO_CREATE;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cla.pulsewave.databinding.FragmentCheckBinding;
import com.cla.pulsewave.service.BluetoothLeService;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Check extends Fragment {
    private FragmentCheckBinding binding;

    public static Check newInstance() {
        Check check = new Check();
        return check;
    }

    //BLE Scan
    private final int BLE_RETURN = 100; //ScanDevice_Activity 로 부터 반환 코드

    //BLE Connect
    private String mDeviceAddress = "00:00:00:00:00:00";
    public static boolean BTstate = false; //블루투스 연결상태 : false로 초기화

    private BluetoothLeService mBluetoothLeService;


    //BLE Read/Write
    private Thread thread;

    private BluetoothGattCharacteristic BTcharacteristic_read; //연결된 기기로 부터 받은 데이터가 들어가는 변수
    private BluetoothGattCharacteristic click_ArrayList_data;

    //GATT
    private BluetoothGattServer server;
    private BluetoothGattService service;

    ////TEST
    private int cnt = 0;
    public boolean Read_State = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCheckBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //블루투스 수신 thread
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (BTstate) {
                    if (Read_State) {
                        Log.w("Read_State(onclick1)", Read_State + "");
                        BTcharacteristic_read = mBluetoothLeService.getSupportedGattServices_read();
                        getCharacteristics(); //데이터 찾는 순간, 받아오기 클릭
                        Log.w("Read_State(onclick1)", Read_State + "");
                    }
                }
            }
        });

        //블루투스 스캔하는 버튼을 클릭했을때
        binding.btnBLESCAN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(requireContext(), ScanDevice.class);
                intent.putExtra("key", "value");
                startActivityForResult(intent, BLE_RETURN);
            }
        });

        //측정하기
        binding.btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BTstate) {
                    // Log.w("et_send_input",et_send_input.getText().toString());
                    //byte[] sendHex = hexStringToByteArray(tv_Send.getText().toString());
                    byte[] sendHex = hexStringToByteArray("01");
                    Log.w("sendHex", String.valueOf(sendHex.length));
                    BluetoothGattCharacteristic BTcharacteristic_write = mBluetoothLeService.getSupportedGattServices_write();

                    mBluetoothLeService.writeCharacteristic(BTcharacteristic_write, sendHex); //TX
                    Read_State = true;
                } else {
                    Toast.makeText(getActivity(), "BLE연결을 확인하십시오.", Toast.LENGTH_LONG).show();
                }

            }
        });

        return view;
    }

    public byte[] hexStringToByteArray(String s)
    {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2)
        {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    /////////////////////////////////   연결 //////////////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
        {
            return;
        }
        //ScanDevice 클래스로 부터 값이 넘어오면 여기로
        if (requestCode == BLE_RETURN)
        {
            //ScanDevice에서 선택한 기기의 주소값을 가져옴
            mDeviceAddress = data.getStringExtra("key");
            BLE_connct(); // 선택한 기기와 연결
        }
    }

    // BluetoothLeSerivce클래스와 바인드되어 클라이언트 입장에서 실시간으로 상황받아옴
    private final ServiceConnection mServiceConnection = new ServiceConnection()
    {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service)
        {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService(); //BluetoothLeService클래스 인스턴스화
            //BluetoothLeService 클래스에서 가져오기를 실패하였을 때
            if (!mBluetoothLeService.initialize())
            {
                requireActivity().finish();
            }
            mBluetoothLeService.connect(mDeviceAddress); //첫연결할때 여기서 연결
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName)
        {
            mBluetoothLeService = null;
        }
    };

    // BluetoothLeService에서 sendBroadcast하면 전달받음
    private final BroadcastReceiver BLE_broadCastReciver = new BroadcastReceiver()
    {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent)
        {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) //기기연결이 성공했을때
            {
                connectUI();
            }
            else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) //기기가 연결되어있지 않을때
            {
                disconnectUI();
                BTstate = false;//블루투스 연결상태 : false(연결 해제)
                thread.stop();

            }
            else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) //서비스를 찾았을때 들어옴
            {
                BTstate = true; //블루투스 연결상태 : true(연결중)
                try{
                    thread.start();
                }catch (IllegalThreadStateException e){
                    Log.w("Thread Error","Thread Error");
                }
            }
            else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) //값을 찾았을때 들어옴
            {

                if(intent.getStringExtra(BluetoothLeService.EXTRA_DATA) != null)
                {
                    String getData = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                    Log.w("String getData = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);",getData+" : "+cnt++);
                }
            }
        }
    };

    //GATT UUID필터(bluetoothLeService.java에서 이것만 들어올수 있게)
    private static IntentFilter makeGattUpdateIntentFilter()
    {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    //블루투스 연결
    public void BLE_connct()
    {
        //두 개의 액티비티가 하나의 서비스의 데이터를 전달받는 것
        Intent gattServiceIntent = new Intent(getContext(), BluetoothLeService.class);
        requireActivity().bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE); // -> 7

        requireActivity().registerReceiver(BLE_broadCastReciver, makeGattUpdateIntentFilter());
        //폰 화면 꺼졋다가 연결할 시에 (pause()되었다가 연결함) 안들어감
        if (mBluetoothLeService != null)
        {
            mBluetoothLeService.disconnect(); //point
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
        }
    }

    //블루투스가 연결 실패 됐을때
    private void disconnectUI()
    {
        Log.w("disconnectUI","disconnectUI");
        mBluetoothLeService.disconnect(); //point
        binding.layoutBLEON.setVisibility(View.GONE);
        binding.layoutBLEOFF.setVisibility(View.VISIBLE);
    }

    //블루투스가 연결 됐을때
    private void connectUI()
    {
        binding.layoutBLEON.setVisibility(View.VISIBLE);
        binding.layoutBLEOFF.setVisibility(View.GONE);
    }

    ////////////////////////////////  Read Write  ///////////////////////////////

    //데이터 받아오기 클릭
    @Nullable
    private void getCharacteristics()
    {
        if(BTcharacteristic_read != null)
        {
            final BluetoothGattCharacteristic characteristic = BTcharacteristic_read;
            final int click_ArrayList_properties = characteristic.getProperties();
            if ((click_ArrayList_properties | BluetoothGattCharacteristic.PROPERTY_READ) > 0)
            {
                if (click_ArrayList_data != null)
                {
                    mBluetoothLeService.setCharacteristicNotification(click_ArrayList_data, false);
                    click_ArrayList_data = null;
                }
                mBluetoothLeService.readCharacteristic(characteristic);//아직 characteristic값 없음
                Log.w("Bluetooth1 : ","!!!!!!!!!!!!!!!!!");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.w("Bluetooth2 : ","!!!!!!!!!!!!!!!!!");
                        if( characteristic.getValue() != null){
                            Log.w("Bluetooth3 : ","!!!!!!!!!!!!!!!!!");
                            if(getDecimal(bytesToHex(characteristic.getValue()))== 11)
                                BTstate = false;
                            Log.w("Bluetooth3 : ", String.valueOf(getDecimal(bytesToHex(characteristic.getValue()))));
                        }

                    }
                });
            }
            if ((click_ArrayList_properties | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0)//읽을때 일로 들어옴
            {
                click_ArrayList_data = characteristic;
                mBluetoothLeService.setCharacteristicNotification(characteristic, true);
            }
        }
    }

    //bytesToHex : 바이트 배열을 16진수 문자열로 변화
    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);
    public static String bytesToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars, StandardCharsets.UTF_8);
    }

    public static int getDecimal(String hex){
        String digits = "0123456789ABCDEF";
        hex = hex.toUpperCase();
        int val = 0;
        for (int i = 0; i < hex.length(); i++)
        {
            char c = hex.charAt(i);
            int d = digits.indexOf(c);
            val = 16*val + d;
        }
        return val;
    }
}