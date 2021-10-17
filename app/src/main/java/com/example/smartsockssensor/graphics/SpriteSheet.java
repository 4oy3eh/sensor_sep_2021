package com.example.smartsockssensor.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import com.example.smartsockssensor.R;

public class SpriteSheet {
    private static Bitmap bitmap;
    private static int deviceH;
    private static int deviceW;
    private static float ratioH;


    public SpriteSheet(Context context) {
        this.deviceH = getDeviceHeight(); //1600
        this.deviceW = getDeviceWidth(); //900
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.main_bez_liniy_l, bitmapOptions);


        ratioH = deviceH/bitmap.getHeight(); // 1600/900
        int qwe =  bitmap.getWidth();
        qwe = (int)(qwe*ratioH);
        bitmap = Bitmap.createScaledBitmap(bitmap,qwe,getDeviceHeight(),false);
    }

    public static Bitmap getBitmap() {
        return bitmap;
    }

    public Sprite getFeetSprite(){
//        return new Sprite(this, new Rect(0,0,200,469));
        return new Sprite(this, new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()));

    }

    public int getDeviceWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public int getDeviceHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }
}
