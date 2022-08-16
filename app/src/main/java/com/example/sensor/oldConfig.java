package com.example.sensor;
import androidx.appcompat.app.AppCompatActivity;

//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothSocket;
//import android.content.Intent;
//import android.content.pm.ActivityInfo;
//import android.graphics.Color;
//import android.os.AsyncTask;
//import android.os.Handler;
//import android.os.SystemClock;

//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//import android.widget.ToggleButton;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
////
public class oldConfig extends AppCompatActivity {}
//
//    public void rightClicked(View view) {
//        // Is the toggle on?
//        boolean on = ((ToggleButton) view).isChecked();
//
//        if (on) {
//            // Enable vibrate
//        } else {
//            // Disable vibrate
//        }
//    }
//
//
//
//    private String mac_left;
//    private String mac_right;
//
//    public BluetoothAdapter myBluetoothAdapter;
//
//    private BluetoothDevice device_left;
//    private BluetoothSocket socket_left;
//    private ConnectedThread thread_left;
//    public static final int thread_left_idx = 1;
//
//    private BluetoothDevice device_right;
//    private BluetoothSocket socket_right;
//    private ConnectedThread thread_right;
//    public static final int thread_right_idx = 2;
//
//    volatile boolean startWorker = false;
//
//    private ToggleButton btn_connect_left;
//    private ToggleButton btn_connect_right;
//
//    private RadioGroup radio_sampling_rate;
//    public String sampling_rate = null;
//    private int sampling_rate_idx;
//    private RadioGroup radio_measurement_range_1;
//    private RadioGroup radio_measurement_range_2;
//    public String measurement_range = null;
//    volatile public static int measurement_range_idx;
//    private boolean isSelectedRange = false;
//
//    public static final String reset_timestamp = "BT^TRES\r";
//    public static final String stop_stream = "BT^STOP\r";
//    public static final String set_speed10Hz = "BTS1=0\r"; // 10Hz
//    public static final String set_speed50Hz = "BTS1=2\r"; // 50Hz
//    public static final String set_speed100Hz = "BTS1=4\r"; // 100Hz
//    public static final String set_speed150Hz = "BTS1=6\r"; // 150Hz
//    public static final String set_speed200Hz = "BTS1=8\r"; // 200Hz
//    public static final String[] speed_vect = new String[] {"10Hz","25Hz","50Hz","75Hz","100Hz","125Hz","150Hz","175Hz","200Hz"};
//    public static final String set_range_2k = "BTS2=9\r";
//    public static final String set_range_4k = "BTS2=8\r";
//    public static final String set_range_8k = "BTS2=7\r";
//    public static final String set_range_16k = "BTS2=6\r";
//    public static final String set_range_32k = "BTS2=5\r";
//    public static final String set_range_64k = "BTS2=4\r";
//    public static final String set_range_128k = "BTS2=3\r";
//    public static final String set_range_256k = "BTS2=2\r";
//    public static final String set_range_512k = "BTS2=1\r";
//    public static final String set_range_1024k = "BTS2=0\r";
//    public final String[] range_vect = new String[] {"1024k","512k","256k","128k","64k","32k","16k","8k","4k","2k"};
//    public static final double[] resistance_multiplier = new double[] {16.00,8.00,4.00,2.00,1.00,0.50,0.25,0.125,0.063,0.03125};
//
//    Handler bluetoothIn;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_config);
//
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // always portrait so we don't have to care about rotation destroying our app
//
//        // get both bluetooth device mac addresses
//        Intent myIntent = getIntent();
//        mac_left = myIntent.getStringExtra("device_left");
//        mac_right = myIntent.getStringExtra("device_right");
//
//        // get the bluetooth adapter
//        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//        measurement_range = set_range_1024k;
//        measurement_range_idx = 9;
//
//        sampling_rate = set_speed200Hz;
//        sampling_rate_idx = 4;
//
//        radio_sampling_rate = findViewById(R.id.radio_sampling_rate);
//        radio_sampling_rate.clearCheck();
//        radio_sampling_rate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                RadioButton rb = group.findViewById(checkedId);
//                if (checkedId == R.id.radio_10hz){
//                    sampling_rate = set_speed10Hz;
//                    sampling_rate_idx = 0;
//                } else if(checkedId == R.id.radio_50hz) {
//                    sampling_rate = set_speed50Hz;
//                    sampling_rate_idx = 2;
//                } else if(checkedId == R.id.radio_100hz) {
//                    sampling_rate = set_speed100Hz;
//                    sampling_rate_idx = 4;
//                } else if(checkedId == R.id.radio_150hz) {
//                    sampling_rate = set_speed150Hz;
//                    sampling_rate_idx = 6;
//                } else if(checkedId == R.id.radio_200hz) {
//                    sampling_rate = set_speed200Hz;
//                    sampling_rate_idx = 8;
//                }
//            }
//        });
//
//        radio_measurement_range_1 = findViewById(R.id.radio_range1);
//        radio_measurement_range_1.clearCheck();
//        radio_measurement_range_1.setOnCheckedChangeListener(listener1);
//
//        radio_measurement_range_2 = findViewById(R.id.radio_range2);
//        radio_measurement_range_2.clearCheck();
//        radio_measurement_range_2.setOnCheckedChangeListener(listener2);
//
//        final TextView txt_settings_left = findViewById(R.id.txt_settings_left);
//        final TextView txt_settings_right = findViewById(R.id.txt_settings_right);
//        Button btn_get_settings = findViewById(R.id.btn_get_config);
//        btn_get_settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                txt_settings_left.setText("Left: \n");
//                if(thread_left != null){
//                    thread_left.getSettings();
//                }
//
//                txt_settings_right.setText("Right: \n");
//                if(thread_right != null){
//                    thread_right.getSettings();
//                }
//            }
//        });
//
//        Button btn_send_settings = findViewById(R.id.btn_set_config);
//        btn_send_settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // first check if we have assigned speed and range
//                if(sampling_rate == null) {
//                    sampling_rate = set_speed100Hz; // default speed
//                }
//                if(measurement_range == null) {
//                    measurement_range = set_range_64k; // default range
//                }
//                // try to send to the measurement unit
//                try {
//                    // stop the measurement
//                    thread_left.write(stop_stream);
//                    startWorker = false;
//                    SystemClock.sleep(20);
//                    // set the range
//                    thread_left.write(measurement_range);
//                    SystemClock.sleep(20);
//                    // set the speed
//                    thread_left.write(sampling_rate);
//                    SystemClock.sleep(20);
//                    // reset the timer
//                    thread_left.write(reset_timestamp);
//                } catch (Exception e){
//                    // couldn't send config, probably because no working thread, put here check
//                    Toast.makeText(getApplicationContext(), "Couldn't send configuration, check connection" ,Toast.LENGTH_LONG).show();
//                }
//
//                try {
//                    // stop the measurement
//                    thread_right.write(stop_stream);
//                    startWorker = false;
//                    SystemClock.sleep(20);
//                    Toast.makeText(getApplicationContext(), "Measurement stopped, configuration updated" ,Toast.LENGTH_LONG).show();
//                    // set the range
//                    thread_right.write(measurement_range);
//                    SystemClock.sleep(20);
//                    // set the speed
//                    thread_right.write(sampling_rate);
//                    SystemClock.sleep(20);
//                    // reset the timer
//                    thread_right.write(reset_timestamp);
//                } catch (Exception e){
//                    // couldn't send config, probably because no working thread, put here check
//                    Toast.makeText(getApplicationContext(), "Couldn't send configuration, check connection" ,Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//        btn_connect_left = findViewById(R.id.btn_connect_left);
//        btn_connect_left.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    try {
//                        // create the device and sock for the left device connection
//                        device_left = myBluetoothAdapter.getRemoteDevice(mac_left); // use the saved mac address
//                        Method m = device_left.getClass().getMethod("createInsecureRfcommSocket", new Class[] { int.class }); // connect using insecure connection
//                        socket_left = (BluetoothSocket)m.invoke(device_left, 1);
//                        socket_left.connect();
//                        thread_left = new ConnectedThread(socket_left,thread_left_idx); // create a new thread for the connection using our custom thread class
//                    } catch (Exception e) {
//                        Toast.makeText(getApplicationContext(), "Left Connection Failed" ,Toast.LENGTH_LONG).show();
//                        btn_connect_left.setChecked(false);
//                    }
//                } else {
//                    try {
//                        // close any existing connections
//                        thread_left.cancel();
//
//                    } catch (Exception e) {
//                        Toast.makeText(getApplicationContext(), "Oooops! Failed to disconnect" ,Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//        });
//
//        btn_connect_right = findViewById(R.id.btn_connect_right);
//        btn_connect_right.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    try {
//                        // create the device and sock for the left device connection
//                        device_right = myBluetoothAdapter.getRemoteDevice(mac_right); // use the saved mac address
//                        Method m = device_right.getClass().getMethod("createInsecureRfcommSocket", new Class[] { int.class }); // connect using insecure connection
//                        socket_right = (BluetoothSocket)m.invoke(device_right, 1);
//                        socket_right.connect();
//                        thread_right = new ConnectedThread(socket_right,thread_right_idx); // create a new thread for the connection using our custom thread class
//                    } catch (Exception e) {
//                        Toast.makeText(getApplicationContext(), "Right Connection Failed" ,Toast.LENGTH_LONG).show();
//                        btn_connect_right.setChecked(false);
//                    }
//                } else {
//                    try {
//                        // close any existing connection
//                        thread_right.cancel();
//
//                    } catch (Exception e) {
//                        Toast.makeText(getApplicationContext(), "Oooops! Failed to disconnect" ,Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//        });
//
//        Button btn_graph = findViewById(R.id.btn_graph);
//        btn_graph.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(device_left == null || device_right == null) { // for now allow only 2 device reading
//                    Toast.makeText(getApplicationContext(), "Connect devices and try again" ,Toast.LENGTH_LONG).show();
//                } else if(!isSelectedRange){
//                    Toast.makeText(getApplicationContext(), "Select the measurement range" ,Toast.LENGTH_LONG).show();
//                } else {
//                    Intent showGraph = new Intent(getApplicationContext(),ShowGraph.class);
//                    showGraph.putExtra("device_left",device_left);
//                    showGraph.putExtra("device_right",device_right);
//                    showGraph.putExtra("range_idx",resistance_multiplier[measurement_range_idx]);
//                    startActivity(showGraph);
//                }
//            }
//        });
//
//        Button btn_save = findViewById(R.id.btn_save);
//        btn_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(device_left == null || device_right == null) { // for now allow only 2 device reading
//                    Toast.makeText(getApplicationContext(), "Connect devices and try again" ,Toast.LENGTH_LONG).show();
//                } else if(!isSelectedRange){
//                    Toast.makeText(getApplicationContext(), "Select the measurement range" ,Toast.LENGTH_LONG).show();
//                } else {
//                    Intent saveData = new Intent(getApplicationContext(),SavingData.class);
//                    saveData.putExtra("device_left",device_left);
//                    saveData.putExtra("device_right",device_right);
//                    saveData.putExtra("range_idx",resistance_multiplier[measurement_range_idx]);
//                    startActivity(saveData);
//                }
//            }
//        });
//
//        bluetoothIn = new Handler() {
//            public void handleMessage(android.os.Message msg) {
//                if (msg.what != 0) {
//
//                    String recMessage = new String((byte[]) msg.obj);
//                    if(msg.what == thread_left_idx){
//                        txt_settings_left.append(recMessage);
//                        String reg_val = txt_settings_left.getText().toString();
//                        if(reg_val.contains("Register 1 value:") && reg_val.contains("Register 2 value:")){
//                            int pos1 = reg_val.indexOf("Register 1 value:") + 18;
//                            int pos2 = reg_val.indexOf("Register 2 value:") + 18;
//                            int spd_idx = new Integer(reg_val.substring(pos1,pos1+1));
//                            int rng_idx = new Integer(reg_val.substring(pos2,pos2+1));
//                            txt_settings_left.setText("Left: \n");
//                            txt_settings_left.append(speed_vect[spd_idx]);
//                            txt_settings_left.append("\n");
//                            txt_settings_left.append(range_vect[rng_idx]);
//                        }
//                    } else {
//                        txt_settings_right.append(recMessage);
//                        String reg_val = txt_settings_right.getText().toString();
//                        if(reg_val.contains("Register 1 value:") && reg_val.contains("Register 2 value:")){
//                            int pos1 = reg_val.indexOf("Register 1 value:") + 18;
//                            int pos2 = reg_val.indexOf("Register 2 value:") + 18;
//                            int spd_idx = new Integer(reg_val.substring(pos1,pos1+1));
//                            int rng_idx = new Integer(reg_val.substring(pos2,pos2+1));
//                            txt_settings_right.setText("Right: \n");
//                            txt_settings_right.append(speed_vect[spd_idx]);
//                            txt_settings_right.append("\n");
//                            txt_settings_right.append(range_vect[rng_idx]);
//                        }
//                    }
//                }
//            }
//        };
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    protected void onPostResume() {
//        super.onPostResume();
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        // close all connections
//        try{
//            thread_left.write(stop_stream);
//            thread_left.cancel();
//            thread_right.write(stop_stream);
//            thread_right.cancel();
//        } catch (Exception e) {
//            // something went wrong
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//
//        try{
//            thread_left.write(stop_stream);
//            thread_right.write(stop_stream);
//        } catch (Exception e) {
//            // something went wrong
//        }
//
//    }
//
//    private RadioGroup.OnCheckedChangeListener listener1 = new RadioGroup.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(RadioGroup group, int checkedId) {
//            if (checkedId != -1){
//                radio_measurement_range_2.setOnCheckedChangeListener(null); // turn it off so we don't get endless loop
//                radio_measurement_range_2.clearCheck(); // clear selection, if any
//                radio_measurement_range_2.setOnCheckedChangeListener(listener2); // turn the listener back on
//                if (checkedId == R.id.radio_2k){
//                    measurement_range = set_range_2k;
//                    measurement_range_idx = 9;
//                } else if(checkedId == R.id.radio_4k) {
//                    measurement_range = set_range_4k;
//                    measurement_range_idx = 8;
//                } else if(checkedId == R.id.radio_8k) {
//                    measurement_range = set_range_8k;
//                    measurement_range_idx = 7;
//                } else if(checkedId == R.id.radio_16k) {
//                    measurement_range = set_range_16k;
//                    measurement_range_idx = 6;
//                } else if(checkedId == R.id.radio_32k) {
//                    measurement_range = set_range_32k;
//                    measurement_range_idx = 5;
//                }
//                isSelectedRange = true;
//            }
//        }
//    };
//
//    private RadioGroup.OnCheckedChangeListener listener2 = new RadioGroup.OnCheckedChangeListener() {
//        @Override
//        public void onCheckedChanged(RadioGroup group, int checkedId) {
//            if (checkedId != -1){
//                radio_measurement_range_1.setOnCheckedChangeListener(null);
//                radio_measurement_range_1.clearCheck();
//                radio_measurement_range_1.setOnCheckedChangeListener(listener1);
//                if (checkedId == R.id.radio_64k){
//                    measurement_range = set_range_64k;
//                    measurement_range_idx = 4;
//                } else if(checkedId == R.id.radio_128k) {
//                    measurement_range = set_range_128k;
//                    measurement_range_idx = 3;
//                } else if(checkedId == R.id.radio_256k) {
//                    measurement_range = set_range_256k;
//                    measurement_range_idx = 2;
//                } else if(checkedId == R.id.radio_512k) {
//                    measurement_range = set_range_512k;
//                    measurement_range_idx = 1;
//                } else if(checkedId == R.id.radio_1024k) {
//                    measurement_range = set_range_1024k;
//                    measurement_range_idx = 0;
//                }
//                isSelectedRange = true;
//            }
//        }
//    };
//
//
//    public class ConnectedThread extends Thread {
//        private final BluetoothSocket mmSocket;
//        private final InputStream mmInStream;
//        private final OutputStream mmOutStream;
//        private final int threadIdx;
//
//        public ConnectedThread(BluetoothSocket socket, int idx) {
//            mmSocket = socket;
//            threadIdx = idx;
//            InputStream tmpIn = null;
//            OutputStream tmpOut = null;
//
//            // Get the input and output streams, using temp objects because
//            // member streams are final
//            try {
//                tmpIn = socket.getInputStream();
//                tmpOut = socket.getOutputStream();
//            } catch (IOException e) { }
//            mmInStream = tmpIn;
//            mmOutStream = tmpOut;
//        }
//
//        public void startThread() {
//            start();
//        }
//
//        public void run() {
//
//            int bytesAvailable; // bytes available in the buffer
//            int bytesRead; // bytes actually returned from read()
//
//            final byte stop_byte = (byte)0x55;
//            final byte start_byte = (byte)0xF0;
//            int pos = 0;
//            int packet_nr = 0;
//            int max_pckt_cnt = 20; // packets to collect before sending over
//            byte[] buf = new byte[1024]; // actually not needed more than 47
//            byte[] pckts = new byte[47*max_pckt_cnt];
//
//            // Keep looping to listen for received messages
//            startWorker = true;
//            while (startWorker) {
//                try {
//                    bytesAvailable = mmInStream.available();
//                    if(bytesAvailable > 0) // one data packet has minimum 47 bytes
//                    {
//                        byte[] b = new byte[bytesAvailable];
//                        bytesRead = mmInStream.read(b);
//                        for(int i=0;i < bytesRead;i++){
//                            // put the byte in the buffer
//                            buf[pos] = b[i];
//                            // check if the new byte was the end of a packet
//                            if(buf[pos] == stop_byte && pos > 46){
//                                if(buf[pos-46] == start_byte){
//                                    System.arraycopy(buf,pos-46,pckts,packet_nr*47,47);
//                                    packet_nr++;
//                                    pos = 0;
//                                }
//                                if(packet_nr > max_pckt_cnt - 1){
//                                    bluetoothIn.obtainMessage(threadIdx, 1, -1,pckts).sendToTarget();
//                                    packet_nr = 0;
//                                }
//                            }
//                            // increment the counter and add limit for overflow safety
//                            if(pos < 1023){
//                                pos++;
//                            } else {
//                                pos = 0;
//                            }
//                        }
//                    }
//                } catch (IOException e) {
//                    startWorker = false;
//                    break;
//                }
//            }
//        }
//
//        /* Call this from the main activity to send data to the remote device */
//        public void write(String input) {
//            byte[] bytes = input.getBytes();           //converts entered String into bytes
//            try {
//                mmOutStream.write(bytes);
//            } catch (IOException e) { }
//        }
//
//        public void getSettings() {
//            int CR = 13;
//            int bytesAvailable;
//            String get_speed_reg_value = "BTS1?";
//            String get_range_reg_value = "BTS2?";
//
//            try
//            {
//                mmOutStream.write(get_speed_reg_value.getBytes());
//                mmOutStream.write(CR);
//            }
//            catch (Exception e) { }
//
//            for(int i = 0;i < 10;i++){
//                try {
//                    bytesAvailable = mmInStream.available();
//                    if(bytesAvailable > 0) // one data packet has minimum 47 bytes
//                    {
//                        byte[] b = new byte[bytesAvailable];
//                        mmInStream.read(b);
//                        bluetoothIn.obtainMessage(threadIdx, 2, -1,b).sendToTarget();
//                    }
//                } catch (IOException e) { }
//                SystemClock.sleep(50);
//            }
//
//            try
//            {
//                mmOutStream.write(get_range_reg_value.getBytes());
//                mmOutStream.write(CR);
//            }
//            catch (Exception e) { }
//
//            for(int i = 0;i < 10;i++){
//                try {
//                    bytesAvailable = mmInStream.available();
//                    if(bytesAvailable > 0) // one data packet has minimum 47 bytes
//                    {
//                        byte[] b = new byte[bytesAvailable];
//                        mmInStream.read(b);
//                        bluetoothIn.obtainMessage(threadIdx, 2, -1,b).sendToTarget();
//                    }
//                } catch (IOException e) { }
//                SystemClock.sleep(50);
//            }
//        }
//
//        /* Call this from the main activity to shutdown the connection */
//        public void cancel() {
//            try {
//                mmSocket.close();
//            } catch (IOException e) { }
//        }
//    }
//
//}
//
