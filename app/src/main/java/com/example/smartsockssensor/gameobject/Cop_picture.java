package com.example.smartsockssensor.gameobject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;


import androidx.core.content.ContextCompat;

import com.example.smartsockssensor.R;

// showing the current point for cop
public class Cop_picture extends GameObject{


    private static float positionX;
    private static float positionY;
    private static Paint paint;

    public Cop_picture(Context context, float positionX, float positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.design_default_color_primary));
        paint.setStrokeWidth(7);
    }

// what will be drawn per frame
    public static void draw(Canvas canvas) {
        canvas.drawPoint(positionX, positionY, paint);
    }

    public void update() {
    }
}
