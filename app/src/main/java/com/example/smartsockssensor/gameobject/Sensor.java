package com.example.smartsockssensor.gameobject;

public class Sensor {
    private int sensor1;
    private int sensor2;
    private int sensor3;
    private int sensor4;
    private int sensor5;
    private int sensor6;

    public Sensor(int sensor1, int sensor2, int sensor3, int sensor4, int sensor5, int sensor6){
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
        this.sensor3 = sensor3;
        this.sensor4 = sensor4;
        this.sensor5 = sensor5;
        this.sensor6 = sensor6;
    }

    //for writing data
    public String toTxtData(long timeOfStart) {
        int time = (int) (System.currentTimeMillis() - timeOfStart);
        return time + "\t" +sensor1 + "\t" + sensor2 + "\t" + sensor3 + "\t" + sensor4 + "\t" + sensor4 + "\t" + sensor5 + "\t" + sensor6 ;
    }
    //get x value for cop
    public float xValue(){
        int sensorSum = sensor1 + sensor2 + sensor3 + sensor4 + sensor5 + sensor6;
        int alphaDegree = 75;
        return (float) (
                sensor1 / sensorSum * Math.cos(Math.toRadians(alphaDegree)) +
                        sensor2 / sensorSum * Math.cos(Math.toRadians(180 - alphaDegree)) +
                        Math.cos(Math.toRadians(alphaDegree)) * sensor3 / sensorSum * Math.cos(Math.toRadians(0)) +
                        Math.cos(Math.toRadians(alphaDegree)) * sensor4 / sensorSum * Math.cos(Math.toRadians(180)) +
                        sensor5 / sensorSum * Math.cos(Math.toRadians(360 - alphaDegree)) +
                        sensor6 / sensorSum * Math.cos(Math.toRadians(180 + alphaDegree)));
    }
    //get y value for cop
    public float yValue(){
        int sensorSum = sensor1 + sensor2 + sensor3 + sensor4 + sensor5 + sensor6;
        int alphaDegree = 75;
        return (float) (
                sensor1 / sensorSum * Math.sin(Math.toRadians(alphaDegree)) +
                        sensor2 / sensorSum * Math.sin(Math.toRadians(180 - alphaDegree)) +
                        Math.cos(Math.toRadians(alphaDegree)) * sensor3 / sensorSum * Math.sin(Math.toRadians(0)) +
                        Math.cos(Math.toRadians(alphaDegree)) * sensor4 / sensorSum * Math.sin(Math.toRadians(180)) +
                        sensor5 / sensorSum * Math.sin(Math.toRadians(360 - alphaDegree)) +
                        sensor6 / sensorSum * Math.sin(Math.toRadians(180 + alphaDegree)));
    }
    public String toString() {
        return "sensor 1 = " + sensor1 + "; sensor 2 = " + sensor2 + "; sensor 3 = " + sensor3 + "; sensor 4 = " + sensor4  + "; sensor 5 = " + sensor5  + "; sensor 6 = " + sensor6;
    }


    //getters setters
    public int getSensor1() {
        return sensor1;
    }

    public void setSensor1(int sensor1) {
        this.sensor1 = sensor1;
    }

    public int getSensor2() {
        return sensor2;
    }

    public void setSensor2(int sensor2) {
        this.sensor2 = sensor2;
    }

    public int getSensor3() {
        return sensor3;
    }

    public void setSensor3(int sensor3) {
        this.sensor3 = sensor3;
    }

    public int getSensor4() {
        return sensor4;
    }

    public void setSensor4(int sensor4) {
        this.sensor4 = sensor4;
    }

    public int getSensor5() {
        return sensor5;
    }

    public void setSensor5(int sensor5) {
        this.sensor5 = sensor5;
    }

    public int getSensor6() {
        return sensor6;
    }

    public void setSensor6(int sensor6) {
        this.sensor6 = sensor6;
    }
}
