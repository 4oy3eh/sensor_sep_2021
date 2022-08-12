package com.example.sensor;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;

public class ConnectedThread extends Thread {
    private final InputStream INPUTSTREAM;
    private final OutputStream OUTPUTSTREAM;
    private final double mmResistanceMultiplier;
    private Sensor headSensor;
    private PrintWriter mmDataWriter;
    private boolean CopProcessing;
    private boolean startWorker;

    public ConnectedThread(BluetoothSocket socket, double resistanceMultiplier, PrintWriter dataWriter) {
        mmResistanceMultiplier = resistanceMultiplier;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        mmDataWriter = dataWriter;
        //Setting a angle
        //important angle for our future cop calculations
        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
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
        byte stopByte = (byte) 0x55;
        byte startByte = (byte) 0xF0;
        int pos = 0, tmp;
        byte[] buf = new byte[1024]; // actually not needed more than 47

        // Keep looping to listen for received messages
        startWorker = true;
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
                        if (buf[pos] == stopByte && pos > 45) {
                            if (buf[pos - 46] == startByte) {


                                // Buffer position 30 - 32 sensor 1
                                // Buffer position 32 - 34 sensor 2
                                // Buffer position 34 - 36 sensor 3
                                // Buffer position 36 - 38 sensor 4
                                // Buffer position 38 - 40 sensor 5
                                // Buffer position 40 - 42 sensor 6
                                for (int IndentValue=17, k=0;k<6;k++,
                                        IndentValue-=2){//k<6 6 sensors;
                                    // packege is being tracked by end bytes
                                    // end byte - 17 till end bye - 15 is 1st sensor
                                    // next sensors following by using 2 bytes each
                                    tmp = bti(Arrays.copyOfRange(buf, pos-IndentValue,
                                            pos-IndentValue-2));
                                    headSensor.setSensor(k+1,tmp * mmResistanceMultiplier);
                                }

                                //TODO
                                //idk figure out something
//                                if (CheckSavingOn) {
                                if(false){
                                    mmDataWriter.println(headSensor.toTxtData(timeOfStart));
                                }


                                if(CopProcessing){// trigger i/o for COP button enabled
                                    //TODO check if callibration values are set

                                    //Calibration
                                    headSensor.xValueMethod2();
                                    headSensor.yValueMethod1();
                                }
                                pos = 0;
                            }
                        } else { // increment the counter
                            if (pos < 1023) pos++; // safety measure
                            else pos = 0;
                        }
                    }
                }
            } catch (IOException e) {
                startWorker = false;
                break;
            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(String input) {
        byte[] bytes = input.getBytes();           //converts entered String into bytes
        try {
            OUTPUTSTREAM.write(bytes);
        } catch (IOException e) {
        }
    }

    private static int bti(byte[] b) {
        return ((b[1] & 0xFF) << 8) | (b[0] & 0xFF);
    }

    public void setThreadSensor(int sensorNumber, double sensorValue){
        headSensor.setSensor(sensorNumber,sensorValue);
    }

    public void setCopProcessingTrue(){
        CopProcessing = true;
    }

    public void setCopProcessingFalse(){
        CopProcessing = false;
    }

    public void stopBufferReading(){
        startWorker = false;
    }

    public void resumeBufferReading(){
        startWorker = true;
    }
}

