package com.example.sensor;



import androidx.appcompat.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.content.pm.ActivityInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    LinearLayout layoutButtonsLR, layoutTextLR;
    ArrayAdapter<String> arrayAdapterPairedDeviceList;
    String stringSelectedDevice, stringSelectedDeviceName;
    ListView listPairedDevices;
    Button btnGetListOfPaired, btnChoseDevice_Left, btnChoseDevice_Right, btnLastSetup, btnContinueNextActivity, btnChoseDeviceCount_Single_left,btnChoseDeviceCount_Single_right, btnChoseDeviceCount_Multiple;
    TextView textStatusLeft, textStatusRight, bugtracker;
    BluetoothAdapter mBlueAdapter;
    Set<BluetoothDevice> pairedDevices;
    ArrayAdapter<String> savedList;
    Intent macIntent, settingsIntent;
    boolean rightOperation , leftOperation;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // always portrait



        //text
        bugtracker = findViewById(R.id.bugtrack);
        textStatusLeft = findViewById(R.id.txt_statusLeft);
        textStatusRight = findViewById(R.id.txt_statusRight);
        //buttons
        btnChoseDeviceCount_Single_left = findViewById(R.id.btn_single_left);
        btnChoseDeviceCount_Single_right = findViewById(R.id.btn_single_right);
        btnChoseDeviceCount_Multiple = findViewById(R.id.btn_multi);
        btnGetListOfPaired = findViewById(R.id.btn_paired);
        btnChoseDevice_Left = findViewById(R.id.btn_left);
        btnChoseDevice_Right = findViewById(R.id.btn_right);
        btnContinueNextActivity = findViewById(R.id.btn_connect);
        btnLastSetup = findViewById(R.id.btnLastSetup);
        //list
        listPairedDevices = findViewById(R.id.pairedDevices);
        //linear layout
        layoutButtonsLR = findViewById(R.id.layoutButtonsLeftRight);
        layoutTextLR = findViewById(R.id.layoutTextLeftRight);






        arrayAdapterPairedDeviceList = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        settingsIntent = new Intent(this, visual.class);
        macIntent = new Intent(this, Config.class);


        savedList = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        //adapter
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();

        //choose single or multiple devices



        btnChoseDeviceCount_Single_left.setOnClickListener(v -> {
            //  i/o interface
//            btnChoseDeviceCount_Single_left.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_primary));
//            btnChoseDeviceCount_Single_right.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_secondary));
//            btnChoseDeviceCount_Multiple.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_secondary));
            btnChoseDevice_Left.setVisibility(View.VISIBLE);
            btnChoseDevice_Right.setVisibility(View.GONE);
            textStatusLeft.setVisibility(View.VISIBLE);
            textStatusRight.setVisibility(View.GONE);
            leftOperation = false;
            rightOperation = true;
            macIntent.putExtra("setup", "left");
            // next bar visible
            btnGetListOfPaired.setVisibility(View.VISIBLE);
            //new setup
            btnLastSetup.setVisibility(View.GONE);
            });

        btnChoseDeviceCount_Single_right.setOnClickListener(v -> {
//            //  i/o interface
//            btnChoseDeviceCount_Single_right.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_primary));
//            btnChoseDeviceCount_Single_left.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_secondary));
//            btnChoseDeviceCount_Multiple.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_secondary));
            btnChoseDevice_Left.setVisibility(View.GONE);
            btnChoseDevice_Right.setVisibility(View.VISIBLE);
            textStatusLeft.setVisibility(View.GONE);
            textStatusRight.setVisibility(View.VISIBLE);
            rightOperation = false;
            leftOperation = true;
            macIntent.putExtra("setup", "right");
            // next bar visible
            btnGetListOfPaired.setVisibility(View.VISIBLE);
            //new setup
            btnLastSetup.setVisibility(View.GONE);
            });

        btnChoseDeviceCount_Multiple.setOnClickListener(v -> {
//            //  i/o interface
//            btnChoseDeviceCount_Multiple.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_primary));
//            btnChoseDeviceCount_Single_left.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_secondary));
//            btnChoseDeviceCount_Single_right.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_secondary));
            btnChoseDevice_Left.setVisibility(View.VISIBLE);
            btnChoseDevice_Right.setVisibility(View.VISIBLE);
            textStatusLeft.setVisibility(View.VISIBLE);
            textStatusRight.setVisibility(View.VISIBLE);
            leftOperation = false;
            rightOperation = false;
            macIntent.putExtra("setup", "both");
            // next bar visible
            btnGetListOfPaired.setVisibility(View.VISIBLE);
            //new setup
            btnLastSetup.setVisibility(View.GONE);
        });

        //get bt devices
        btnGetListOfPaired.setOnClickListener(v -> {

            layoutTextLR.setVisibility(View.VISIBLE);
            layoutButtonsLR.setVisibility(View.VISIBLE);

            if (savedList!=null) {
                savedList.clear();
                savedList.notifyDataSetChanged();
            }
//
            pairedDevices = mBlueAdapter.getBondedDevices();


            for (BluetoothDevice bt : pairedDevices) {
                String deviceName = bt.getName();
                String macAddress = bt.getAddress();
                savedList.add("Device name: " + deviceName + "\nMAC Address: " + macAddress);
            }

//
//
            listPairedDevices.setAdapter(savedList);
        });





        btnChoseDevice_Left.setOnClickListener(v -> {
            if(stringSelectedDevice!=null){

                    macIntent.putExtra("device_left_mac", stringSelectedDevice);
                    macIntent.putExtra("device_left_name", stringSelectedDeviceName);
                    textStatusLeft.setText(stringSelectedDeviceName);
                    leftOperation = true;
                    if (leftOperation && rightOperation) {
                        btnContinueNextActivity.setVisibility(View.VISIBLE);
                    }

            }
        });

       btnChoseDevice_Right.setOnClickListener(v -> {
           if(stringSelectedDevice!=null){

                   macIntent.putExtra("device_right_mac", stringSelectedDevice);
                   macIntent.putExtra("device_right_name", stringSelectedDeviceName);
                   textStatusRight.setText(stringSelectedDeviceName);
                   rightOperation = true;
                   if (leftOperation && rightOperation) {
                       btnContinueNextActivity.setVisibility(View.VISIBLE);
                   }

           }
        });






        listPairedDevices.setOnItemClickListener((parent, view, position, id) -> {

            String s = listPairedDevices.getItemAtPosition(position).toString();
                stringSelectedDeviceName = s.substring(13,s.length()-30);
                stringSelectedDevice = s.substring(s.length()-17);

        });

        btnContinueNextActivity.setOnClickListener(v -> {
            startActivity(macIntent);
        });

        btnLastSetup.setOnClickListener(v -> {
            settingsIntent.putExtra("setup", "left");

            settingsIntent.putExtra("device_left_mac", "00:16:A4:48:8E:E6");
            settingsIntent.putExtra("device_left_name", "Block 3");

            settingsIntent.putExtra("settings_BTS1", "5");
            settingsIntent.putExtra("settings_BTS2", "2");//0 = 2k, 9 = 1024k

            startActivity(settingsIntent);

        });



    }
}
