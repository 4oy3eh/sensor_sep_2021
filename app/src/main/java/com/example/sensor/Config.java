package com.example.sensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.content.pm.ActivityInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.SeekBar;

public class Config extends AppCompatActivity {
    boolean singlemode;
    int BTS1 = 6, BTS2 = 7;
    Button btnStartNextActivity;
    SeekBar barBTS1, barBTS2;
    TextView textBTS1, textBTS2, bugTracker;
    String setup;
    Intent macIntent, settingsIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // always portrait
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //bars
        barBTS1 = findViewById(R.id.bar_BTS1);
        barBTS2 = findViewById(R.id.bar_BTS2);
        //text
        bugTracker = findViewById(R.id.bugtrack);
        textBTS1 = findViewById(R.id.txt_BTS1);
        textBTS2 = findViewById(R.id.txt_BTS2);
        //buttons
        btnStartNextActivity = findViewById(R.id.btn_continue);

        settingsIntent = new Intent(this, Visual.class);

        barBTS1.setProgress(4);
        barBTS2.setProgress(4);

        macIntent = getIntent();
        setup = macIntent.getStringExtra("setup");
        settingsIntent.putExtra("setup", setup);

        settingsIntent.putExtra("settings_BTS1", "5");
        settingsIntent.putExtra("settings_BTS2", "3");

        if (setup.equals("both")) {
            singlemode = false;
            //safe measure


            settingsIntent.putExtra("device_left_mac", macIntent.getStringExtra("device_left_mac"));
            settingsIntent.putExtra("device_left_name", macIntent.getStringExtra("device_left_name"));
            settingsIntent.putExtra("device_right_mac", macIntent.getStringExtra("device_right_mac"));
            settingsIntent.putExtra("device_right_name", macIntent.getStringExtra("device_right_name"));
        } else {
            singlemode = true;
            settingsIntent.putExtra("device_mac", macIntent.getStringExtra("device_mac"));
            settingsIntent.putExtra("device_name", macIntent.getStringExtra("device_name"));
        }


        barBTS1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                BTS1 = progress; //getting bts1 left value
                settingsIntent.putExtra("settings_BTS1", String.valueOf(BTS1));

                if (progress == 0) progressChangedValue = 10;
                else
                    progressChangedValue = 25 * progress;
                textBTS1.setText(progressChangedValue + " Hz");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textBTS1.setText("Sample rate: " + progressChangedValue + " Hz");

            }


        });

        barBTS2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            double progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = 1024 / Math.pow(2, progress);
                BTS2 = progress;  //getting bts2 left value
                settingsIntent.putExtra("settings_BTS2", String.valueOf(9 - BTS2));
                textBTS2.setText((int) progressChangedValue + " kOhm");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                textBTS2.setText("Measurement range: " + (int) progressChangedValue + " kOhm");
            }


        });


        btnStartNextActivity.setOnClickListener(v -> {
            saveData();
            startActivity(settingsIntent);
            //TODO
            //if single -> visual
            //if multiple -> visual_two
        });


    }
    
    public void saveData(){
//        SharedPreferences sharedPreferences = getSharedPreferences(Shared_preferences, MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString(intent_name + "\t" + intent_value)
    }

}