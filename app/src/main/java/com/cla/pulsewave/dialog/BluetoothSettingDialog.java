package com.cla.pulsewave.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cla.pulsewave.R;
import com.cla.pulsewave.database.BleDatabase;
import com.cla.pulsewave.datatype.BluetoothData;

import java.util.ArrayList;
import java.util.List;

public class BluetoothSettingDialog extends Dialog implements View.OnClickListener {

//    private BluetoothSettingDialogListener bluetoothSettingDialogListener;

    private Button btn_OK;
    private Button btn_Cancel;

    private EditText et_DeviceUUID;
    private EditText et_WriteUUID;
    private EditText et_ReadUUID;

    private BleDatabase database;
    private List<BluetoothData> list;
    private boolean isEmpty = false;

    public BluetoothSettingDialog(@NonNull Context context) {
        super(context);
    }


//    //인터페이스 설정
//    interface BluetoothSettingDialogListener {
//        void onPositiveClicked(String result);
//
//        void onNegativeClicked();
//    }
//
//    //호출할 리스너 초기화
//    public void setDialogListener(BluetoothSettingDialogListener bluetoothSettingDialogListener) {
//        this.bluetoothSettingDialogListener = bluetoothSettingDialogListener;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_setting_dialog);

        list = new ArrayList<>();
        database = BleDatabase.getInstance(getContext());
        list = database.bluetoothDao().getAll();
        if (list.size() > 0) {
            isEmpty = false;
        } else {
            isEmpty = true;
        }
        btn_OK = findViewById(R.id.btn_OK);
        btn_Cancel = findViewById(R.id.btn_Cancel);
        et_DeviceUUID = findViewById(R.id.et_DeviceUUID);
        et_WriteUUID = findViewById(R.id.et_WriteUUID);
        et_ReadUUID = findViewById(R.id.et_ReadUUID);

        //버튼 클릭 리스너 등록
        btn_OK.setOnClickListener(this);
        btn_Cancel.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_OK:
                String device = et_DeviceUUID.getText().toString();
                String write = et_WriteUUID.getText().toString();
                String read = et_ReadUUID.getText().toString();
                if (isEmpty) {
                    database.bluetoothDao().insert(new BluetoothData(device,write,read));
                } else {
                    database.bluetoothDao().update(0,device,write,read);
                }
                dismiss();
                break;
            case R.id.btn_Cancel:
                dismiss();
                break;
        }
    }


}