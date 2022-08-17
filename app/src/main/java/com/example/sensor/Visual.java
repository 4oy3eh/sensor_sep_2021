package com.example.sensor;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.FileOutputStream;
import java.lang.reflect.Method;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.widget.Toast;

import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.graphics.Canvas;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;


import android.content.pm.ActivityInfo;

import java.io.File;
import java.util.Date;
import java.util.Locale;

//canvas
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.PrintWriter;


public class Visual extends AppCompatActivity {
    boolean isLeft, isCalib = false, first_time_canvas = true;
    Button btnValues, btnCop, btnReconnect, btnPlay, btnTest, btnGraph, btnCopCalib, btnSaveData,
            btnBreak;
    TextView bugtracker, textSensor1, textSensor2, textSensor3, textSensor4, textSensor5,
            textSensor6;
    ImageView imgCop;
    ImageView imageLeftArrow, imageRightArrow, imageDownArrow, imageFeetOverlay, imageSensor1G,
            imageSensor1Y, imageSensor1R, imageSensor2G, imageSensor2Y, imageSensor2R,
            imageSensor3G, imageSensor3Y, imageSensor3R, imageSensor4G, imageSensor4Y,
            imageSensor4R, imageSensor5G, imageSensor5Y, imageSensor5R, imageSensor6G,
            imageSensor6Y, imageSensor6R;
    Intent settingsIntent, nextIntent;
    String mac, bts1, bts2, setup, measurementRange, samplingRate;
    BluetoothDevice device;
    BluetoothSocket socket;
    BluetoothAdapter mBlueAdapter;
    ConnectedThread thread;
    PrintWriter tempData;
    int refreshSpeed;
    double resistanceMultiplier, maxValue, sensor1, sensor2, sensor3, sensor4, sensor5, sensor6, cop_sensor1,
            cop_sensor2, cop_sensor3, cop_sensor4, cop_sensor5, cop_sensor6, calib_sensor1,
            calib_sensor2, calib_sensor3, calib_sensor4, calib_sensor5, calib_sensor6;
    double tmpArray[], tmpArrayTwo[];
    float xValue, yValue, xMiddle, yMiddle, xValuePixel, yValuePixel, xValueOld, yValueOld;
    ArrayList<Float> List_xValue = new ArrayList<Float>();
    ArrayList<Float> List_yValue = new ArrayList<Float>();
    float Array_y_5[] = new float[5];
    float Array_y_30[] = new float[30];
    float Array_x_5[] = {0, 0, 0, 0, 0};
    float Array_x_30[] = new float[30];
    int deviceHeight, deviceWidth, imageHeight, imageWidth;
    FileOutputStream fos;
    Handler bluetoothIn;
    Thread forRefresh;
    //for categories and refresh(showdata)
    boolean checkVisualOn = false, checkDataOn = false, checkSavingOn = false;
    int sleepTime = 100; //ms

    Sensor tmpSensor;
    //save
    String filename = "", filepath = "SavedData";

    String startStream = "BT^START\r";
    String resetTimestamp = "BT^TRES\r";
    String stopStream = "BT^STOP\r";
    String[] setSpeedHz = new String[]{"BTS1=0\r", "BTS1=1\r", "BTS1=2\r", "BTS1=3\r", "BTS1=4\r", "BTS1=5\r", "BTS1=6\r", "BTS1=7\r", "BTS1=8\r"};
    String[] setResistanceKHom = new String[]{"BTS2=0\r", "BTS2=1\r", "BTS2=2\r", "BTS2=3\r", "BTS2=4\r", "BTS2=5\r", "BTS2=6\r", "BTS2=7\r", "BTS2=8\r", "BTS2=9\r"};
    double[] resistanceMultiplierArray = new double[]{0.03125, 0.063, 0.125, 0.25, 0.50, 1.00, 2.00, 4.00, 8.00, 16.00};
    int[] maxValueArray = new int[]{2000, 4000, 8000, 16000, 32000, 64000, 128000, 256000, 512000, 1024000};


    // Set FIFO capacity on DataSeries
    int sideSelection = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visual);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // always portrait
        //intent first to understand which images to use
        settingsIntent = getIntent();
        setup = settingsIntent.getStringExtra("setup");
        //test
        //TODO
        //buttons
        btnBreak = findViewById(R.id.btn_break);
        btnSaveData = findViewById(R.id.btn_savedata);
        btnGraph = findViewById(R.id.btn_graph);
        btnPlay = findViewById(R.id.play);
        btnValues = findViewById(R.id.btn_visual);
        btnCop = findViewById(R.id.btn_cop);
        btnReconnect = findViewById(R.id.btn_reconnect);
        btnCopCalib = findViewById(R.id.btn_calib_cop);
        //images
        imageLeftArrow = findViewById(R.id.image_leftarrow);
        imageRightArrow = findViewById(R.id.image_rightarrow);
        imageDownArrow = findViewById(R.id.image_downarrow);
        imgCop = findViewById(R.id.imageCop_l);
        imageFeetOverlay = findViewById(R.id.image_first_l);
        imageSensor1G = findViewById(R.id.image_1sensor_g_l);
        imageSensor1Y = findViewById(R.id.image_1sensor_y_l);
        imageSensor1R = findViewById(R.id.image_1sensor_r_l);
        imageSensor2G = findViewById(R.id.image_2sensor_g_l);
        imageSensor2Y = findViewById(R.id.image_2sensor_y_l);
        imageSensor2R = findViewById(R.id.image_2sensor_r_l);
        imageSensor3G = findViewById(R.id.image_3sensor_g_l);
        imageSensor3Y = findViewById(R.id.image_3sensor_y_l);
        imageSensor3R = findViewById(R.id.image_3sensor_r_l);
        imageSensor4G = findViewById(R.id.image_4sensor_g_l);
        imageSensor4Y = findViewById(R.id.image_4sensor_y_l);
        imageSensor4R = findViewById(R.id.image_4sensor_r_l);
        imageSensor5G = findViewById(R.id.image_5sensor_g_l);
        imageSensor5Y = findViewById(R.id.image_5sensor_y_l);
        imageSensor5R = findViewById(R.id.image_5sensor_r_l);
        imageSensor6G = findViewById(R.id.image_6sensor_g_l);
        imageSensor6Y = findViewById(R.id.image_6sensor_y_l);
        imageSensor6R = findViewById(R.id.image_6sensor_r_l);

        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
        nextIntent = new Intent(this, Visual.class);
        deviceWidth = getDeviceWidth();
        deviceHeight = getDeviceHeight();

        nextIntent.putExtra("setup", setup);

        btnCopCalib.setVisibility(View.INVISIBLE);

        VisualMainButtonsVisibility(View.GONE);

        //get intent+autoconnect
        bts1 = settingsIntent.getStringExtra("settings_BTS1");
        bts2 = settingsIntent.getStringExtra("settings_BTS2");

        //Common things
        mac = settingsIntent.getStringExtra("device_mac");

        if (setup.equals("left")) {
            isLeft = true;
            btnReconnect.setText("L");

            //sensor values for left
            textSensor1 = findViewById(R.id.textsensor1);
            textSensor2 = findViewById(R.id.textsensor2);
            textSensor3 = findViewById(R.id.textsensor3);
            textSensor4 = findViewById(R.id.textsensor4);
            textSensor5 = findViewById(R.id.textsensor5);
            textSensor6 = findViewById(R.id.textsensor6);
        } else { //is right
            isLeft = false;
            //TODO
            //Test  R / r / l / L / Left / Right / etc
            btnReconnect.setText("R");
            // Next portion is:
            // Mirroring images from left to right
            imgCop.setScaleX(-1);
            imageFeetOverlay.setScaleX(-1);
            imageSensor1G.setScaleX(-1);
            imageSensor1Y.setScaleX(-1);
            imageSensor1R.setScaleX(-1);
            imageSensor2G.setScaleX(-1);
            imageSensor2Y.setScaleX(-1);
            imageSensor2R.setScaleX(-1);
            imageSensor3G.setScaleX(-1);
            imageSensor3Y.setScaleX(-1);
            imageSensor3R.setScaleX(-1);
            imageSensor4G.setScaleX(-1);
            imageSensor4Y.setScaleX(-1);
            imageSensor4R.setScaleX(-1);
            imageSensor5G.setScaleX(-1);
            imageSensor5Y.setScaleX(-1);
            imageSensor5R.setScaleX(-1);
            imageSensor6G.setScaleX(-1);
            imageSensor6Y.setScaleX(-1);
            imageSensor6R.setScaleX(-1);
            //set up sensor values for right(mirrored)
            textSensor1 = findViewById(R.id.textsensor2);
            textSensor2 = findViewById(R.id.textsensor1);
            textSensor3 = findViewById(R.id.textsensor4);
            textSensor4 = findViewById(R.id.textsensor3);
            textSensor5 = findViewById(R.id.textsensor6);
            textSensor6 = findViewById(R.id.textsensor5);
        }
        //TODO
        //else exit->error
        //set settings
        samplingRate = setSpeedHz[Integer.parseInt(bts1)];
        measurementRange = setResistanceKHom[Integer.parseInt(bts2)];
        resistanceMultiplier = resistanceMultiplierArray[Integer.parseInt(bts2)];
        maxValue = maxValueArray[Integer.parseInt(bts2)];

        if (Integer.parseInt(bts1) == 0) refreshSpeed = 1000 / 10;
        else {
            refreshSpeed = 1000 / (25 * Integer.parseInt(bts1));//milliseconds
        }

/*
        try {
            // create the device and sock for the Right device connection
            device = mBlueAdapter.getRemoteDevice(mac); // use the saved mac address
            Method m = device.getClass().getMethod("createInsecureRfcommSocket", new Class[]{int.class}); // connect using insecure connection
            socket = (BluetoothSocket) m.invoke(device, 1);
            socket.connect();
            thread = new ConnectedThread(socket, resistanceMultiplier,tempData);// create a new thread for the connection using our custom thread class
        } catch (Exception e) {
            //TODO
            //make all toast string l/r into setup value
            Toast.makeText(getApplicationContext(), mac + " Connection to the " + setup +" sock has failed", Toast.LENGTH_LONG).show();
        }
*/



        btnReconnect.setOnClickListener(v -> {
            try {
                // create the device and sock for the left device connection
                device = mBlueAdapter.getRemoteDevice(mac); // use the saved mac address
                Method m = device.getClass().getMethod("createInsecureRfcommSocket", new Class[]{int.class}); // connect using insecure connection
                socket = (BluetoothSocket) m.invoke(device, 1);
                socket.connect();
                thread = new ConnectedThread(socket, resistanceMultiplier,tempData); // create a new thread for the connection using our custom thread class
            } catch (Exception e) {
                if(isLeft) {
                    Toast.makeText(getApplicationContext(), "Left Connection Failed", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Right Connection Failed", Toast.LENGTH_LONG).show();
                }
            }
        });



        btnPlay.setOnClickListener(v -> {
            //TODO
            //check visibility logic
            btnCopCalib.setVisibility(View.VISIBLE);



                if (thread == null) {
                    // we failed creating thread so we don't enter the next part
                } else if (thread.getState() != Thread.State.NEW) {
                    thread = new ConnectedThread(socket, resistanceMultiplier,tempData);
                    SystemClock.sleep(50);
                    thread.write(startStream);
                    thread.startThread();
                } else {
                    thread.write(startStream);
                    thread.startThread();
                }

                try {
                    // stop the measurement
                    thread.write(stopStream);

                    SystemClock.sleep(20);
                    // set the range
                    thread.write(measurementRange);
                    SystemClock.sleep(20);
                    // set the speed
                    thread.write(samplingRate);
                    SystemClock.sleep(20);
                    // reset the timer
                    thread.write(resetTimestamp);
                } catch (Exception e) {
                    // couldn't send config, probably because no working thread, put here check
                    Toast.makeText(getApplicationContext(), "Couldn't send configuration, check connection", Toast.LENGTH_LONG).show();
                }
            // is Right



        });

        //printing symbols to mark timestamps
        //for measurments
        btnBreak.setOnClickListener(v -> {
            if (checkSavingOn) {
                tempData.println("---");
            }
        });


        btnSaveData.setOnClickListener(v -> {

                    if (isStoragePermissionGranted()) {


                        if (checkSavingOn) {// is saving data
                            try {
                                btnBreak.setVisibility(View.INVISIBLE);
                                fos.close();
                                tempData.close();
                                checkSavingOn = false;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else { // not saving data
                            btnBreak.setVisibility(View.VISIBLE);
                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy-HH:mm(ss)", Locale.getDefault());//file name
                            String fileName = sdf.format(new Date()) + ".txt"; //q:mozet bitj dobavitj kakie nastroiki v nazvanie faila???
                            File dir = new File("/sdcard/Sensor/data");//pofiksitj razrewenie na ispolzovanija dannih
                            File savefile = new File(dir, fileName);


                            try {
                                btnBreak.setVisibility(View.VISIBLE);
                                fos = new FileOutputStream(savefile);
                                tempData = new PrintWriter(fos);
                                btnSaveData.setEnabled(true);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            checkSavingOn = true;
                        }
                    } else {
                        requestPermission();
                    }

                }
        );
        //is not used+ is not working
        //TODO
        //make graph
        btnGraph.setEnabled(false);
        btnGraph.setOnClickListener(v -> {//reset button
            //btncolors
//            btnGraph.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_primary));
//            btnCop.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_secondary));
//            btnValues.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_secondary));
            //data
            CopVisibility(View.GONE);
            CopCheckVisibility(View.GONE);
            ValuesVisibility(View.GONE);
            GraphVisbility(View.VISIBLE);
            //checks
            isCalib = false;
            checkDataOn = false;
            checkVisualOn = false;
        });
        //      x1   x2
        //      x3   x4
        //      x5   x6         x1,x2...x6 are values from sensor
        btnValues.setOnClickListener(v -> {
            //btncolors
//            btnValues.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_primary));
//            btnCop.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_secondary));
//            btnGraph.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_secondary));
            //data
            CopVisibility(View.INVISIBLE);
            CopCheckVisibility(View.INVISIBLE);
            GraphVisbility(View.GONE);
            ValuesVisibility(View.VISIBLE);
            //checks
            isCalib = false;
            checkDataOn = true;
            checkVisualOn = false;
        });


        btnCop.setOnClickListener(v -> {
            //btn colors
//            btnCop.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_primary));
//            btnGraph.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_secondary));
//            btnValues.setBackgroundTintList(getResources().getColorStateList(R.color.design_default_color_secondary));
            //data
            CopCheckVisibility(View.GONE);
            ValuesVisibility(View.GONE);
            GraphVisbility(View.GONE);
            CopVisibility(View.VISIBLE);
            //checks
            isCalib = false;
            checkDataOn = false;
            checkVisualOn = true;
            first_time_canvas = true;
        });

        btnCopCalib.setOnClickListener(v -> {
            VisualMainButtonsVisibility(View.VISIBLE);

            //data
            checkDataOn = false;
            checkVisualOn = false;
            //saving data for calibration
            isCalib = false;
            calib_sensor1 = sensor1;
            calib_sensor2 = sensor2;
            calib_sensor3 = sensor3;
            calib_sensor4 = sensor4;
            calib_sensor5 = sensor5;
            calib_sensor6 = sensor6;
//            if (checkSavingOn) {
//                tempData.println(tmpSensor.toTxtCalibrationData(timeOfStart));
//            }
            isCalib = true;
            ValuesVisibility(View.GONE);
            CopVisibility(View.GONE);
            GraphVisbility(View.GONE);
            CopCheckVisibility(View.VISIBLE);
        });




        forRefresh = new Thread() {
            @Override
            public void run() {

                while (!isInterrupted()) {
                    try {
                        Thread.sleep(sleepTime);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (checkVisualOn) {
                                    if(isLeft) {
                                        if ( (calib_sensor3/4)+cop_sensor4 < cop_sensor3) { //cop_sensor3 > cop_sensor4 &&
                                            imageLeftArrow.setVisibility(View.INVISIBLE);
                                            imageRightArrow.setVisibility(View.VISIBLE);
                                            imageDownArrow.setVisibility(View.INVISIBLE);
                                        } else if( (calib_sensor4/4)+cop_sensor3 < cop_sensor4) { //cop_sensor4 > cop_sensor3 &&
                                            imageLeftArrow.setVisibility(View.VISIBLE);
                                            imageRightArrow.setVisibility(View.INVISIBLE);
                                            imageDownArrow.setVisibility(View.INVISIBLE);
                                        } else{
                                            imageLeftArrow.setVisibility(View.INVISIBLE);
                                            imageRightArrow.setVisibility(View.INVISIBLE);
                                            imageDownArrow.setVisibility(View.VISIBLE);
                                        }
                                    }
                                    if(!isLeft){
                                        if(  (calib_sensor4/5)+cop_sensor3 < cop_sensor4) { //cop_sensor4 > cop_sensor3
                                            imageLeftArrow.setVisibility(View.INVISIBLE);
                                            imageRightArrow.setVisibility(View.VISIBLE);
                                            imageDownArrow.setVisibility(View.INVISIBLE);

                                        } else if (  (calib_sensor3/5)+cop_sensor4 < cop_sensor3){ //cop_sensor3 > cop_sensor4
                                            imageLeftArrow.setVisibility(View.VISIBLE);
                                            imageRightArrow.setVisibility(View.INVISIBLE);
                                            imageDownArrow.setVisibility(View.INVISIBLE);
                                        } else{
                                            imageLeftArrow.setVisibility(View.INVISIBLE);
                                            imageRightArrow.setVisibility(View.INVISIBLE);
                                            imageDownArrow.setVisibility(View.VISIBLE);
                                        }
                                    }

                                    CanvasToBitmap canvasToBitmap = new CanvasToBitmap(getBaseContext());
                                    Bitmap copBitmap = canvasToBitmap.bitmap;
                                    imgCop.setImageBitmap(copBitmap);
                                } else if (isCalib) {

                                    textSensor1.setText(String.valueOf((int) sensor1));
                                    textSensor2.setText(String.valueOf((int) sensor2));
                                    textSensor3.setText(String.valueOf((int) sensor3));
                                    textSensor4.setText(String.valueOf((int) sensor4));
                                    textSensor5.setText(String.valueOf((int) sensor5));
                                    textSensor6.setText(String.valueOf((int) sensor6));


                                    RedYellowGreenStripes(imageSensor1G, imageSensor1Y, imageSensor1R, maxValue, sensor1);
                                    RedYellowGreenStripes(imageSensor2G, imageSensor2Y, imageSensor2R, maxValue, sensor2);
                                    RedYellowGreenStripes(imageSensor3G, imageSensor3Y, imageSensor3R, maxValue, sensor3);
                                    RedYellowGreenStripes(imageSensor4G, imageSensor4Y, imageSensor4R, maxValue, sensor4);
                                    RedYellowGreenStripes(imageSensor5G, imageSensor5Y, imageSensor5R, maxValue, sensor5);
                                    RedYellowGreenStripes(imageSensor6G, imageSensor6Y, imageSensor6R, maxValue, sensor6);

                                } else if (checkDataOn) {
                                    thread.resumeBufferReading();//??? helps or not
                                    //if - a lot then recalibrate
                                    if(isLeft){

                                            tmpArray = thread.getSensorArray();
                                            tmpArrayTwo = thread.getCalibArray();
                                        textSensor1.setText(String.valueOf((int) (100 - (tmpArray[0] * 100 / tmpArrayTwo[0]))) + " %");
                                        textSensor2.setText(String.valueOf((int) (100 - (tmpArray[1] * 100 / tmpArrayTwo[1]))) + " %");
                                        textSensor3.setText(String.valueOf((int) (100 - (tmpArray[2] * 100 / tmpArrayTwo[2]))) + " %");
                                        textSensor4.setText(String.valueOf((int) (100 - (tmpArray[3] * 100 / tmpArrayTwo[3]))) + " %");
                                        textSensor5.setText(String.valueOf((int) (100 - (tmpArray[4] * 100 / tmpArrayTwo[4]))) + " %");
                                        textSensor6.setText(String.valueOf((int) (100 - (tmpArray[5] * 100 / tmpArrayTwo[5]))) + " %");

                                    }else if(!isLeft){
                                        textSensor1.setText(String.valueOf((int) (100 - (tmpArray[0] * 100 / tmpArrayTwo[0]))) + " %");
                                        textSensor2.setText(String.valueOf((int) (100 - (tmpArray[1] * 100 / tmpArrayTwo[1]))) + " %");
                                        textSensor3.setText(String.valueOf((int) (100 - (tmpArray[2] * 100 / tmpArrayTwo[2]))) + " %");
                                        textSensor4.setText(String.valueOf((int) (100 - (tmpArray[3] * 100 / tmpArrayTwo[3]))) + " %");
                                        textSensor5.setText(String.valueOf((int) (100 - (tmpArray[4] * 100 / tmpArrayTwo[4]))) + " %");
                                        textSensor6.setText(String.valueOf((int) (100 - (tmpArray[5] * 100 / tmpArrayTwo[5]))) + " %");
                                    } else{

                                    }
                                    RedYellowGreenStripes(imageSensor1G, imageSensor1Y,
                                            imageSensor1R, calib_sensor1, cop_sensor1);
                                    RedYellowGreenStripes(imageSensor2G, imageSensor2Y,
                                            imageSensor2R, calib_sensor2, cop_sensor2);
                                    RedYellowGreenStripes(imageSensor3G, imageSensor3Y,
                                            imageSensor3R, calib_sensor3, cop_sensor3);
                                    RedYellowGreenStripes(imageSensor4G, imageSensor4Y,
                                            imageSensor4R, calib_sensor4, cop_sensor4);
                                    RedYellowGreenStripes(imageSensor5G, imageSensor5Y,
                                            imageSensor5R, calib_sensor5, cop_sensor5);
                                    RedYellowGreenStripes(imageSensor6G, imageSensor6Y,
                                            imageSensor6R, calib_sensor6, cop_sensor6);

                                } else {

                                }

                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        forRefresh.start();


    }


//    public class ConnectedThread extends Thread {
//        private final BluetoothSocket mmSocket;
//        private final InputStream mmInStream;
//        private final OutputStream mmOutStream;
//
//        public ConnectedThread(BluetoothSocket socket) {
//            mmSocket = socket;
//            InputStream tmpIn = null;
//            OutputStream tmpOut = null;
//
//            // Get the input and output streams, using temp objects because
//            // member streams are final
//            try {
//                tmpIn = socket.getInputStream();
//                tmpOut = socket.getOutputStream();
//            } catch (IOException e) {
//            }
//            mmInStream = tmpIn;
//            mmOutStream = tmpOut;
//        }
//
//
//        public void startThread() {
//            start();
//        }
//
//        public void run() {
//
//            int bytesAvailable; // bytes available in the buffer
//            byte stop_byte = (byte) 0x55;
//            byte start_byte = (byte) 0xF0;
//            int pos = 0, tmp;
//            byte[] buf = new byte[1024]; // actually not needed more than 47
//            //byte[] b = new byte[1024]; // actually not needed more than 47
//
//            // Keep looping to listen for received messages
//            startWorker = true;
//            timeOfStart = System.currentTimeMillis();
//            int tm;
//            int[] sVal = new int[6];
//
//
//            while (startWorker) {
//                try {
//                    bytesAvailable = mmInStream.available();
//                    if (bytesAvailable > 0) // one data packet has minimum 47 bytes
//                    {
//                        byte[] b = new byte[bytesAvailable];
//                        bytesAvailable = mmInStream.read(b, 0, bytesAvailable);
//                        for (int i = 0; i < bytesAvailable; i++) {
//                            // put the byte in the buffer
//                            buf[pos] = b[i];
//                            // check if the new byte was the end of a packet
//                            if (buf[pos] == stop_byte && pos > 45) {
//                                if (buf[pos - 46] == start_byte) {
//                                    //TODO
//                                    //try with nanotime instead of ms
//                                    //tm = System.nanoTime();
//
//
//                                        tmp = bti(Arrays.copyOfRange(buf, pos - 17, pos - 15));
//                                        sVal[0] = (int) (tmp * res_multip);
//                                        sensor1 = Double.valueOf(sVal[0]);
//
//
//                                        tmp = bti(Arrays.copyOfRange(buf, pos - 15, pos - 13));
//                                        sVal[1] = (int) (tmp * res_multip);
//                                        sensor2 = Double.valueOf(sVal[1]);
//
//                                        tmp = bti(Arrays.copyOfRange(buf, pos - 13, pos - 11));
//                                        sVal[2] = (int) (tmp * res_multip);
//                                        sensor3 = Double.valueOf(sVal[2]);
//
//                                        tmp = bti(Arrays.copyOfRange(buf, pos - 11, pos - 9));
//                                        sVal[3] = (int) (tmp * res_multip);
//                                        sensor4 = Double.valueOf(sVal[3]);
//
//                                        tmp = bti(Arrays.copyOfRange(buf, pos - 9, pos - 7));
//                                        sVal[4] = (int) (tmp * res_multip);
//                                        sensor5 = Double.valueOf(sVal[4]);
//
//                                        tmp = bti(Arrays.copyOfRange(buf, pos - 7, pos - 5));
//                                        sVal[5] = (int) (tmp * res_multip);
//                                        sensor6 = Double.valueOf(sVal[5]);
//
//                                        tmpSensor = new Sensor(sVal[0],sVal[1],sVal[2],sVal[3],sVal[4],sVal[5]);
//                                        if (CheckSavingOn) {
//                                            tempData.println(tmpSensor.toTxtData(timeOfStart));
//                                        }
//
//
//
////                                            cop_sensor1 = (sensor1 - calib_sensor1);
////                                            cop_sensor2 = (sensor2 - calib_sensor2);
////                                            cop_sensor3 = (sensor3 - calib_sensor3);
////                                            cop_sensor4 = (sensor4 - calib_sensor4);
////                                            cop_sensor5 = (sensor5 - calib_sensor5);
////                                            cop_sensor6 = (sensor6 - calib_sensor6);
//                                        //get values for cop
//                                        cop_sensor1 = (calib_sensor1 - sensor1);
//                                        cop_sensor2 = (calib_sensor2 - sensor2);
//                                        cop_sensor3 = (calib_sensor3 - sensor3);
//                                        cop_sensor4 = (calib_sensor4 - sensor4);
//                                        cop_sensor5 = (calib_sensor5 - sensor5);
//                                        cop_sensor6 = (calib_sensor6 - sensor6);
//
//                                        //sensetivity bug fix
//                                        if (cop_sensor1 < 0) cop_sensor1 = 0;
//                                        if (cop_sensor2 < 0) cop_sensor2 = 0;
//                                        if (cop_sensor3 < 0) cop_sensor3 = 0;
//                                        if (cop_sensor4 < 0) cop_sensor4 = 0;
//                                        if (cop_sensor5 < 0) cop_sensor5 = 0;
//                                        if (cop_sensor6 < 0) cop_sensor6 = 0;
//                                        if (CheckVisualOn) {
//                                            double cop_sensorSum = cop_sensor1 + cop_sensor2 + cop_sensor3 + cop_sensor4 + cop_sensor5 + cop_sensor6;
//                                            double cop_calibSum = calib_sensor1 + calib_sensor2 + calib_sensor3 + calib_sensor4 + calib_sensor5 + calib_sensor6;
//                                            xValue = (float) ((
//                                                    cop_sensor1 / calib_sensor1 * Math.cos(Math.toRadians(alphaDegree)) +
//                                                    cop_sensor2 / calib_sensor2 * Math.cos(Math.toRadians(180 - alphaDegree)) +
//                                                    Math.cos(Math.toRadians(alphaDegree)) * cop_sensor3 / calib_sensor3 * Math.cos(Math.toRadians(0)) +
//                                                    Math.cos(Math.toRadians(alphaDegree)) * cop_sensor4 / calib_sensor4 * Math.cos(Math.toRadians(180)) +
//                                                    cop_sensor5 / calib_sensor5 * Math.cos(Math.toRadians(360 - alphaDegree)) +
//                                                    cop_sensor6 / calib_sensor6 * Math.cos(Math.toRadians(180 + alphaDegree)))/6);
//
//                                            yValue = (float) ((
//                                                     cop_sensor1 / cop_sensorSum * Math.sin(Math.toRadians(alphaDegree)) +
//                                                     cop_sensor2 / cop_sensorSum * Math.sin(Math.toRadians(180 - alphaDegree)) +
//                                                     Math.cos(Math.toRadians(alphaDegree)) * cop_sensor3 / cop_sensorSum * Math.sin(Math.toRadians(0)) +
//                                                     Math.cos(Math.toRadians(alphaDegree)) * cop_sensor4 / cop_sensorSum * Math.sin(Math.toRadians(180)) +
//                                                     cop_sensor5 / cop_sensorSum * Math.sin(Math.toRadians(360 - alphaDegree)) +
//                                                     cop_sensor6 / cop_sensorSum * Math.sin(Math.toRadians(180 + alphaDegree)))/6);
//
////                                            xValue = (float) ((
////                                                    cop_sensor1   * Math.cos(Math.toRadians(alphaDegree)) +
////                                                    cop_sensor2  * Math.cos(Math.toRadians(180 - alphaDegree)) +
////                                                    Math.cos(Math.toRadians(alphaDegree)) * cop_sensor3  * Math.cos(Math.toRadians(0)) +
////                                                    Math.cos(Math.toRadians(alphaDegree)) * cop_sensor4  * Math.cos(Math.toRadians(180)) +
////                                                    cop_sensor5  * Math.cos(Math.toRadians(360 - alphaDegree)) +
////                                                    cop_sensor6  * Math.cos(Math.toRadians(180 + alphaDegree)))/cop_calibSum);
////
////                                            yValue = (float) ((
////                                                     cop_sensor1 / cop_calibSum * Math.sin(Math.toRadians(alphaDegree)) +
////                                                     cop_sensor2 / cop_calibSum * Math.sin(Math.toRadians(180 - alphaDegree)) +
////                                                     Math.cos(Math.toRadians(alphaDegree)) * cop_sensor3 / cop_calibSum * Math.sin(Math.toRadians(0)) +
////                                                     Math.cos(Math.toRadians(alphaDegree)) * cop_sensor4 / cop_calibSum * Math.sin(Math.toRadians(180)) +
////                                                     cop_sensor5 / cop_calibSum * Math.sin(Math.toRadians(360 - alphaDegree)) +
////                                                     cop_sensor6 / cop_calibSum * Math.sin(Math.toRadians(180 + alphaDegree)))/cop_calibSum);
//
//
//
//
//                                            List_xValue.add(xValue);
//                                            List_yValue.add(yValue);
//
//                                        }
//
//
//
//                                    pos = 0;
//                                }
//                            } else { // increment the counter
//                                if (pos < 1023) pos++; // safety measure
//                                else pos = 0;
//                            }
//                        }
//                    }
//
//                } catch (IOException e) {
//                    startWorker = false;
//                    break;
//                }
//            }
//        }
//
//        /* Call this from the main activity to send data to the remote device */
//        public void write(String input) {
//            byte[] bytes = input.getBytes();           //converts entered String into bytes
//            try {
//                mmOutStream.write(bytes);
//            } catch (IOException e) {
//            }
//        }
//
//
//    }


//    private static int bti(byte[] b) {
//        return ((b[1] & 0xFF) << 8) | (b[0] & 0xFF);
//    }

    public class CanvasToBitmap extends View {

        //for canvas
        float canvasHeight, canvasWidth, floatDensity, offset;
        int intDensity;
        boolean first_time_canvas = true;
        Paint paint = new Paint();
        int width = imgCop.getWidth();
        int height = imgCop.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Bitmap bit_l = BitmapFactory.decodeResource(getResources(), R.drawable.main_bez_liniy_l);
        Bitmap bit_r = BitmapFactory.decodeResource(getResources(), R.drawable.main_bez_liniy_r);


        float pictureDensity = bit_l.getDensity(); // 374
        float pictureHeight = bit_l.getHeight(); //6854
        float pictureWidth = bit_l.getWidth();//3855
        //l = r doesnt matter

        public CanvasToBitmap(Context context) {
            super(context);
            Canvas canvas = new Canvas(bitmap);
            draw(canvas);
        }


        @Override
        public void onDraw(Canvas canvas) {

            paint.setColor(Color.BLUE);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(50);
            if (first_time_canvas) {
                canvas.setBitmap(bitmap);

                //getting Density to match xml

                canvasHeight = canvas.getHeight();
                canvasWidth = canvas.getWidth();
                floatDensity = pictureDensity / pictureHeight * canvasHeight;
                intDensity = Math.round(floatDensity);
                canvas.setDensity(intDensity);
                //placing image in the middle
                offset = (canvasWidth / 2) - (pictureWidth / pictureDensity * floatDensity / 2);
                //finding coefficient 1 equals distance between center and first sensor
                xMiddle = canvasWidth / 2;
                yMiddle = canvasHeight / 2;

                xValuePixel = (xValue + 1) * canvasWidth / 2;
                yValuePixel = (-1 * yValue + 1) * canvasHeight / 2;
                if(isLeft)
                    canvas.drawBitmap(bit_l, offset, 0, paint);
                else canvas.drawBitmap(bit_r, offset, 0, paint);
                //x
                canvas.drawLine(xMiddle, 0, xMiddle, canvas.getHeight(), paint);
                //y
                canvas.drawLine(0, yMiddle, canvas.getWidth(), yMiddle, paint);
                first_time_canvas = false;

            }


            paint.setStrokeWidth(7);
            paint.setColor(Color.BLUE);
            canvas.drawPoint(xValuePixel, yValuePixel, paint);
            //metodi
            if (List_xValue.size() > 0 && List_yValue.size() > 0) {
                Array_x_5 = arrayOf5(List_xValue);
                Array_y_5 = arrayOf5(List_yValue);
                Array_x_30 = plus5from30(Array_x_30, Array_x_5);
                Array_y_30 = plus5from30(Array_y_30, Array_y_5);
//                canvas.drawText(String.valueOf(Array_x_5[0]) + " - " + String.valueOf(Array_x_5[1]), 50, 50, paint);
                List_xValue.clear();
                List_yValue.clear();
            }
            //figa4iw 30
            for (int n = 0; n < 29; n++) {
                float pictureX_first = getPictureXValue(Array_x_30[n], canvasWidth);
                float pictureY_first = getPictureYValue(Array_y_30[n], canvasHeight);
                float pictureX_last = getPictureXValue(Array_x_30[n + 1], canvasWidth);
                float pictureY_last = getPictureYValue(Array_y_30[n + 1], canvasHeight);

                paint.setStrokeWidth(2 + n / 3);
                paint.setColor(Color.rgb(255 - 180 + n * 3, 0, 120));
                canvas.drawLine(pictureX_first, pictureY_first, pictureX_last, pictureY_last, paint);
                if (n == 28)
                    paint.setColor(Color.RED);
                canvas.drawPoint(pictureX_last, pictureY_last, paint);
            }
            xValueOld = xValuePixel;//ne uzaetsja
            yValueOld = yValuePixel;


//            ByteArrayOutputStream mByteArrayOutputStream = new ByteArrayOutputStream();


            try {
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100,    mByteArrayOutputStream); //default q = 100
//                canvas.
//                bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(mByteArrayOutputStream.toByteArray()));
//                mByteArrayOutputStream.flush();
//                mByteArrayOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public class DrawLayer extends View {
        public DrawLayer(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            Paint myPaint = new Paint();
            myPaint.setColor(Color.rgb(0, 255, 255));
            myPaint.setTextSize(60);
            myPaint.setStrokeWidth(50);


            Bitmap copBitmap;
            Canvas myCanvas = new Canvas();
            if(isLeft){
                copBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_bez_liniy_l);
            } else {
                copBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_bez_liniy_r);
            }

//            myCanvas.drawBitmap(copBitmap, 0, 0, null);
            xMiddle = copBitmap.getWidth() / 2;
            yMiddle = copBitmap.getHeight() / 2;


            // init StaticLayout for text
//            StaticLayout textLayout = new StaticLayout(
//                    "qwe", qwePaint, 60, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);

//            myCanvas.save();
//            myCanvas.translate(xMiddle,yMiddle);
//            textLayout.draw(myCanvas);
//            myCanvas.restore();


        }

    }


    public void CopCheckVisibility(int visibility) {
        imageFeetOverlay.setVisibility(visibility);
        imageSensor1G.setVisibility(visibility);
        imageSensor1Y.setVisibility(visibility);
        imageSensor1R.setVisibility(visibility);
        imageSensor2G.setVisibility(visibility);
        imageSensor2Y.setVisibility(visibility);
        imageSensor2R.setVisibility(visibility);
        imageSensor3G.setVisibility(visibility);
        imageSensor3Y.setVisibility(visibility);
        imageSensor3R.setVisibility(visibility);
        imageSensor4G.setVisibility(visibility);
        imageSensor4Y.setVisibility(visibility);
        imageSensor4R.setVisibility(visibility);
        imageSensor5G.setVisibility(visibility);
        imageSensor5Y.setVisibility(visibility);
        imageSensor5R.setVisibility(visibility);
        imageSensor6G.setVisibility(visibility);
        imageSensor6Y.setVisibility(visibility);
        imageSensor6R.setVisibility(visibility);
        textSensor1.setVisibility(visibility);
        textSensor2.setVisibility(visibility);
        textSensor3.setVisibility(visibility);
        textSensor4.setVisibility(visibility);
        textSensor5.setVisibility(visibility);
        textSensor6.setVisibility(visibility);
    }

    public void ValuesVisibility(int visibility) {
        imageFeetOverlay.setVisibility(visibility);
        imageSensor1G.setVisibility(visibility);
        imageSensor1Y.setVisibility(visibility);
        imageSensor1R.setVisibility(visibility);
        imageSensor2G.setVisibility(visibility);
        imageSensor2Y.setVisibility(visibility);
        imageSensor2R.setVisibility(visibility);
        imageSensor3G.setVisibility(visibility);
        imageSensor3Y.setVisibility(visibility);
        imageSensor3R.setVisibility(visibility);
        imageSensor4G.setVisibility(visibility);
        imageSensor4Y.setVisibility(visibility);
        imageSensor4R.setVisibility(visibility);
        imageSensor5G.setVisibility(visibility);
        imageSensor5Y.setVisibility(visibility);
        imageSensor5R.setVisibility(visibility);
        imageSensor6G.setVisibility(visibility);
        imageSensor6Y.setVisibility(visibility);
        imageSensor6R.setVisibility(visibility);
        textSensor1.setVisibility(visibility);
        textSensor2.setVisibility(visibility);
        textSensor3.setVisibility(visibility);
        textSensor4.setVisibility(visibility);
        textSensor5.setVisibility(visibility);
        textSensor6.setVisibility(visibility);

    }

    public void CopVisibility(int visibility) {
        imgCop.setVisibility(visibility);
        imageLeftArrow.setVisibility(visibility);
        imageRightArrow.setVisibility(visibility);
        imageDownArrow.setVisibility(visibility);
    }

    public void GraphVisbility(int visibility) {

    }

    public void VisualMainButtonsVisibility(int visibility) {
        btnGraph.setVisibility(visibility);
        btnCop.setVisibility(visibility);
        btnValues.setVisibility(visibility);
    }


    public void RedYellowGreenStripes(ImageView green, ImageView yellow, ImageView red, double max_value,double sensor_value){

        if (sensor_value <= (max_value / 3) && 0 <= sensor_value) {
            green.setVisibility(View.VISIBLE);
            yellow.setVisibility(View.INVISIBLE);
            red.setVisibility(View.INVISIBLE);
        } else if (sensor_value <= 2 * (max_value / 3) && (max_value / 3) < sensor_value) {
            green.setVisibility(View.INVISIBLE);
            yellow.setVisibility(View.VISIBLE);
            red.setVisibility(View.INVISIBLE);
        } else if (sensor_value <= max_value && (2 * max_value / 3) < sensor_value) {
            green.setVisibility(View.INVISIBLE);
            yellow.setVisibility(View.INVISIBLE);
            red.setVisibility(View.VISIBLE);
        } else {
            green.setVisibility(View.INVISIBLE);
            yellow.setVisibility(View.INVISIBLE);
            red.setVisibility(View.INVISIBLE);
        }

    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permission is granted", Toast.LENGTH_LONG).show();
                return true;
            } else {


                Toast.makeText(getApplicationContext(), "Permission is revoked", Toast.LENGTH_LONG).show();
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Toast.makeText(getApplicationContext(), "Permission is granted", Toast.LENGTH_LONG).show();
            return true;
        }

    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(Visual.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(Visual.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(Visual.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }

    public int getDeviceWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public int getDeviceHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static float[] arrayOf5(ArrayList<Float> arrayList) {
        int size = arrayList.size();
        float newArray[] = new float[5];
        int average = size / 4;
        int a = 0;
        for (int i = 0; i < 5; i++) {

            if (i == 4) {
                if (a>1)
                newArray[i] = arrayList.get(a - 1);
                else newArray[i] = arrayList.get(0);
            } else {
                newArray[i] = arrayList.get(a);
            }
            a += average;
        }
        return newArray;
    }

    public static float getPictureXValue(float xValue, float canvasWidth) {
        float xValuePixel = (xValue + 1) * canvasWidth / 2;

        return xValuePixel;
    }

    public static float getPictureYValue(float yValue, float canvasHeight) {
        float yValuePixel = (-1 * yValue + 1) * canvasHeight / 2;
        return yValuePixel;
    }

    public static float[] plus5from30(float arrayof30[], float arrayof5[]) {
        int i;

        // create a new array of size n+1
        float newArray[] = new float[30];

        // insert the elements from
        // the old array into the new array
        // insert all elements till n
        // then insert x at n+1
        for (i = 0; i < 30 - 5; i++)
            newArray[i] = arrayof30[i + 5];
        newArray[25] = arrayof5[0];
        newArray[26] = arrayof5[1];
        newArray[27] = arrayof5[2];
        newArray[28] = arrayof5[3];
        newArray[29] = arrayof5[4];


        return newArray;
    }

    //for loop




}


