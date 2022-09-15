package com.example.sensor;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;


public class ConnectedThread extends Thread {
    private   InputStream INPUTSTREAM;
    private   OutputStream OUTPUTSTREAM;
    private final double RESISTANCE_MULTIPLIER;
    public Sensor sensor;
    private PrintWriter mmDataWriter;



    private boolean CopProcessing = false;
    private final byte stopByte;
    private final byte startByte;
    private boolean startWorker;
    private final int LENGTH_DATA_PACKET;
    private boolean calibrationDoneTrigger = false;



    public ConnectedThread(BluetoothSocket socket, double resistanceMultiplier,
                           String sensorName) {
        RESISTANCE_MULTIPLIER = resistanceMultiplier;
        stopByte = (byte) 0x55;
        startByte = (byte) 0xF0;
        LENGTH_DATA_PACKET = 47;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        sensor = new Sensor(sensorName);

        //Setting a angle
        //important angle for our future cop calculations
        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (Exception e) {
            //IOException should be most common
            //NullPointer exception possible
            System.out.println("Error " + e);
        }
        INPUTSTREAM = tmpIn;
        OUTPUTSTREAM = tmpOut;
    }


    public void startThread() {
        start();
    }

    public void run() {

        int bytesAvailable; // bytes available in the buffer
        int pos = 0, tmp;
        byte[] buf = new byte[1024]; // actually not needed more than 47

        // Keep looping to listen for received messages
        boolean startWorker = true;
        long timeOfStart = System.currentTimeMillis();
        //TODO
        //try with nanotime instead of ms because sometimes you get more than 1 data in 1 ms
        //tm = System.nanoTime();

        //TODO
        //remake logic
        //make more fluent
        while (startWorker) {
            try {
                bytesAvailable = INPUTSTREAM.available();
                if (bytesAvailable > 0) // one data packet has minimum 47 bytes
                {
                    byte[] b = new byte[bytesAvailable];
                    bytesAvailable = INPUTSTREAM.read(b, 0, bytesAvailable);
                    for (int i = 0; i < bytesAvailable; i++) {
                        // put the byte in the buffer
                        buf[pos] = b[i];
                        // check if the new byte was the end of a packet
                        if (buf[pos] == stopByte && pos > LENGTH_DATA_PACKET - 2) {
                            if (buf[pos - (LENGTH_DATA_PACKET - 1)] == startByte) {

                                // Buffer position 30 - 31 sensor 1
                                // Buffer position 32 - 33 sensor 2
                                // Buffer position 34 - 35 sensor 3
                                // Buffer position 36 - 37 sensor 4
                                // Buffer position 38 - 39 sensor 5
                                // Buffer position 40 - 41 sensor 6
                                // Buffer position 42 - 43 sensor 7
                                // Buffer position 44 - 45 sensor 8




                                for (int IndentValue = 17, sensorCount = 0; sensorCount < 6; sensorCount++,
                                        IndentValue-=2){
                                    //sensorCount<6 6 sensors; if need 8 change to 8
                                    // package is being tracked by end bytes
                                    // end byte - 17 = 47 - 17 = 30
                                    // until end bye - 15 - 47 - 15 = 32 is 1st sensor
                                    // next sensors following by using 2 bytes each
                                    tmp = bti(Arrays.copyOfRange(buf, pos - IndentValue,
                                            pos - (IndentValue - 2)));
                                    sensor.setSensor(sensorCount + 1,
                                            tmp * RESISTANCE_MULTIPLIER);

                                }



                                if(answerToIfCalibrationIsDone()){
                                    sensor.doSignalNormalization();
                                }

                                if(CopProcessing){
                                    sensor.addValueListXValue();
                                    sensor.addValueListYValue();
                                }

                                // single refresh after analysing 1 data segment
                                Visual.forRefresh.run();


                                //TODO
                                //idk figure out something
//                                if (CheckSavingOn) {
                                if(false){
                                    mmDataWriter.println(sensor.toTxtData(timeOfStart));
                                }

                                // trigger i/o for COP button enabled

                                pos = 0;
                            }
                        } else { // increment the counter
                            if (pos < 1023) pos++; // safety measure
                            else pos = 0;
                        }
                    }
                }

            } catch (IOException e) {
                //IOException
                startWorker = false;
                break;
            }
        }
    }


    // Call this from the main activity to send data to the remote device
    public void write(String input) {
        //converts entered String into bytes
        byte[] bytes = input.getBytes();
        try {
            OUTPUTSTREAM.write(bytes);
        } catch (IOException e) {
            //
        }
    }
    //byte to int
    private static int bti(byte[] b) {
        // int b[1] + b[0]
        return ((b[1] & 0xFF) << 8) | (b[0] & 0xFF);
    }

    public void doCalibration(){
        calibrationDoneTrigger = true;
        sensor.doCalibration();
    }

    public boolean answerToIfCalibrationIsDone(){
        return calibrationDoneTrigger;
    }

    public void setCopProcessing(boolean copProcessing) {
        CopProcessing = copProcessing;
    }


}

