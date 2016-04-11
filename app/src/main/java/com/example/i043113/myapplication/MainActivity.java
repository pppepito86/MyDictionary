package com.example.i043113.myapplication;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoWcdma;
import android.telephony.CellLocation;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager mSensorManager;

    WifiManager wifi;
    WifiScanReceiver wifiReciever;
    private Sensor mAccelerometer;
    private Sensor mGeomagnetic;
    TelephonyManager tManager;

    private static SensorManager mySensorManager;
    private boolean sersorrunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGeomagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

       // mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
       // mSensorManager.registerListener(this, mGeomagnetic, SensorManager.SENSOR_DELAY_FASTEST);


        mySensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> mySensors = mySensorManager.getSensorList(Sensor.TYPE_ORIENTATION);

        if(mySensors.size() > 0){
            mySensorManager.registerListener(mySensorEventListener, mySensors.get(0), SensorManager.SENSOR_DELAY_NORMAL);
            sersorrunning = true;
            Toast.makeText(this, "Start ORIENTATION Sensor", Toast.LENGTH_LONG).show();

        }
        else{
            Toast.makeText(this, "No ORIENTATION Sensor", Toast.LENGTH_LONG).show();
            sersorrunning = false;
            finish();
        }

        //String mac3 = "d8:c7:c8:88:70:b0";
        //String mac2 = "d8:c7:c8:8b:40:72";
        String mac1 = "d8:c7:c8:8b:43:52";
        String mac2 = "d8:c7:c8:88:5c:30";

        bobiMap.put(mac1, new AP(mac1, 0, 0));
        bobiMap.put(mac2, new AP(mac2, 70, 0));
        //bobiMap.put(mac3, new AP(mac3, 70, 0));

        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        wifi=(WifiManager)getSystemService(Context.WIFI_SERVICE);
        wifiReciever = new WifiScanReceiver();
        wifi.startScan();

        final TextView view = (TextView) findViewById(R.id.aaa);

        /*
        new Thread() {
            @Override
            public void run() {



                final TextView view = (TextView) findViewById(R.id.aaa);
                WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                final Map<String, Integer> pointMap = new HashMap<String, Integer>();
                for (int i = 0; i < 5; i++) {
                    wifiManager.startScan();
                    try {
                        Thread.sleep(6000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    List<ScanResult> apList = wifiManager.getScanResults();
                    Map<String, Integer> pointMap2 = new HashMap<String, Integer>();
                    for (ScanResult sr : apList) {
                        pointMap2.put(sr.BSSID, sr.level);
                    }
                    Map<String, Integer> pointMap3 = new HashMap<String, Integer>(pointMap);
                    for (String key : pointMap3.keySet()) {
                        if (pointMap2.containsKey(key)) {
                            pointMap.put(key, pointMap.get(key) + pointMap2.get(key));
                            pointMap2.remove(key);
                        } else {
                            pointMap.put(key, pointMap.get(key) - 100);
                        }
                    }
                    for (Map.Entry<String, Integer> entry : pointMap2.entrySet()) {
                        pointMap.put(entry.getKey(), entry.getValue() - 100 * i);
                    }
                }
                for (String key : pointMap.keySet()) {
                    pointMap.put(key, pointMap.get(key) / 5);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.setText(pointMap.toString());
                    }
                });

                for (;;) {
                    wifiManager.startScan();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    List<ScanResult> apList = wifiManager.getScanResults();
                    int probability = 100;
                    HashMap<String, Integer> pointMap2 = new HashMap<String, Integer>(pointMap);
                    for (ScanResult sr : apList) {
                        pointMap2.put(sr.BSSID, sr.level);

                    }
                    int count = 0;
                    int d = 0;
                    for (Map.Entry<String, Integer> entry: pointMap.entrySet()) {
                        if (pointMap2.containsKey(entry.getKey())) {
                            int diff = Math.abs(entry.getValue() - pointMap2.get(entry.getKey()));
                            if (diff < 5) {
                                //probability -= diff;
                                d += diff;
                                count++;
                            }
                            pointMap2.remove(entry.getKey());
                        } else if (entry.getValue() >= -80){
                            probability -= 10;
                        }
                    }
                    for (Map.Entry<String, Integer> entry: pointMap2.entrySet()) {
                        if (entry.getValue() >= -80){
                            probability -= 10;
                        }
                    }
                    probability -= d/count;

                        final int p = probability;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.setText("" + p);
                        }
                    });

                }
            }


        };*/
    }

    protected void onResume() {
        super.onResume();


        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER), SensorManager.SENSOR_STATUS_ACCURACY_LOW);

        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);

    }

    public double calculateDistance(double levelInDb, double freqInMHz)    {
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(levelInDb)) / 20.0;
        return Math.pow(10.0, exp);
    }

    float Rot[]=null; //for gravity rotational data
    //don't use R because android uses that for other stuff
    float I[]=null; //for magnetic rotational data
    float accels[]=new float[3];
    float mags[]=new float[3];
    float[] values = new float[3];

    float azimuth;
    float pitch;
    float roll;

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        //below commented code - junk - unreliable is never populated
        //if sensor is unreliable, return void
        //if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
        //{
        //    return;
        //}
        final TextView view1 = (TextView) findViewById(R.id.aaa);


        switch (event.sensor.getType())
        {
            case Sensor.TYPE_MAGNETIC_FIELD:
                mags = event.values.clone();
                break;
            case Sensor.TYPE_ACCELEROMETER:
                accels = event.values.clone();
                break;
            case Sensor.TYPE_STEP_COUNTER:
                steps.add(angle);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view1.setText(view1.getText() + "\n" + angle);
                    }
                });
        }

        if (mags != null && accels != null) {
            Rot = new float[9];
            I= new float[9];
            SensorManager.getRotationMatrix(Rot, I, accels, mags);
            // Correct if screen is in Landscape

            float[] outR = new float[9];
            SensorManager.remapCoordinateSystem(Rot, SensorManager.AXIS_X, SensorManager.AXIS_Y, outR);
            SensorManager.getOrientation(outR, values);

            azimuth = values[0] * 57.2957795f; //looks like we don't need this one
            pitch =values[1] * 57.2957795f;
            roll = values[2] * 57.2957795f;
            mags = null; //retrigger the loop when things are repopulated
            accels = null; ////retrigger the loop when things are repopulated
            final TextView view = (TextView) findViewById(R.id.aaa);
            angle = azimuth;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //view.setText(String.valueOf(azimuth));
                }
            });
        }
    }

    List<Double> steps = new ArrayList<>();


    private SensorEventListener mySensorEventListener = new SensorEventListener(){

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            final String s = "" + event.values[0];
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final TextView view = (TextView) findViewById(R.id.aaa);
                  //  view.setText(s);
                }
            });
        }
    };

    @Override
    protected void onDestroy() {
// TODO Auto-generated method stub
        super.onDestroy();

        if(sersorrunning){
            mySensorManager.unregisterListener(mySensorEventListener);
        }
    }

    HashMap<String, AP> bobiMap = new HashMap<String, AP>();

    private class WifiScanReceiver extends BroadcastReceiver{
        public void onReceive(Context c, Intent intent) {
            List<ScanResult> wifiScanList = wifi.getScanResults();
            final TextView view = (TextView) findViewById(R.id.aaa);
            StringBuilder sb = new StringBuilder();
            for (ScanResult s: wifiScanList) {
                DecimalFormat df = new DecimalFormat("#.##");
                sb.append(s.BSSID + ", " + s.SSID + ", " + s.level + ", " + s.frequency + ", " + df.format(calculateDistance((double)s.level, s.frequency)) + "\n");
            }
            //view.setText(sb.toString());

            final List<ScanResult> threeBest = getThreeBest(filterRouters(wifiScanList));
            final Map<AP, Double> map = new HashMap<AP, Double>();
            for (ScanResult result: threeBest) {
                map.put(bobiMap.get(result.BSSID), calculateDistance(result.level, result.frequency));
            }
            Log.e("myapplication", "threebest: " + threeBest);
           // if (threeBest.size() < 3) return;
            AP[] ap = map.keySet().toArray(new AP[]{});
            //double[] position = Trilateration.MyTrilateration2(ap[0], map.get(ap[0]),
              //      ap[1], map.get(ap[2]),
                //    ap[2], map.get(ap[2]));
            //final String text = "Loc: " + Arrays.toString(position);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final TextView view = (TextView) findViewById(R.id.aaa);
                    //view.setText(map.toString() + "\n" + text + "\n");
                    double dist1 = 0, dist2 = 0;
                    if (threeBest.size() > 0) {
                        dist1 = calculateDistance(threeBest.get(0).level, threeBest.get(0).frequency);
                    }
                    if (threeBest.size() > 1) {
                        dist2 = calculateDistance(threeBest.get(1).level, threeBest.get(0).frequency);
                    }
                    //view.setText(dist1 + "\n" + dist2);
                }
            });

            wifi.startScan();
        }

        private List<ScanResult> filterRouters(List<ScanResult> scanResults) {
            ArrayList<ScanResult> filteredResults = new ArrayList<>(5);
            for (ScanResult s: scanResults) {
                if (bobiMap.containsKey(s.BSSID)) {
                    filteredResults.add(s);
                }
            }
            return filteredResults;
        }

        private List<ScanResult> getThreeBest(List<ScanResult> filteredResults) {
            Collections.sort(filteredResults, new Comparator<ScanResult>() {
                @Override
                public int compare(ScanResult r1, ScanResult r2) {
                    double res = calculateDistance(r1.level, r1.frequency) - calculateDistance(r2.level, r2.frequency);
                    if (res > 0.001) return 1;
                    if (res< -0.001) return -1;
                    return 0;
                }
            });
            ArrayList<ScanResult> bestResults = new ArrayList<>(5);
            for (int i = 0; i < 3; i++) {
                if (filteredResults.size() > i)
                    bestResults.add(filteredResults.get(i));
            }
            return bestResults;
        }
    }

    private static final int TEST_GRAV = Sensor.TYPE_ACCELEROMETER;
    private static final int TEST_MAG = Sensor.TYPE_MAGNETIC_FIELD;
    private final float alpha = (float) 0.8;
    private float gravity[] = new float[3];
    private float magnetic[] = new float[3];
    public void onSensorChanged1(SensorEvent event) {
       /* final TextView view = (TextView) findViewById(R.id.aaa);
        Sensor sensor = event.sensor;
        if (sensor.getType() == TEST_GRAV) {
            // Isolate the force of gravity with the low-pass filter.
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
        } else if (sensor.getType() == TEST_MAG) {

            magnetic[0] = event.values[0];
            magnetic[1] = event.values[1];
            magnetic[2] = event.values[2];

            float[] R = new float[9];
            float[] I = new float[9];
            SensorManager.getRotationMatrix(R, I, gravity, magnetic);
            float [] A_D = event.values.clone();
            float [] A_W = new float[3];
            A_W[0] = R[0] * A_D[0] + R[1] * A_D[1] + R[2] * A_D[2];
            A_W[1] = R[3] * A_D[0] + R[4] * A_D[1] + R[5] * A_D[2];
            A_W[2] = R[6] * A_D[0] + R[7] * A_D[1] + R[8] * A_D[2];
            final String s = "X :"+A_W[0]+"\nY :"+A_W[1]+"\nZ :"+A_W[2] + "\n";
            Log.e("myapplication", "\nX :" + A_W[0] + "\nY :" + A_W[1] + "\nZ :" + A_W[2]);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view.setText(Arrays.toString(magnetic));
                }
            });
        }
    }*/





        final TextView view = (TextView) findViewById(R.id.aaa);
        final float azimuth_angle = event.values[0];
        float pitch_angle = event.values[1];
        float roll_angle = event.values[2];
        final String r = azimuth_angle + "   " + pitch_angle + "   " + roll_angle;
        // Do something with these orientation angles.
        angle = azimuth_angle;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                       // view.setText(String.valueOf(azimuth_angle));
            }
        });
    }

    double angle = 0;

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
