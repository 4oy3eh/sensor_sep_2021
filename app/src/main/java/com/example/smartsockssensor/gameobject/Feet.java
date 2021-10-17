package com.example.smartsockssensor.gameobject;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.content.res.Resources;

import com.example.smartsockssensor.graphics.Sprite;

// showing the feet object
public class Feet {

    public Sprite sprite;

    public Feet(Context context,Sprite sprite){
        this.sprite = sprite;
    }


    public void draw(Canvas canvas) {
        sprite.draw(canvas);
    }


}
