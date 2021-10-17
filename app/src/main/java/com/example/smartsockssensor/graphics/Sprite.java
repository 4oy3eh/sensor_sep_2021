package com.example.smartsockssensor.graphics;


import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite {

    private final SpriteSheet spriteSheet;
    private final Rect rect;
    private final float offset;


    public Sprite(SpriteSheet spriteSheet, Rect rect) {
        this.spriteSheet = spriteSheet;
        this.rect = rect;
        //put image in the middle
        this.offset = getDeviceWidth()/2 - rect.right/2;

    }

    public void draw(Canvas canvas) {

        canvas.drawBitmap(SpriteSheet.getBitmap(), offset, 0, null);
    }

    public int getDeviceWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

}