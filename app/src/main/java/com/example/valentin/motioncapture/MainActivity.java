package com.example.valentin.motioncapture;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.valentin.motioncapture.R.id.spinnerL;

public class MainActivity extends AppCompatActivity implements SensorEventListener {


    TextView x;
    TextView y;
    TextView z;
    Spinner spinner1;
    Spinner spinner2;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    public boolean accelSopported;
    ToggleButton start;
    HashMap<String,Integer> map1,map2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Spinner de choix du capteur
        spinner1=(Spinner)findViewById(spinnerL);
        ArrayAdapter<CharSequence> adapterL=ArrayAdapter.createFromResource(this,R.array.sensors, android.R.layout.simple_spinner_item);
        adapterL.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapterL);

        //Spinner de choix dde la Fréquence
        spinner2=(Spinner)findViewById(R.id.spinnerR);
        ArrayAdapter<CharSequence> adapterR=ArrayAdapter.createFromResource(this,R.array.frequency, android.R.layout.simple_spinner_item);
        adapterR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapterR);


        //Détecter les sensors présents
        mSensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors=mSensorManager.getSensorList(Sensor.TYPE_ALL);
        List<CharSequence> str=new ArrayList<CharSequence>();
        for(Sensor sensor : deviceSensors){
            Log.d("sensor",sensor.getName());
            str.add(sensor.getName());
        }

            // ArrayAdapter<CharSequence> adapter=new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, str);
        //spinner1.setAdapter(adapter);


        x=(TextView)findViewById(R.id.up);
        y=(TextView)findViewById(R.id.middle);
        z=(TextView)findViewById(R.id.down);
        start=(ToggleButton)findViewById(R.id.button);

        map1=new HashMap<String,Integer>();
        map1.put("Accelerometer",Sensor.TYPE_ACCELEROMETER);
        map1.put("Magnetometer",Sensor.TYPE_MAGNETIC_FIELD);
        map1.put("Gyroscope",Sensor.TYPE_GYROSCOPE);

        map2=new HashMap<String,Integer>();
        map2.put("FASTEST",SensorManager.SENSOR_DELAY_FASTEST);
        map2.put("GAME",SensorManager.SENSOR_DELAY_GAME);
        map2.put("NORMAL",SensorManager.SENSOR_DELAY_NORMAL);
        map2.put("UI",SensorManager.SENSOR_DELAY_UI);

        mSensorManager=(SensorManager) getSystemService(Service.SENSOR_SERVICE);
        mAccelerometer=mSensorManager.getDefaultSensor(map1.get(spinner1.getSelectedItem()));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()){
            case Sensor.TYPE_ACCELEROMETER:
                x.setText("x= "+Float.toString(event.values[0])+"m/s²");
                y.setText("y= "+Float.toString(event.values[1])+"m/s²");
                z.setText("z ="+Float.toString(event.values[2])+"m/s²");
                break;
            case Sensor.TYPE_GYROSCOPE:
                x.setText("x= "+Float.toString(event.values[0])+"rad/s");
                y.setText("y= "+Float.toString(event.values[1])+"rad/s");
                z.setText("z= "+Float.toString(event.values[2])+"rad/s");
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                x.setText("x= "+Float.toString(event.values[0])+"uT");
                y.setText("y= "+Float.toString(event.values[1])+"uT");
                z.setText("z= "+Float.toString(event.values[2])+"uT");
                break;
            default:
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onResume(){
        super.onResume();
        if(start.isChecked()){
            Log.d("capteur",""+map1.get(spinner1.getSelectedItem()));
            mAccelerometer=mSensorManager.getDefaultSensor(map1.get(spinner1.getSelectedItem()));
            accelSopported=mSensorManager.registerListener(this,mAccelerometer,map2.get(spinner2.getSelectedItem()));
        } else{
        mSensorManager.unregisterListener(this, mAccelerometer);
    }
    }

    @Override
    public void onPause(){
        if(accelSopported){
            mSensorManager.unregisterListener(this, mAccelerometer);
        }
        super.onPause();
    }

    public void click(View view) {
        if(start.isChecked()){

            Log.d("capteur",""+map1.get(spinner1.getSelectedItem()));
            mAccelerometer=mSensorManager.getDefaultSensor(map1.get(spinner1.getSelectedItem()));
            accelSopported=mSensorManager.registerListener(this,mAccelerometer,map2.get(spinner2.getSelectedItem()));
        }else{
            mSensorManager.unregisterListener(this, mAccelerometer);
        }
    }



}
