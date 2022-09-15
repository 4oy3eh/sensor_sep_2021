//package com.example.sensor;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.view.View;
//
//public class CanvasToBitmap extends View {
//
//    //for canvas
//    float xMiddle, yMiddle;
//    float canvasHeight, canvasWidth, floatDensity, offset;
//    int intDensity;
//    boolean isFirstTimeCanvas = true;
//    Paint paint = new Paint();
//    int width = imgCop.getWidth();
//    int height = imgCop.getHeight();
//    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//    Bitmap bitmapLeft = BitmapFactory.decodeResource(getResources(), R.drawable.main_bez_liniy_l);
//    Bitmap bitmapRight = BitmapFactory.decodeResource(getResources(), R.drawable.main_bez_liniy_r);
//
//    float pictureDensity = bitmapLeft.getDensity(); // 374
//    float pictureHeight = bitmapLeft.getHeight(); //6854
//    float pictureWidth = bitmapLeft.getWidth();//3855
//    //l = r doesnt matter
//
//    public CanvasToBitmap(Context context) {
//        super(context);
//        Canvas canvas = new Canvas(bitmap);
//        draw(canvas);
//    }
//
//
//    @Override
//    public void onDraw(Canvas canvas) {
//
//        paint.setColor(Color.BLUE);
//        paint.setStyle(Paint.Style.FILL);
//        paint.setTextSize(50);
//        if (isFirstTimeCanvas) {
//            canvas.setBitmap(bitmap);
//
//            //getting Density to match xml
//
//            canvasHeight = canvas.getHeight();
//            canvasWidth = canvas.getWidth();
//            floatDensity = pictureDensity / pictureHeight * canvasHeight;
//            intDensity = Math.round(floatDensity);
//            canvas.setDensity(intDensity);
//            //placing image in the middle
//            offset = (canvasWidth / 2) - (pictureWidth / pictureDensity * floatDensity / 2);
//            //finding coefficient 1 equals distance between center and first sensor
//            xMiddle = canvasWidth / 2;
//            yMiddle = canvasHeight / 2;
//            if(isLeft)
//                canvas.drawBitmap(bitmapLeft, offset, 0, paint);
//            else canvas.drawBitmap(bitmapRight, offset, 0, paint);
//            //x
//            canvas.drawLine(xMiddle, 0, xMiddle, canvas.getHeight(), paint);
//            //y
//            canvas.drawLine(0, yMiddle, canvas.getWidth(), yMiddle, paint);
//            isFirstTimeCanvas = false;
//        }
//
//
//        paint.setStrokeWidth(7);
//        paint.setColor(Color.BLUE);
//        canvas.drawPoint(xValuePixel, yValuePixel, paint);
//        //to remove bug with no values in
//        if (thread.sensor.getListXValue().size() > 0 && thread.sensor.getListYValue().size() > 0) {
//            arrayX5 = arrayOf5(thread.sensor.getListXValue());
//            arrayY5 = arrayOf5(thread.sensor.getListYValue());
//            arrayX30 = plus5from30(arrayX30, arrayX5);
//            arrayY30 = plus5from30(arrayY30, arrayY5);
////                canvas.drawText(String.valueOf(Array_x_5[0]) + " - " + String.valueOf(Array_x_5[1]), 50, 50, paint);
//            thread.sensor.clearListXAndYValue();
//        }
//        //draw 30 lines
//        for (int n = 0; n < arrayX30.length - 1; n++) {
//            float pictureXFirst = getPictureXValue(arrayX30[n], canvasWidth);
//            float pictureYFirst = getPictureYValue(arrayY30[n], canvasHeight);
//            float pictureXLast = getPictureXValue(arrayX30[n + 1], canvasWidth);
//            float pictureYLast = getPictureYValue(arrayY30[n + 1], canvasHeight);
//            paint.setStrokeWidth(2 + n / 3);
//            paint.setColor(Color.rgb(255 - 180 + n * 3, 0, 120));
//            canvas.drawLine(pictureXFirst, pictureYFirst, pictureXLast, pictureYLast, paint);
//            if (n == 28)
//                paint.setColor(Color.RED);
//            canvas.drawPoint(pictureXLast, pictureYLast, paint);
//        }
//
//
//
//
//
//    }
//
//    private static float getPictureXValue(float xValue, float canvasWidth) {
//        float xValuePixel = (xValue + 1) * canvasWidth / 2;
//
//        return xValuePixel;
//    }
//
//    private static float getPictureYValue(float yValue, float canvasHeight) {
//        float yValuePixel = (-1 * yValue + 1) * canvasHeight / 2;
//        return yValuePixel;
//    }
//
//    private static float[] plus5from30(float arrayof30[], float arrayof5[]) {
//        int i;
//
//        // create a new array of size n+1
//        float newArray[] = new float[30];
//
//        // insert the elements from
//        // the old array into the new array
//        // insert all elements till n
//        // then insert x at n+1
//        for (i = 0; i < 30 - 5; i++)
//            newArray[i] = arrayof30[i + 5];
//        newArray[25] = arrayof5[0];
//        newArray[26] = arrayof5[1];
//        newArray[27] = arrayof5[2];
//        newArray[28] = arrayof5[3];
//        newArray[29] = arrayof5[4];
//
//
//        return newArray;
//    }
//}
