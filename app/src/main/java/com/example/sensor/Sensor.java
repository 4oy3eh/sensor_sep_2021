package com.example.sensor;

import android.os.Build;

import java.util.ArrayList;
import java.util.Arrays;

public class Sensor {
    private final String sensorName;
    private double[] sensorValue;
    private double[] calibrationSensorValue;
    private double[] normalizedSensorValue;
    private static final int ALPHA_DEGREE = 75;

    public static int getMaxSensorCount() {
        return MAX_SENSOR_COUNT;
    }

    private static final int MAX_SENSOR_COUNT = 8;
    private ArrayList<Float> listXValue;
    private ArrayList<Float> listYValue;

    public ArrayList<Float> getListXValue() {
        return listXValue;
    }

    public ArrayList<Float> getListYValue() {
        return listYValue;
    }

    public void addValueListXValue() {
        this.listXValue.add(xValueMethod2());
    }

    public void addValueListYValue() {
        this.listYValue.add(yValueMethod2());
    }

    public void clearListXAndYValue(){
        this.listXValue.clear();
        this.listYValue.clear();
    }




    public Sensor(String name){
        this.sensorName = name;
        this.sensorValue = new double[MAX_SENSOR_COUNT];
        this.calibrationSensorValue = new double[MAX_SENSOR_COUNT];
        this.normalizedSensorValue = new double[MAX_SENSOR_COUNT];
        this.listXValue = new ArrayList<>();
        this.listYValue = new ArrayList<>();
    }

//for writing data
    public String toTxtData(long timeOfStart) {
        int time = (int) (System.currentTimeMillis() - timeOfStart);
        String ans = String.valueOf(time);
        for (int i = 0; i < MAX_SENSOR_COUNT; i++)
            ans += "\t" + sensorValue[i];
        return  ans;
    }


    //for cop writing data
    public String toTxtCalibrationData(long timeOfStart) {
        int time = (int) (System.currentTimeMillis() - timeOfStart);
        String ans = time + "\t###CALIBRATION##DONE#" + "\n" + time;
        for (int i = 0; i < MAX_SENSOR_COUNT; i++)
            ans += "\t" + sensorValue[i];
        ans += "\n" + time + "\t###VALUES##NEXT#";
        return  ans;
    }

    public String toString() {
        String ans = "";
        for (int i = 0; i < MAX_SENSOR_COUNT; i++)
            ans += sensorValue[i] + " ";
        return ans;
    }

    public String makeBreak(){
        return "---";
    }



//getters setters

    public int[] getSensorPercent(){
        int[] ans = new int[MAX_SENSOR_COUNT];
        for (int i = 0; i < MAX_SENSOR_COUNT; i++)
            ans[i] = (int) (normalizedSensorValue[i] / calibrationSensorValue[i] * 100);
        return ans;
    }

    public double[] getSensorValueArray() {
        return sensorValue;
    }

    public double[] getCalibrationSensorValueArray() {
        return calibrationSensorValue;
    }

    public double[] getNormalizedSensorValueArray() {
        return normalizedSensorValue;
    }

    // calibration value setter
    public void doCalibration(){
        //lift foot and save values for real minimum of pressure done by
        // your foot to process it further
        for (int i = 0; i < MAX_SENSOR_COUNT; i++)
            calibrationSensorValue[i] = sensorValue[i];
    }

    public void setSensor(int sensorNumber, double sensorValue) {
        if (sensorNumber > MAX_SENSOR_COUNT){
            System.out.print("Error: choose sensor 1-8 only");
            return;
        }

        switch(sensorNumber){
            case 1:
                this.sensorValue[0] = sensorValue;
                break;
            case 2:
                this.sensorValue[1] = sensorValue;
                break;
            case 3:
                this.sensorValue[2] = sensorValue;
                break;
            case 4:
                this.sensorValue[3] = sensorValue;
                break;
            case 5:
                this.sensorValue[4] = sensorValue;
                break;
            case 6:
                this.sensorValue[5] = sensorValue;
                break;
            case 7:
                this.sensorValue[6] = sensorValue;
                break;
            case 8:
                this.sensorValue[7] = sensorValue;
                break;
            default:
                //TODO
                //some error
                System.out.print("Error: choose sensor 1-8 only");
                break;
        }
    }

    // normalization value setter
    // getting the real pressure applied by reducing actual value from calibrated zero pressure
    // value
    public void doSignalNormalization(){
        //must be more than 0
        //actually if less than zero calibration was done bad or sensor showing inaccurate data
        for (int i = 0; i < MAX_SENSOR_COUNT; i++)
            this.normalizedSensorValue[i] = Math.max(0,calibrationSensorValue[i] - sensorValue[i]);
    }

    private double normalizationSum(){
            double normalizedSum = 0;
            for(int i = 0; i < MAX_SENSOR_COUNT; i++)
                normalizedSum += normalizedSensorValue[i];
            return normalizedSum;
    }


    private double calibrationSum(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Arrays.stream(calibrationSensorValue).count();
        } else {
            double calibrationSum = 0;
            for(int i = 0; i < MAX_SENSOR_COUNT; i++)
                calibrationSum += calibrationSensorValue[i];
            return calibrationSum;
        }
    }

    //not used too much power needed
    //to count it every time of inforamtion updated
    //get x value for cop
    public float xValueMethod1(){//could be applied for y
        double normalizedSensorSum = normalizationSum();
        return (float) (
                normalizedSensorValue[0] / normalizedSensorSum * Math.cos(Math.toRadians(ALPHA_DEGREE)) +
                        normalizedSensorValue[1] / normalizedSensorSum * Math.cos(Math.toRadians(180 - ALPHA_DEGREE)) +
                        Math.cos(Math.toRadians(ALPHA_DEGREE)) * normalizedSensorValue[2] / normalizedSensorSum * Math.cos(Math.toRadians(0)) +
                        Math.cos(Math.toRadians(ALPHA_DEGREE)) * normalizedSensorValue[3] / normalizedSensorSum * Math.cos(Math.toRadians(180)) +
                        normalizedSensorValue[4] / normalizedSensorSum * Math.cos(Math.toRadians(360 - ALPHA_DEGREE)) +
                        normalizedSensorValue[5] / normalizedSensorSum * Math.cos(Math.toRadians(180 + ALPHA_DEGREE)));
    }

    public float xValueMethod2(){
        double a = Math.cos(Math.toRadians(ALPHA_DEGREE));
        double b = Math.cos(Math.toRadians(180 - ALPHA_DEGREE));
        double c = Math.cos(Math.toRadians(0)) * Math.cos(Math.toRadians(ALPHA_DEGREE));
        double d = Math.cos(Math.toRadians(180)) * Math.cos(Math.toRadians(ALPHA_DEGREE));
        double e = Math.cos(Math.toRadians(360 - ALPHA_DEGREE));
        double f = Math.cos(Math.toRadians(180 + ALPHA_DEGREE));
        float qwe = (float) ((
                normalizedSensorValue[0] / calibrationSensorValue[0] * a +
                        normalizedSensorValue[1] / calibrationSensorValue[1] * b +
                        normalizedSensorValue[2] / calibrationSensorValue[2] * c +
                        normalizedSensorValue[3] / calibrationSensorValue[3] * d +
                        normalizedSensorValue[4] / calibrationSensorValue[4] * e +
                        normalizedSensorValue[5] / calibrationSensorValue[5] * f)/0.75);
        return qwe;
    }

    public float yValueMethod1(){
        double normalizedSensorSum = normalizationSum();
        double a = Math.sin(Math.toRadians(ALPHA_DEGREE));
        double b = Math.sin(Math.toRadians(180 - ALPHA_DEGREE));
        double c = Math.sin(Math.toRadians(0)) * Math.cos(Math.toRadians(ALPHA_DEGREE));
        double d = Math.sin(Math.toRadians(180)) * Math.cos(Math.toRadians(ALPHA_DEGREE));
        double e = Math.sin(Math.toRadians(360 - ALPHA_DEGREE));
        double f = Math.sin(Math.toRadians(180 + ALPHA_DEGREE));
        return (float) (
                normalizedSensorValue[0] / normalizedSensorSum * a +
                        normalizedSensorValue[1] / normalizedSensorSum * b +
                        normalizedSensorValue[2] / normalizedSensorSum * c +
                        normalizedSensorValue[3] / normalizedSensorSum * d +
                        normalizedSensorValue[4] / normalizedSensorSum * e +
                        normalizedSensorValue[5] / normalizedSensorSum * f);
    }

    public float yValueMethod2(){
        double a = Math.sin(Math.toRadians(ALPHA_DEGREE));
        double b = Math.sin(Math.toRadians(180 - ALPHA_DEGREE));
        double c = Math.sin(Math.toRadians(0)) * Math.cos(Math.toRadians(ALPHA_DEGREE));
        double d = 0;
//        double d = Math.sin(Math.toRadians(180)) * Math.cos(Math.toRadians(ALPHA_DEGREE));
        double e = Math.sin(Math.toRadians(360 - ALPHA_DEGREE));
        double f = Math.sin(Math.toRadians(180 + ALPHA_DEGREE));
        float qwe = (float) ((
                        normalizedSensorValue[0] / calibrationSensorValue[0] * a +
                        normalizedSensorValue[1] / calibrationSensorValue[1] * b +
                        normalizedSensorValue[2] / calibrationSensorValue[2] * c +
                        normalizedSensorValue[3] / calibrationSensorValue[3] * d +
                        normalizedSensorValue[4] / calibrationSensorValue[4] * e +
                        normalizedSensorValue[5] / calibrationSensorValue[5] * f)/2);
        return qwe;
    }

    //get y value for cop
    public float yValueMethod3(){
        double normalizedSensorSum = normalizationSum();
        return (float) (

                sensorValue[0] / normalizedSensorSum * Math.sin(Math.toRadians(ALPHA_DEGREE)) +
                        sensorValue[1] / normalizedSensorSum * Math.sin(Math.toRadians(180 - ALPHA_DEGREE)) +
                        Math.cos(Math.toRadians(ALPHA_DEGREE)) * sensorValue[2] / normalizedSensorSum * Math.sin(Math.toRadians(0)) +
                        Math.cos(Math.toRadians(ALPHA_DEGREE)) * sensorValue[3] / normalizedSensorSum * Math.sin(Math.toRadians(180)) +
                        sensorValue[4] / normalizedSensorSum * Math.sin(Math.toRadians(360 - ALPHA_DEGREE)) +
                        sensorValue[5] / normalizedSensorSum * Math.sin(Math.toRadians(180 + ALPHA_DEGREE)));
    }
}
