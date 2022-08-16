//package com.example.sensor;
//
//import android.bluetooth.BluetoothSocket;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.Arrays;
//
//public class ConnectedThread_copy extends Thread {
//    private final BluetoothSocket mmSocket;
//    private final InputStream mmInStream;
//    private final OutputStream mmOutStream;
//
//    public ConnectedThread_copy(BluetoothSocket socket) {
//        mmSocket = socket;
//        InputStream tmpIn = null;
//        OutputStream tmpOut = null;
//
//        // Get the input and output streams, using temp objects because
//        // member streams are final
//        try {
//            tmpIn = socket.getInputStream();
//            tmpOut = socket.getOutputStream();
//        } catch (IOException e) {
//        }
//        mmInStream = tmpIn;
//        mmOutStream = tmpOut;
//    }
//
//
//    public void startThread() {
//        start();
//    }
//
//    public void run() {
//
//        int bytesAvailable; // bytes available in the buffer
//        byte stop_byte = (byte) 0x55;
//        byte start_byte = (byte) 0xF0;
//        int pos = 0, tmp;
//        byte[] buf = new byte[1024]; // actually not needed more than 47
//        //byte[] b = new byte[1024]; // actually not needed more than 47
//
//        // Keep looping to listen for received messages
//        startWorker = true;
//        timeOfStart = System.currentTimeMillis();
//        int tm;
//        int[] sVal = new int[6];
//
//
//        while (startWorker) {
//            try {
//                bytesAvailable = mmInStream.available();
//                if (bytesAvailable > 0) // one data packet has minimum 47 bytes
//                {
//                    byte[] b = new byte[bytesAvailable];
//                    bytesAvailable = mmInStream.read(b, 0, bytesAvailable);
//                    for (int i = 0; i < bytesAvailable; i++) {
//                        // put the byte in the buffer
//                        buf[pos] = b[i];
//                        // check if the new byte was the end of a packet
//                        if (buf[pos] == stop_byte && pos > 45) {
//                            if (buf[pos - 46] == start_byte) {
//                                //TODO
//                                //try with nanotime instead of ms
//                                //tm = System.nanoTime();
//
//
//                                tmp = bti(Arrays.copyOfRange(buf, pos - 17, pos - 15));
//                                sVal[0] = (int) (tmp * res_multip);
//                                sensor1 = Double.valueOf(sVal[0]);
//
//
//                                tmp = bti(Arrays.copyOfRange(buf, pos - 15, pos - 13));
//                                sVal[1] = (int) (tmp * res_multip);
//                                sensor2 = Double.valueOf(sVal[1]);
//
//                                tmp = bti(Arrays.copyOfRange(buf, pos - 13, pos - 11));
//                                sVal[2] = (int) (tmp * res_multip);
//                                sensor3 = Double.valueOf(sVal[2]);
//
//                                tmp = bti(Arrays.copyOfRange(buf, pos - 11, pos - 9));
//                                sVal[3] = (int) (tmp * res_multip);
//                                sensor4 = Double.valueOf(sVal[3]);
//
//                                tmp = bti(Arrays.copyOfRange(buf, pos - 9, pos - 7));
//                                sVal[4] = (int) (tmp * res_multip);
//                                sensor5 = Double.valueOf(sVal[4]);
//
//                                tmp = bti(Arrays.copyOfRange(buf, pos - 7, pos - 5));
//                                sVal[5] = (int) (tmp * res_multip);
//                                sensor6 = Double.valueOf(sVal[5]);
//
//                                tmpSensor = new Sensor(sVal[0],sVal[1],sVal[2],sVal[3],sVal[4],sVal[5]);
//                                if (CheckSavingOn) {
//                                    tempData.println(tmpSensor.toTxtData(timeOfStart));
//                                }
//
//
//
////                                            cop_sensor1 = (sensor1 - calib_sensor1);
////                                            cop_sensor2 = (sensor2 - calib_sensor2);
////                                            cop_sensor3 = (sensor3 - calib_sensor3);
////                                            cop_sensor4 = (sensor4 - calib_sensor4);
////                                            cop_sensor5 = (sensor5 - calib_sensor5);
////                                            cop_sensor6 = (sensor6 - calib_sensor6);
//                                //get values for cop
//                                cop_sensor1 = (calib_sensor1 - sensor1);
//                                cop_sensor2 = (calib_sensor2 - sensor2);
//                                cop_sensor3 = (calib_sensor3 - sensor3);
//                                cop_sensor4 = (calib_sensor4 - sensor4);
//                                cop_sensor5 = (calib_sensor5 - sensor5);
//                                cop_sensor6 = (calib_sensor6 - sensor6);
//
//                                //sensetivity bug fix
//                                if (cop_sensor1 < 0) cop_sensor1 = 0;
//                                if (cop_sensor2 < 0) cop_sensor2 = 0;
//                                if (cop_sensor3 < 0) cop_sensor3 = 0;
//                                if (cop_sensor4 < 0) cop_sensor4 = 0;
//                                if (cop_sensor5 < 0) cop_sensor5 = 0;
//                                if (cop_sensor6 < 0) cop_sensor6 = 0;
//                                if (CheckVisualOn) {
//                                    double cop_sensorSum = cop_sensor1 + cop_sensor2 + cop_sensor3 + cop_sensor4 + cop_sensor5 + cop_sensor6;
//                                    double cop_calibSum = calib_sensor1 + calib_sensor2 + calib_sensor3 + calib_sensor4 + calib_sensor5 + calib_sensor6;
//                                    xValue = (float) ((
//                                            cop_sensor1 / calib_sensor1 * Math.cos(Math.toRadians(alphaDegree)) +
//                                                    cop_sensor2 / calib_sensor2 * Math.cos(Math.toRadians(180 - alphaDegree)) +
//                                                    Math.cos(Math.toRadians(alphaDegree)) * cop_sensor3 / calib_sensor3 * Math.cos(Math.toRadians(0)) +
//                                                    Math.cos(Math.toRadians(alphaDegree)) * cop_sensor4 / calib_sensor4 * Math.cos(Math.toRadians(180)) +
//                                                    cop_sensor5 / calib_sensor5 * Math.cos(Math.toRadians(360 - alphaDegree)) +
//                                                    cop_sensor6 / calib_sensor6 * Math.cos(Math.toRadians(180 + alphaDegree)))/6);
//
//                                    yValue = (float) ((
//                                            cop_sensor1 / cop_sensorSum * Math.sin(Math.toRadians(alphaDegree)) +
//                                                    cop_sensor2 / cop_sensorSum * Math.sin(Math.toRadians(180 - alphaDegree)) +
//                                                    Math.cos(Math.toRadians(alphaDegree)) * cop_sensor3 / cop_sensorSum * Math.sin(Math.toRadians(0)) +
//                                                    Math.cos(Math.toRadians(alphaDegree)) * cop_sensor4 / cop_sensorSum * Math.sin(Math.toRadians(180)) +
//                                                    cop_sensor5 / cop_sensorSum * Math.sin(Math.toRadians(360 - alphaDegree)) +
//                                                    cop_sensor6 / cop_sensorSum * Math.sin(Math.toRadians(180 + alphaDegree)))/6);
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
//                                    List_xValue.add(xValue);
//                                    List_yValue.add(yValue);
//
//                                }
//
//
//
//                                pos = 0;
//                            }
//                        } else { // increment the counter
//                            if (pos < 1023) pos++; // safety measure
//                            else pos = 0;
//                        }
//                    }
//                }
//
//            } catch (IOException e) {
//                startWorker = false;
//                break;
//            }
//        }
//    }
//
//    /* Call this from the main activity to send data to the remote device */
//    public void write(String input) {
//        byte[] bytes = input.getBytes();           //converts entered String into bytes
//        try {
//            mmOutStream.write(bytes);
//        } catch (IOException e) {
//        }
//    }
//
//
//}