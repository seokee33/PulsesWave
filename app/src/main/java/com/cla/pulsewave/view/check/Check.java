package com.cla.pulsewave.view.check;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.BIND_AUTO_CREATE;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cla.pulsewave.adapter.SparkChartAdapter;
import com.cla.pulsewave.database.BleDatabase;
import com.cla.pulsewave.databinding.FragmentCheckBinding;
import com.cla.pulsewave.datatype.BluetoothData;
import com.cla.pulsewave.dialog.BluetoothSettingDialog;
import com.cla.pulsewave.service.BluetoothLeService;
import com.cla.pulsewave.util.TextUtil;
import com.robinhood.spark.SparkView;
import com.robinhood.spark.animation.LineSparkAnimator;
import com.robinhood.spark.animation.MorphSparkAnimator;

import java.util.ArrayList;
import java.util.List;

public class Check extends Fragment {
    private FragmentCheckBinding binding;
    private BleDatabase database;
    private List<BluetoothData> list;

    public static Check newInstance() {
        Check check = new Check();
        return check;
    }

    //BLE Scan
    private final int BLE_RETURN = 100; //ScanDevice_Activity 로 부터 반환 코드

    //BLE Connect
    public static String mDeviceAddress = "00:00:00:00:00:00";
    public static boolean BTstate = false; //블루투스 연결상태 : false로 초기화
    private BluetoothLeService mBluetoothLeService;


    private BluetoothGattCharacteristic BTcharacteristic_read; //연결된 기기로 부터 받은 데이터가 들어가는 변수
    private BluetoothGattCharacteristic click_ArrayList_data;

    //chart
    private SparkView sparkView;
    private SparkChartAdapter sparkChartAdapter;

    //평균값
    int avg = 0;
    int avgCnt = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCheckBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        //블루투스 스캔하는 버튼을 클릭했을때
        binding.btnBLESCAN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = new ArrayList<>();
                database = BleDatabase.getInstance(getContext());
                list = database.bluetoothDao().getAll();
                if(list.size()>0){
                    Intent intent = new Intent(requireContext(), ScanDevice.class);
                    intent.putExtra("key", "value");
                    startActivityForResult(intent, BLE_RETURN);
                }else{
                    Toast.makeText(getActivity(),"UUID를 입력해주세요",Toast.LENGTH_SHORT).show();
                    BluetoothSettingDialog dialog = new BluetoothSettingDialog(getContext());
                    dialog.show();
                }

            }
        });

        //측정하기
        binding.btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BTstate) {
                    sendData();
                } else {
                    Toast.makeText(getActivity(), "BLE연결을 확인하십시오.", Toast.LENGTH_LONG).show();
                }
            }
        });


        //chart
        sparkChartAdapter = new SparkChartAdapter();

        return view;
    }

    /////////////////////////////////   연결 //////////////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        //ScanDevice 클래스로 부터 값이 넘어오면 여기로
        if (requestCode == BLE_RETURN) {
            //ScanDevice에서 선택한 기기의 주소값을 가져옴
            mDeviceAddress = data.getStringExtra("key");
            BLE_connct(); // 선택한 기기와 연결

        }
    }

    // BluetoothLeSerivce클래스와 바인드되어 클라이언트 입장에서 실시간으로 상황받아옴
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService(); //BluetoothLeService클래스 인스턴스화
            //BluetoothLeService 클래스에서 가져오기를 실패하였을 때
            if (!mBluetoothLeService.initialize(list.get(0).getDevice(),list.get(0).getWrite(),list.get(0).getRead())) {
                requireActivity().finish();
            }
            mBluetoothLeService.connect(mDeviceAddress); //첫연결할때 여기서 연결
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };

    // BluetoothLeService에서 sendBroadcast하면 전달받음
    private final BroadcastReceiver BLE_broadCastReciver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) //기기연결이 성공했을때
            {
                connectUI();
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) //기기가 연결되어있지 않을때
            {
                disconnectUI();
                BTstate = false;//블루투스 연결상태 : false(연결 해제)

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) //서비스를 찾았을때 들어옴
            {
                BTstate = true; //블루투스 연결상태 : true(연결중)
                readData();
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) //값을 찾았을때 들어옴
            {

                if (intent.getStringExtra(BluetoothLeService.EXTRA_DATA) != null) {
                    String getData = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);
                    if(!getData.equals("0")){
                        try{
                            requireActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    binding.tvBPM.setText(String.valueOf(calcAVG(Integer.parseInt(getData))));
                                }
                            });
                            binding.chartPulse.setAdapter(sparkChartAdapter);
                            sparkChartAdapter.addData(Float.parseFloat(getData));
                            sparkChartAdapter.notifyDataSetChanged();
                        }catch (RuntimeException e){
                            Log.w("error_avg","error");
                        }
                    }
                    Log.w("String getData = intent.getStringExtra(BluetoothLeService.EXTRA_DATA);", getData);
                }
            }
        }
    };

    private int calcAVG(int value){
        avg = (value+(++avgCnt-1)*avg)/avgCnt;
        return avg;
    }
    //블루투스 연결
    public void BLE_connct() {
        //두 개의 액티비티가 하나의 서비스의 데이터를 전달받는 것
        Intent gattServiceIntent = new Intent(getContext(), BluetoothLeService.class);
        requireActivity().bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE); // -> 7

        requireActivity().registerReceiver(BLE_broadCastReciver, makeGattUpdateIntentFilter());
        //폰 화면 꺼졋다가 연결할 시에 (pause()되었다가 연결함) 안들어감
        if (mBluetoothLeService != null) {
            mBluetoothLeService.disconnect(); //point
            final boolean result = mBluetoothLeService.connect(mDeviceAddress);
        }
    }

    //블루투스가 연결 실패 됐을때
    private void disconnectUI() {
        mBluetoothLeService.disconnect(); //point
        binding.layoutBLEON.setVisibility(View.GONE);
        binding.layoutBLEOFF.setVisibility(View.VISIBLE);
    }

    //블루투스가 연결 됐을때
    private void connectUI() {
        binding.layoutBLEON.setVisibility(View.VISIBLE);
        binding.layoutBLEOFF.setVisibility(View.GONE);
        avg = 0;
        binding.tvBPM.setText("");
    }

    ////////////////////////////////  Read Write  ///////////////////////////////
    //데이터 받아오기 클릭
    @Nullable
    private void getCharacteristics() {
        if (BTcharacteristic_read != null) {
            final BluetoothGattCharacteristic characteristic = BTcharacteristic_read;
            final int click_ArrayList_properties = characteristic.getProperties();
            if ((click_ArrayList_properties | BluetoothGattCharacteristic.PROPERTY_READ) > 0) {
                if (click_ArrayList_data != null) {
                    Log.w("getCharacteristics_read", "ininininin");
                    mBluetoothLeService.setCharacteristicNotification(click_ArrayList_data, false);
                    click_ArrayList_data = null;
                }
                mBluetoothLeService.readCharacteristic(characteristic); //아직 characteristic값 없음
            }
            if ((click_ArrayList_properties | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0)//읽을때 일로 들어옴
            {
                Log.w("getCharacteristics_Notify", "inininininin");
                click_ArrayList_data = characteristic;

                //delay를 줘야하는것 같음!!!!!
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBluetoothLeService.setCharacteristicNotification(characteristic, true);
                    }
                }, 3000);
            }
        }
    }

    public void sendData() {
        byte[] sendHex = TextUtil.getInstance().hexStringToByteArray("01");
        Log.w("sendHex", String.valueOf(sendHex.length));
        BluetoothGattCharacteristic BTcharacteristic_write = mBluetoothLeService.getSupportedGattServices_write();

        mBluetoothLeService.writeCharacteristic(BTcharacteristic_write, sendHex); //TX
    }

    public void readData() {
        BTcharacteristic_read = mBluetoothLeService.getSupportedGattServices_read(); //변경
        getCharacteristics(); //데이터 찾는 순간, 받아오기 클릭
    }

    //GATT UUID필터(bluetoothLeService.java에서 이것만 들어올수 있게)
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}