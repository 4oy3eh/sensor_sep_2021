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

import java.text.MessageFormat;
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

    // because this is solo class everything could be static

    // everything UI is static because it is used in static thread later
    private static ImageView imgCop;
    private static ImageView imageLeftArrow, imageRightArrow, imageDownArrow, imageFeetOverlay;
    private static TextView[] textSensor = new TextView[8];
    private static ImageView[] imageSensorRedStripe = new ImageView[8];
    private static ImageView[] imageSensorYellowStripe = new ImageView[8];
    private static ImageView[] imageSensorGreenStripe = new ImageView[8];
    // booleans are used in static thread
    static boolean visualizationCOPIsOn = false, visualizationValuesIsOn = false, checkSavingOn =
            false;
    private CanvasToBitmap canvasToBitmap;
    private Bitmap copBitmap;

    private boolean resetThePicture = true, dataReadIsOn = false,
            isRunning;
    private static boolean isLeft;

    private Button btnValues, btnCop, btnReconnect, btnPlay, btnTest, btnGraph, btnDoCalibration,
            btnSaveData,
            btnBreak;
    TextView bugtracker;







    Intent settingsIntent, nextIntent;
    String mac, bts1, bts2, setup, measurementRange, samplingRate;
    BluetoothDevice device;
    BluetoothSocket socket;
    BluetoothAdapter bluetoothAdapter;
    static ConnectedThread thread;
    PrintWriter tempData;
    int refreshSpeed;
    static double resistanceMultiplier;
    static int maxValue;
    static int counterForRefreshPicture;
    double sensorArray[], calibrationValuesArray[];
    float xMiddle, yMiddle, xValuePixel, yValuePixel, xValueOld, yValueOld;
    float arrayY5[] = new float[5];
    float arrayY30[] = new float[30];
    float arrayX5[] = {0, 0, 0, 0, 0};
    float arrayX30[] = new float[30];
    final static int sensorCount = Sensor.getMaxSensorCount();
    int deviceHeight, deviceWidth;
    FileOutputStream fos;
    Handler bluetoothIn;
    static Thread forRefresh;
    //for categories and refresh(showdata)

    //save
    String filename = "", filepath = "SavedData";

    String startStream = "BT^START\r";
    String resetTimestamp = "BT^TRES\r";
    String stopStream = "BT^STOP\r";
    String[] setSpeedHz = new String[]{"BTS1=0\r", "BTS1=1\r", "BTS1=2\r", "BTS1=3\r", "BTS1=4\r", "BTS1=5\r", "BTS1=6\r", "BTS1=7\r", "BTS1=8\r"};
    String[] setResistanceKHom = new String[]{"BTS2=0\r", "BTS2=1\r", "BTS2=2\r", "BTS2=3\r", "BTS2=4\r", "BTS2=5\r", "BTS2=6\r", "BTS2=7\r", "BTS2=8\r", "BTS2=9\r"};
    double[] resistanceMultiplierArray = new double[]{0.03125, 0.063, 0.125, 0.25, 0.50, 1.00, 2.00,
            4.00, 8.00, 16.00};
    static final int[] maxValueArray = new int[]{2000,  4000, 8000, 16000, 32000, 64000, 128000,
            256000, 512000, 1024000};




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
        btnDoCalibration = findViewById(R.id.btn_calib_cop);
        //images
        imageLeftArrow = findViewById(R.id.image_leftarrow);
        imageRightArrow = findViewById(R.id.image_rightarrow);
        imageDownArrow = findViewById(R.id.image_downarrow);
        imgCop = findViewById(R.id.imageCop_l);
        imageFeetOverlay = findViewById(R.id.image_first_l);

        //stripes into array
        imageSensorGreenStripe[0] = findViewById(R.id.image_1sensor_g_l);
        imageSensorGreenStripe[1] = findViewById(R.id.image_2sensor_g_l);
        imageSensorGreenStripe[2] = findViewById(R.id.image_3sensor_g_l);
        imageSensorGreenStripe[3] = findViewById(R.id.image_4sensor_g_l);
        imageSensorGreenStripe[4] = findViewById(R.id.image_5sensor_g_l);
        imageSensorGreenStripe[5] = findViewById(R.id.image_6sensor_g_l);

        imageSensorYellowStripe[0] = findViewById(R.id.image_1sensor_y_l);
        imageSensorYellowStripe[1] = findViewById(R.id.image_2sensor_y_l);
        imageSensorYellowStripe[2] = findViewById(R.id.image_3sensor_y_l);
        imageSensorYellowStripe[3] = findViewById(R.id.image_4sensor_y_l);
        imageSensorYellowStripe[4] = findViewById(R.id.image_5sensor_y_l);
        imageSensorYellowStripe[5] = findViewById(R.id.image_6sensor_y_l);

        imageSensorRedStripe[0] = findViewById(R.id.image_1sensor_r_l);
        imageSensorRedStripe[1] = findViewById(R.id.image_2sensor_r_l);
        imageSensorRedStripe[2] = findViewById(R.id.image_3sensor_r_l);
        imageSensorRedStripe[3] = findViewById(R.id.image_4sensor_r_l);
        imageSensorRedStripe[4] = findViewById(R.id.image_5sensor_r_l);
        imageSensorRedStripe[5] = findViewById(R.id.image_6sensor_r_l);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        nextIntent = new Intent(this, Visual.class);
        deviceWidth = getDeviceWidth();
        deviceHeight = getDeviceHeight();

        nextIntent.putExtra("setup", setup);
        btnDoCalibration.setVisibility(View.INVISIBLE);
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
            textSensor[0] = findViewById(R.id.textsensor1);
            textSensor[1] = findViewById(R.id.textsensor2);
            textSensor[2] = findViewById(R.id.textsensor3);
            textSensor[3] = findViewById(R.id.textsensor4);
            textSensor[4] = findViewById(R.id.textsensor5);
            textSensor[5] = findViewById(R.id.textsensor6);
        } else { //is right
            isLeft = false;
            btnReconnect.setText("R");
            // Next portion is:
            // Mirroring images from left to right
            imgCop.setScaleX(-1);
            imageFeetOverlay.setScaleX(-1);
            for (int i = 0; i < sensorCount; i++) {
                imageSensorRedStripe[i].setScaleX(-1);
                imageSensorRedStripe[i].setScaleX(-1);
                imageSensorGreenStripe[i].setScaleX(-1);
            }
            //set up sensor values for right(mirrored)
            textSensor[0] = findViewById(R.id.textsensor2);
            textSensor[1] = findViewById(R.id.textsensor1);
            textSensor[2] = findViewById(R.id.textsensor4);
            textSensor[3] = findViewById(R.id.textsensor3);
            textSensor[4] = findViewById(R.id.textsensor6);
            textSensor[5] = findViewById(R.id.textsensor5);
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

        btnReconnect.setOnClickListener(v -> {
            try {
                // create the device and sock for the left device connection
                device = bluetoothAdapter.getRemoteDevice(mac); // use the saved mac address
                Method m = device.getClass().getMethod("createInsecureRfcommSocket", new Class[]{int.class}); // connect using insecure connection
                socket = (BluetoothSocket) m.invoke(device, 1);
                socket.connect();
                thread = new ConnectedThread(socket, resistanceMultiplier, mac); //
                // create a
                // new thread for the connection using our custom thread class
            } catch (Exception e) {
                if (isLeft) {
                    Toast.makeText(getApplicationContext(), "Left Connection Failed", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Right Connection Failed", Toast.LENGTH_LONG).show();
                }
            }
        });


        btnPlay.setOnClickListener(v -> {
            //TODO
            //check visibility logic
            btnDoCalibration.setVisibility(View.VISIBLE);


            if (thread == null) {
                // we failed creating thread so we don't enter the next part
            } else if (thread.getState() != Thread.State.NEW) {
                thread = new ConnectedThread(socket, resistanceMultiplier, mac);
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


            RedYellowGreenStripesNoCalib(1);
            RedYellowGreenStripesNoCalib(2);
            RedYellowGreenStripesNoCalib(3);
            RedYellowGreenStripesNoCalib(4);
            RedYellowGreenStripesNoCalib(5);
            RedYellowGreenStripesNoCalib(0);


            dataReadIsOn = true;
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
        btnGraph.setOnClickListener(v -> {
            //reset button
            //data
            CopVisibility(View.GONE);
            CopCheckVisibility(View.GONE);
            ValuesVisibility(View.GONE);
            GraphVisbility(View.VISIBLE);
            //checks
            visualizationValuesIsOn = false;
            visualizationCOPIsOn = false;
            thread.setCopProcessing(false);
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

            visualizationValuesIsOn = true;
            visualizationCOPIsOn = false;
            thread.setCopProcessing(false);
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
            visualizationValuesIsOn = false;
            visualizationCOPIsOn = true;
            thread.setCopProcessing(true);
            resetThePicture = true;
        });

        btnDoCalibration.setOnClickListener(v -> {
            VisualMainButtonsVisibility(View.VISIBLE);

            //data
            visualizationValuesIsOn = false;
            visualizationCOPIsOn = false;
            thread.setCopProcessing(false);
            //saving data for calibration
            thread.doCalibration();
            canvasToBitmap = new CanvasToBitmap(getBaseContext());

            for (int sensorNumber = 0; sensorNumber < 6; sensorNumber++) {
                setSensorTextValue(sensorNumber);
            }

//            if (checkSavingOn) {
//                tempData.println(tmpSensor.toTxtCalibrationData(timeOfStart));
//            }


            ValuesVisibility(View.GONE);
            CopVisibility(View.GONE);
            GraphVisbility(View.GONE);
            CopCheckVisibility(View.VISIBLE);
        });


        //cop
        if (visualizationCOPIsOn) {
            //arrows for left


            CanvasToBitmap canvasToBitmap = new CanvasToBitmap(getBaseContext());
            Bitmap copBitmap = canvasToBitmap.resultBitmap;
            imgCop.setImageBitmap(copBitmap);

//        }
//        else if (checkDataOn) {
//            //calib done
//            if (thread.calibrationDone()) {
//                thread.resumeBufferReading();//??? helps or not
//                //if - a lot then recalibrate
//
//                for (int sensorNumber = 0; sensorNumber < 6; sensorNumber++) {
//                    setSensorTextPercent(sensorNumber);
//                    RedYellowGreenStripes(sensorNumber);
//                }
//
//            } else {
//                //calib not done
//
//                for (int sensorNumber = 0; sensorNumber < 6; sensorNumber++) {
//                    setSensorTextValue(sensorNumber);
//                    RedYellowGreenStripes(sensorNumber);
//                }
//
//            }
        } else {
            // do nothing

        }






         forRefresh = new Thread() {
            @Override
            public void run() {
                    try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    if(visualizationValuesIsOn) {
                                        visualizationValues();
                                    }else if(visualizationCOPIsOn){
                                        visualizationCOP();
                                        copArrowsVisibility();
                                    }else {

                                    }
                                }
                            });
                    } catch (NullPointerException e) {

                    }
            }
        };
    }


    public static void copArrowsVisibility(){
        if (isLeft) {
            if (thread.sensor.getSensorPercent()[2] + 20 > thread.sensor.getSensorPercent()[3]) { //cop_sensor3 > cop_sensor4 &&
                imageLeftArrow.setVisibility(View.INVISIBLE);
                imageRightArrow.setVisibility(View.VISIBLE);
                imageDownArrow.setVisibility(View.INVISIBLE);
            } else if (thread.sensor.getSensorPercent()[3] + 20 > thread.sensor.getSensorPercent()[2]) { //cop_sensor4 > cop_sensor3 &&
                imageLeftArrow.setVisibility(View.VISIBLE);
                imageRightArrow.setVisibility(View.INVISIBLE);
                imageDownArrow.setVisibility(View.INVISIBLE);
            } else {
                imageLeftArrow.setVisibility(View.INVISIBLE);
                imageRightArrow.setVisibility(View.INVISIBLE);
                imageDownArrow.setVisibility(View.VISIBLE);
            }
        }
        //arrows for right
        if (!isLeft) {
            if (thread.sensor.getSensorPercent()[2] + 20 > thread.sensor.getSensorPercent()[3]) { //cop_sensor3 > cop_sensor4
                imageLeftArrow.setVisibility(View.INVISIBLE);
                imageRightArrow.setVisibility(View.VISIBLE);
                imageDownArrow.setVisibility(View.INVISIBLE);
            } else if (thread.sensor.getSensorPercent()[3] + 20 > thread.sensor.getSensorPercent()[4]) { //cop_sensor4 > cop_sensor3
                imageLeftArrow.setVisibility(View.VISIBLE);
                imageRightArrow.setVisibility(View.INVISIBLE);
                imageDownArrow.setVisibility(View.INVISIBLE);
            } else {
                imageLeftArrow.setVisibility(View.INVISIBLE);
                imageRightArrow.setVisibility(View.INVISIBLE);
                imageDownArrow.setVisibility(View.VISIBLE);
            }
        }
    }



    public static void visualizationValues(){
        try {
            for (int i = 0; i < sensorCount; i++) {
                if (!thread.answerToIfCalibrationIsDone()) {
                    setSensorTextValue(i);
                    RedYellowGreenStripesNoCalib(i);
                } else {
                    setSensorTextPercent(i);
                    RedYellowGreenStripesCalib(i);
                }
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public void visualizationCOP(){
        canvasToBitmap.redraw();
        copBitmap = canvasToBitmap.resultBitmap;
        imgCop.setImageBitmap(copBitmap);
        counterForRefreshPicture++;
        if (counterForRefreshPicture == 50){
            counterForRefreshPicture = 0;
            resetThePicture = true;
        }
    }


    public class CanvasToBitmap extends View {

        //for canvas
        float canvasHeight, canvasWidth, floatDensity, offset;
        int intDensity;
        Paint paint = new Paint();
        Paint paintForCopLines = new Paint();
        Paint paintForAxes = new Paint();
        Paint rawPaint = new Paint();
        int width = imgCop.getWidth();
        int height = imgCop.getHeight();
        Bitmap resultBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Bitmap bitmapLeft = BitmapFactory.decodeResource(getResources(), R.drawable.main_bez_liniy_l);
        Bitmap bitmapRight = BitmapFactory.decodeResource(getResources(), R.drawable.main_bez_liniy_r);
        Canvas canvas = new Canvas(resultBitmap);
        // l = r doesn't matter
        float pictureDensity = bitmapLeft.getDensity(); // 374
        float pictureHeight = bitmapLeft.getHeight(); //6854
        float pictureWidth = bitmapLeft.getWidth();//3855


        public CanvasToBitmap(Context context) {
            super(context);
            draw(canvas);
        }

        public void redraw(){
            draw(canvas);
        }


        @Override
        public void onDraw(Canvas canvas) {
            paintForAxes.setColor(Color.BLUE);
            paintForAxes.setStyle(Paint.Style.FILL);
            paintForAxes.setTextSize(50);
            if (resetThePicture) {
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
                if(isLeft)
                    canvas.drawBitmap(bitmapLeft, offset, 0, rawPaint);
                else canvas.drawBitmap(bitmapRight, offset, 0, rawPaint);
                //x
                canvas.drawLine(xMiddle, 0, xMiddle, canvas.getHeight(), paintForAxes);
                //y
                canvas.drawLine(0, yMiddle, canvas.getWidth(), yMiddle, paintForAxes);
                canvas.save();
                resetThePicture = false;
            }
            //to remove bug with no values in
            if (thread.sensor.getListXValue().size() > 0 && thread.sensor.getListYValue().size() > 0) {
                arrayX5 = arrayOf5(thread.sensor.getListXValue());
                arrayY5 = arrayOf5(thread.sensor.getListYValue());
                arrayX30 = plus5from30(arrayX30, arrayX5);
                arrayY30 = plus5from30(arrayY30, arrayY5);
                thread.sensor.clearListXAndYValue();
            }
            //draw 30 lines
            for (int n = 0; n < arrayX30.length - 1; n++) {
            float pictureXFirst = getPictureXValue(arrayX30[n], canvasWidth);
            float pictureYFirst = getPictureYValue(arrayY30[n], canvasHeight);
            float pictureXLast = getPictureXValue(arrayX30[n + 1], canvasWidth);
            float pictureYLast = getPictureYValue(arrayY30[n + 1], canvasHeight);
            paintForCopLines.setStrokeWidth(2 + n / 3);
            paintForCopLines.setColor(Color.rgb(255 - 180 + n * 3, 0, 120));
            canvas.drawLine(pictureXFirst, pictureYFirst, pictureXLast, pictureYLast, paintForCopLines);
            if (n == 28)
                paintForCopLines.setColor(Color.RED);
            canvas.drawPoint(pictureXLast, pictureYLast, paintForCopLines);
        }
        }
    }


//    public class DrawLayer extends View {
//        public DrawLayer(Context context) {
//            super(context);
//        }
//
//        @Override
//        protected void onDraw(Canvas canvas) {
//            super.onDraw(canvas);
//
//            Paint myPaint = new Paint();
//            myPaint.setColor(Color.rgb(0, 255, 255));
//            myPaint.setTextSize(60);
//            myPaint.setStrokeWidth(50);
//
//
//            Bitmap copBitmap;
//            Canvas myCanvas = new Canvas();
//            if(isLeft){
//                copBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_bez_liniy_l);
//            } else {
//                copBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.main_bez_liniy_r);
//            }
//
////            myCanvas.drawBitmap(copBitmap, 0, 0, null);
//            xMiddle = copBitmap.getWidth() / 2;
//            yMiddle = copBitmap.getHeight() / 2;
//
//
//
//
//
//        }
//
//    }


    private void CopCheckVisibility(int visibility) {
        imageFeetOverlay.setVisibility(visibility);
        for (int i = 0; i<6;i++){
            imageSensorRedStripe[i].setVisibility(visibility);
            imageSensorYellowStripe[i].setVisibility(visibility);
            imageSensorGreenStripe[i].setVisibility(visibility);

            textSensor[i].setVisibility(visibility);
        }
    }

    private void ValuesVisibility(int visibility) {
        imageFeetOverlay.setVisibility(visibility);
        for (int i = 0; i<6;i++){
            imageSensorRedStripe[i].setVisibility(visibility);
            imageSensorYellowStripe[i].setVisibility(visibility);
            imageSensorGreenStripe[i].setVisibility(visibility);
            textSensor[i].setVisibility(visibility);
        }
    }

    // static methods for visualization for ConnectedThread class
    public static void setSensorTextValue(int sensorNumber){
        textSensor[sensorNumber].setText(String.valueOf((int) thread.sensor.getSensorValueArray()[sensorNumber]));
    }


    public static void setSensorTextPercent(int sensorNumber){
        textSensor[sensorNumber].setText(MessageFormat.format("{0} %",
                String.valueOf((int) (thread.sensor.getSensorPercent()[sensorNumber]))));
    }


    public static void RedYellowGreenStripesCalib(int sensorNumber){
        final double sensorValue = thread.sensor.getSensorValueArray()[sensorNumber];
        double qwe = thread.sensor.getCalibrationSensorValueArray()[sensorNumber];
        //if sensor pressed for 33% or less then green
        if (sensorValue <= (qwe / 3) && 0 <= sensorValue) {
            imageSensorGreenStripe[sensorNumber].setVisibility(View.INVISIBLE);
            imageSensorYellowStripe[sensorNumber].setVisibility(View.INVISIBLE);
            imageSensorRedStripe[sensorNumber].setVisibility(View.VISIBLE);
            //if sensor pressed from 34% to 66% then yellow
        } else if (sensorValue <= 2 * (qwe / 3) && (qwe / 3) < sensorValue) {
            imageSensorGreenStripe[sensorNumber].setVisibility(View.INVISIBLE);
            imageSensorYellowStripe[sensorNumber].setVisibility(View.VISIBLE);
            imageSensorRedStripe[sensorNumber].setVisibility(View.INVISIBLE);
            //if sensor is pressed for more than 67% then red
        } else if (sensorValue <= qwe && (2 * qwe / 3) < sensorValue) {
            imageSensorGreenStripe[sensorNumber].setVisibility(View.VISIBLE);
            imageSensorYellowStripe[sensorNumber].setVisibility(View.INVISIBLE);
            imageSensorRedStripe[sensorNumber].setVisibility(View.INVISIBLE);
            //if some strange error just hide them
        } else {
            imageSensorGreenStripe[sensorNumber].setVisibility(View.INVISIBLE);
            imageSensorYellowStripe[sensorNumber].setVisibility(View.INVISIBLE);
            imageSensorRedStripe[sensorNumber].setVisibility(View.INVISIBLE);
        }
    }

    public static void RedYellowGreenStripesNoCalib(int sensorNumber){
        final double sensorValue = thread.sensor.getSensorValueArray()[sensorNumber];
        //if sensor pressed for 33% or less then green
        if (sensorValue <= (maxValue / 3) && 0 <= sensorValue) {
            imageSensorGreenStripe[sensorNumber].setVisibility(View.INVISIBLE);
            imageSensorYellowStripe[sensorNumber].setVisibility(View.INVISIBLE);
            imageSensorRedStripe[sensorNumber].setVisibility(View.VISIBLE);
            //if sensor pressed from 34% to 66% then yellow
        } else if (sensorValue <= 2 * (maxValue / 3) && (maxValue / 3) < sensorValue) {
            imageSensorGreenStripe[sensorNumber].setVisibility(View.INVISIBLE);
            imageSensorYellowStripe[sensorNumber].setVisibility(View.VISIBLE);
            imageSensorRedStripe[sensorNumber].setVisibility(View.INVISIBLE);
            //if sensor is pressed for more than 67% then red
        } else if (sensorValue <= maxValue && (2 * maxValue / 3) < sensorValue) {
            imageSensorGreenStripe[sensorNumber].setVisibility(View.VISIBLE);
            imageSensorYellowStripe[sensorNumber].setVisibility(View.INVISIBLE);
            imageSensorRedStripe[sensorNumber].setVisibility(View.INVISIBLE);
            //if some strange error just hide them
        } else {
            imageSensorGreenStripe[sensorNumber].setVisibility(View.INVISIBLE);
            imageSensorYellowStripe[sensorNumber].setVisibility(View.INVISIBLE);
            imageSensorRedStripe[sensorNumber].setVisibility(View.INVISIBLE);
        }
    }




    private void CopVisibility(int visibility) {
        imgCop.setVisibility(visibility);
        imageLeftArrow.setVisibility(visibility);
        imageRightArrow.setVisibility(visibility);
        imageDownArrow.setVisibility(visibility);
    }

    private void GraphVisbility(int visibility) {

    }

    private void VisualMainButtonsVisibility(int visibility) {
        btnGraph.setVisibility(visibility);
        btnCop.setVisibility(visibility);
        btnValues.setVisibility(visibility);
    }




    private boolean isStoragePermissionGranted() {
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

    private int getDeviceWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private int getDeviceHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private static float[] arrayOf5(ArrayList<Float> arrayList) {
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

    private static float getPictureXValue(float xValue, float canvasWidth) {
        float xValuePixel = (xValue + 1) * canvasWidth / 2;

        return xValuePixel;
    }

    private static float getPictureYValue(float yValue, float canvasHeight) {
        float yValuePixel = (-1 * yValue + 1) * canvasHeight / 2;
        return yValuePixel;
    }

    private static float[] plus5from30(float arrayof30[], float arrayof5[]) {
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


