package com.example.sensor;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class old extends AppCompatActivity {

//    private ListView pairedDeviceList;
//    private ArrayAdapter<String> pairedDeviceListAdapter;
//
//    private TextView txt_deviceLeft;
//    private TextView txt_deviceRight;
//    private boolean isSelectedLeft = false;
//    private boolean isSelectedRight = false;
//
//    private BluetoothAdapter myBluetoothAdapter;
//    private Set<BluetoothDevice> pairedDevices;
//    private String selectedDevice;
//
//    public Intent myIntent;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // always portrait so we don't have to care about rotation destroying our app
//
//
//
//        txt_deviceLeft = findViewById(R.id.txt_left_selected);
//        txt_deviceRight = findViewById(R.id.txt_right_selected);
//
//        Button btn_bluetooth_on = findViewById(R.id.btn_bt_on);
//        btn_bluetooth_on.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!myBluetoothAdapter.isEnabled()) {
//                    Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    startActivityForResult(turnOn, 0);
//                    Toast.makeText(getApplicationContext(), "Turned on",Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Already on", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//        Button btn_bluetooth_off = findViewById(R.id.btn_bt_off);
//        btn_bluetooth_off.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myBluetoothAdapter.disable();
//                Toast.makeText(getApplicationContext(), "Turned off" ,Toast.LENGTH_LONG).show();
//            }
//        });
//
//        Button btn_show_paired = findViewById(R.id.btn_show_paired);
//        btn_show_paired.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if(!pairedDeviceListAdapter.isEmpty()){
//                    pairedDeviceListAdapter.clear();
//                    pairedDeviceListAdapter.notifyDataSetChanged();
//                }
//
//                pairedDevices = myBluetoothAdapter.getBondedDevices();
//
//                for(BluetoothDevice device : pairedDevices) {
//                    String deviceName = device.getName();
//                    String macAddress = device.getAddress();
//                    pairedDeviceListAdapter.add("Name: " + deviceName + "\nMAC Address: " + macAddress);
//                }
//
//                pairedDeviceList.setAdapter(pairedDeviceListAdapter);
//            }
//        });
//
//        Button btn_selectLeft = findViewById(R.id.btn_select_left);
//        btn_selectLeft.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(selectedDevice == null){
//                    Toast.makeText(getApplicationContext(), "No device selected" ,Toast.LENGTH_LONG).show();
//                } else {
//                    myIntent.putExtra("device_left",selectedDevice);
//                    txt_deviceLeft.setText(selectedDevice);
//                    isSelectedLeft = true;
//                }
//            }
//        });
//
//        Button btn_selectRight = findViewById(R.id.btn_select_right);
//        btn_selectRight.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(selectedDevice == null){
//                    Toast.makeText(getApplicationContext(), "No device selected" ,Toast.LENGTH_LONG).show();
//                } else {
//                    myIntent.putExtra("device_right",selectedDevice);
//                    txt_deviceRight.setText(selectedDevice);
//                    isSelectedRight = true;
//                }
//            }
//        });
//
//        Button btn_connect = findViewById(R.id.btn_connect);
//        btn_connect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // activate bluetooth, we will need it
//                if (!myBluetoothAdapter.isEnabled()) {
//                    Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                    startActivityForResult(turnOn, 0);
//                    Toast.makeText(getApplicationContext(), "Turning on Bluetooth",Toast.LENGTH_LONG).show();
//                }
//                // make sure that both devices have been selected
//                if(!isSelectedLeft || !isSelectedRight){
//                    Toast.makeText(getApplicationContext(), "Select both devices to continue",Toast.LENGTH_LONG).show();
//                } else { // start new intent
//                    startActivity(myIntent);
//                }
//
//            }
//        });
//
//        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        pairedDeviceListAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
//
//        pairedDeviceList = findViewById(R.id.pairedDevices);
//        pairedDeviceList.setAdapter(pairedDeviceListAdapter);
//        pairedDeviceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String s = pairedDeviceList.getItemAtPosition(position).toString();
//                selectedDevice = s.substring(s.length()-17);
//            }
//        });
//    }
}
