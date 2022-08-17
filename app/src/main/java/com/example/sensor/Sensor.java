package com.example.sensor;

public class Sensor {
    private double sensor1;
    private double sensor2;
    private double sensor3;
    private double sensor4;
    private double sensor5;
    private double sensor6;
    private double calibrationSensor1;
    private double calibrationSensor2;
    private double calibrationSensor3;
    private double calibrationSensor4;
    private double calibrationSensor5;
    private double calibrationSensor6;
    private double normalizedSensor1;
    private double normalizedSensor2;
    private double normalizedSensor3;
    private double normalizedSensor4;
    private double normalizedSensor5;
    private double normalizedSensor6;
    private final int alphaDegree = 75;



//for writing data
    public String toTxtData(long timeOfStart) {
        int time = (int) (System.currentTimeMillis() - timeOfStart);
        return time + "\t" +sensor1 + "\t" + sensor2 + "\t" + sensor3 + "\t" + sensor4 + "\t" +
        sensor4 + "\t" + sensor5 + "\t" + sensor6 ;
    }


    //for cop writing data
    public String toTxtCalibrationData(long timeOfStart) {
        int time = (int) (System.currentTimeMillis() - timeOfStart);

        return time + "\t###CALIBRATION##DONE#" + "\n" + time + "\t" +sensor1 + "\t" + sensor2 +
        "\t" + sensor3 + "\t" + sensor4 + "\t" + sensor4 + "\t" + sensor5 + "\t" + sensor6 + "\n"
        + time + "\t###VALUES##NEXT#";
    }

    public String toString() {
        return "sensor 1 = " + sensor1 + "; sensor 2 = " + sensor2 + "; sensor 3 = " + sensor3 +
        "; sensor 4 = " + sensor4  + "; sensor 5 = " + sensor5  + "; sensor 6 = " + sensor6;
    }

    public Sensor(double sensor1, double sensor2, double sensor3, double sensor4,
                       double sensor5, double sensor6){
        this.sensor1 = sensor1;
        this.sensor2 = sensor2;
        this.sensor3 = sensor3;
        this.sensor4 = sensor4;
        this.sensor5 = sensor5;
        this.sensor6 = sensor6;
    }

//getters setters
<<<<<<< HEAD
    public double getSensorValue(int sensorNumber) {
=======
    public double getSensor(int sensorNumber) {
>>>>>>> 2d18ab6ed3e92668b3db6814e26ff339534cd660
        double result;
        switch(sensorNumber){
            case 1:
                result = sensor1;
                break;
            case 2:
                result = sensor2;
                break;
            case 3:
                result = sensor3;
                break;
            case 4:
                result = sensor4;
                break;
            case 5:
                result = sensor5;
                break;
            case 6:
                result = sensor6;
                break;
            default:
                result = 0;
                //TODO
                //some error
                System.out.print("Error: choose sensor 1-6 only");
                break;
        }
        return result;
    }

<<<<<<< HEAD
 public double getCalibrationSensorValue(int sensorNumber) {
        double result;
        switch(sensorNumber){
            case 1:
                result = calibrationSensor1;
                break;
            case 2:
                result = calibrationSensor2;
                break;
            case 3:
                result = calibrationSensor3;
                break;
            case 4:
                result = calibrationSensor4;
                break;
            case 5:
                result = calibrationSensor5;
                break;
            case 6:
                result = calibrationSensor6;
                break;
            default:
                result = 0;
                //TODO
                //some error
                System.out.print("Error: choose sensor 1-6 only");
                break;
        }
        return result;
    }

=======
>>>>>>> 2d18ab6ed3e92668b3db6814e26ff339534cd660


    public void setSensor(int sensorNumber, double sensorValue) {
        switch(sensorNumber){
            case 1:
                this.sensor1 = sensorValue;
                break;
            case 2:
                this.sensor2 = sensorValue;
                break;
            case 3:
                this.sensor3 = sensorValue;
                break;
            case 4:
                this.sensor4 = sensorValue;
                break;
            case 5:
                this.sensor5 = sensorValue;
                break;
            case 6:
                this.sensor6 = sensorValue;
                break;
            default:
                //TODO
                //some error
                System.out.print("Error: choose sensor 1-6 only");
                break;
        }
    }

<<<<<<< HEAD
    private void doCalibration(){//lift foot and save values for real minimum of pressure done by
=======
    public void doCalibration(){//lift foot and save values for real minimum of pressure done by
>>>>>>> 2d18ab6ed3e92668b3db6814e26ff339534cd660
        // your foot to process it further
        this.calibrationSensor1 = sensor1;
        this.calibrationSensor2 = sensor2;
        this.calibrationSensor3 = sensor3;
        this.calibrationSensor4 = sensor4;
        this.calibrationSensor5 = sensor5;
        this.calibrationSensor6 = sensor6;
    }
    // getting the real pressure applied by reducing actual value from calibrated zero pressure
    // value
<<<<<<< HEAD
    private void doSignalNormalization(){//must be more than 0
=======
    public void doSignalNormalization(){//must be more than 0
>>>>>>> 2d18ab6ed3e92668b3db6814e26ff339534cd660
        //actually if less than zero calibration was done bad or sensor showing inaccurate data
        this.normalizedSensor1 = Math.max(0,calibrationSensor1 - sensor1);
        this.normalizedSensor2 = Math.max(0,calibrationSensor2 - sensor2);
        this.normalizedSensor3 = Math.max(0,calibrationSensor3 - sensor3);
        this.normalizedSensor4 = Math.max(0,calibrationSensor4 - sensor4);
        this.normalizedSensor5 = Math.max(0,calibrationSensor5 - sensor5);
        this.normalizedSensor6 = Math.max(0,calibrationSensor6 - sensor6);
    }

<<<<<<< HEAD
    private double normalizationSum(){
        return normalizedSensor1 + normalizedSensor2 + normalizedSensor3 + normalizedSensor4 + normalizedSensor5 + normalizedSensor6;
    }

    private double calibrationSum(){
=======
    public double normalizationSum(){
        return normalizedSensor1 + normalizedSensor2 + normalizedSensor3 + normalizedSensor4 + normalizedSensor5 + normalizedSensor6;
    }

    public double calibrationSum(){
>>>>>>> 2d18ab6ed3e92668b3db6814e26ff339534cd660
        return calibrationSensor1 + calibrationSensor2 + calibrationSensor3 + calibrationSensor4 + calibrationSensor5 + calibrationSensor6;
    }

    //not used too much power needed
    //to count it every time of inforamtion updated
    //get x value for cop
    public float xValueMethod1(){//could be applied for y
        double normalizedSensorSum = normalizationSum();
        return (float) ((
                normalizedSensor1 / normalizedSensorSum * Math.cos(Math.toRadians(alphaDegree)) +
                        normalizedSensor2 / normalizedSensorSum * Math.cos(Math.toRadians(180 - alphaDegree)) +
                        Math.cos(Math.toRadians(alphaDegree)) * normalizedSensor3 / normalizedSensorSum * Math.cos(Math.toRadians(0)) +
                        Math.cos(Math.toRadians(alphaDegree)) * normalizedSensor4 / normalizedSensorSum * Math.cos(Math.toRadians(180)) +
                        normalizedSensor5 / normalizedSensorSum * Math.cos(Math.toRadians(360 - alphaDegree)) +
                        normalizedSensor6 / normalizedSensorSum * Math.cos(Math.toRadians(180 + alphaDegree))));
    }

    public float xValueMethod2(){
        return (float) ((
                normalizedSensor1 / calibrationSensor1 * Math.cos(Math.toRadians(alphaDegree)) +
                        normalizedSensor2 / calibrationSensor2 * Math.cos(Math.toRadians(180 - alphaDegree)) +
                        Math.cos(Math.toRadians(alphaDegree)) * normalizedSensor3 / calibrationSensor3 * Math.cos(Math.toRadians(0)) +
                        Math.cos(Math.toRadians(alphaDegree)) * normalizedSensor4 / calibrationSensor4 * Math.cos(Math.toRadians(180)) +
                        normalizedSensor5 / calibrationSensor5 * Math.cos(Math.toRadians(360 - alphaDegree)) +
                        normalizedSensor6 / calibrationSensor6 * Math.cos(Math.toRadians(180 + alphaDegree))));
    }


    //not used too much power needed
    //to count it every time of inforamtion updated
    //get y value for cop
    public float yValueMethod1(){
        double normalizedSensorSum = normalizationSum();
        return (float) (

                sensor1 / normalizedSensorSum * Math.sin(Math.toRadians(alphaDegree)) +
                        sensor2 / normalizedSensorSum * Math.sin(Math.toRadians(180 - alphaDegree)) +
                        Math.cos(Math.toRadians(alphaDegree)) * sensor3 / normalizedSensorSum * Math.sin(Math.toRadians(0)) +
                        Math.cos(Math.toRadians(alphaDegree)) * sensor4 / normalizedSensorSum * Math.sin(Math.toRadians(180)) +
                        sensor5 / normalizedSensorSum * Math.sin(Math.toRadians(360 - alphaDegree)) +
                        sensor6 / normalizedSensorSum * Math.sin(Math.toRadians(180 + alphaDegree)));
    }
}
