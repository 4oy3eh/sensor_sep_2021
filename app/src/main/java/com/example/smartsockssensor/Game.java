package com.example.smartsockssensor;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.smartsockssensor.gameobject.Cop_picture;
import com.example.smartsockssensor.gameobject.Feet;
import com.example.smartsockssensor.gameobject.Sensor;

import com.example.smartsockssensor.graphics.SpriteSheet;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.lang.reflect.Method;

public class Game extends SurfaceView implements SurfaceHolder.Callback {

    private final Cop_picture copPicture;
    private GameLoop gameLoop;
    private Feet feetPicture;
    private Context context;
    private Sensor sensor;

    public Game(Context context){
        super(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        SpriteSheet spriteSheet = new SpriteSheet(context);
        feetPicture = new Feet(context,spriteSheet.getFeetSprite());
        copPicture = new Cop_picture(getContext(),500,500);
        this.context = context;
        gameLoop = new GameLoop(this, surfaceHolder);
        setFocusable(true);
        //bluetooth
        BluetoothAdapter mBlueAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothSocket socket_left;
        ConnectedThread thread_left;
        int thread_left_idx = 1;
        try {
            // create the device and sock for the left device connection
            BluetoothDevice device_left = mBlueAdapter.getRemoteDevice("00:16:A4:48:8E:E6"); // use the saved mac address
            Method m = device_left.getClass().getMethod("createInsecureRfcommSocket", new Class[]{int.class}); // connect using insecure connection
            socket_left = (BluetoothSocket) m.invoke(device_left, 1);
            socket_left.connect();
            thread_left = new ConnectedThread(socket_left, thread_left_idx); // create a new thread for the connection using our custom thread class
        } catch (Exception e) {
            Toast.makeText(context.getApplicationContext(), "00:16:A4:48:8E:E6" + " Connection to left sock has failed", Toast.LENGTH_LONG).show();
        }


    }
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        feetPicture.draw(canvas);
        drawFPS(canvas);
        drawUPS(canvas);
        Cop_picture.draw(canvas);
    }

    public void drawUPS(Canvas canvas){
        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(context,R.color.design_default_color_primary));
        paint.setTextSize(30);
        canvas.drawText("UPS: " + averageUPS, 50, 40, paint);
    }

    public void drawFPS(Canvas canvas){
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(context,R.color.design_default_color_primary));
        paint.setTextSize(30);
        canvas.drawText("FPS: " + averageFPS, 50, 80, paint);
    }


    public void pause() {
    }

    public void update() {
        copPicture.update();
    }
}
