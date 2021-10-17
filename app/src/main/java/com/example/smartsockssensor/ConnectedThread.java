package com.example.smartsockssensor;

import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import com.example.smartsockssensor.gameobject.Sensor;

public class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private final int threadIdx;
    private boolean startWorker;
    private long timeOfStart;
    private int sideSelection;
    private double res_multip;
    private double maxvalue;
    private final double[] resistance_multiplier = new double[]{0.03125, 0.063, 0.125, 0.25, 0.50, 1.00, 2.00, 4.00, 8.00, 16.00};
    private final int[] max_value = new int[]{2000, 4000, 8000, 16000, 32000, 64000, 128000, 256000, 512000, 1024000};
    private Sensor sensor;

    public ConnectedThread(BluetoothSocket socket, int idx) {
        mmSocket = socket;
        threadIdx = idx;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;
        sideSelection = 1;
        res_multip = resistance_multiplier[5];
        maxvalue = max_value[5];

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
        }
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }


    public void startThread() {
        start();
    }

    public void run() {

        int bytesAvailable; // bytes available in the buffer
        byte stop_byte = (byte) 0x55;
        byte start_byte = (byte) 0xF0;
        int pos = 0, tmp;
        byte[] buf = new byte[1024]; // actually not needed more than 47
        //byte[] b = new byte[1024]; // actually not needed more than 47

        // Keep looping to listen for received messages
        startWorker = true;
        timeOfStart = System.currentTimeMillis();
        int tm;
        int[] sVal = new int[6];


        while (startWorker) {
            try {
                bytesAvailable = mmInStream.available();
                if (bytesAvailable > 0) // one data packet has minimum 47 bytes
                {
                    byte[] b = new byte[bytesAvailable];
                    bytesAvailable = mmInStream.read(b, 0, bytesAvailable);
                    for (int i = 0; i < bytesAvailable; i++) {
                        // put the byte in the buffer
                        buf[pos] = b[i];
                        // check if the new byte was the end of a packet
                        if (buf[pos] == stop_byte && pos > 45) {
                            if (buf[pos - 46] == start_byte) {

                                //tm = System.nanoTime();
                                if (threadIdx == sideSelection) {

                                    tmp = bti(Arrays.copyOfRange(buf, pos - 17, pos - 15));
                                    sVal[0] = (int) (tmp * res_multip);
                                    sensor.setSensor1(sVal[0]);

                                    tmp = bti(Arrays.copyOfRange(buf, pos - 15, pos - 13));
                                    sVal[1] = (int) (tmp * res_multip);
                                    sensor.setSensor2(sVal[1]);

                                    tmp = bti(Arrays.copyOfRange(buf, pos - 13, pos - 11));
                                    sVal[2] = (int) (tmp * res_multip);
                                    sensor.setSensor3(sVal[2]);

                                    tmp = bti(Arrays.copyOfRange(buf, pos - 11, pos - 9));
                                    sVal[3] = (int) (tmp * res_multip);
                                    sensor.setSensor4(sVal[3]);

                                    tmp = bti(Arrays.copyOfRange(buf, pos - 9, pos - 7));
                                    sVal[4] = (int) (tmp * res_multip);
                                    sensor.setSensor5(sVal[4]);

                                    tmp = bti(Arrays.copyOfRange(buf, pos - 7, pos - 5));
                                    sVal[5] = (int) (tmp * res_multip);
                                    sensor.setSensor6(sVal[5]);

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
            mmOutStream.write(bytes);
        } catch (IOException e) {
        }
    }

    private static int bti(byte[] b) {
        return ((b[1] & 0xFF) << 8) | (b[0] & 0xFF);
    }


}
