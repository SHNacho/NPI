package com.example.npi;

import static android.content.Context.SENSOR_SERVICE;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import in.championswimmer.sfg.lib.SimpleFingerGestures;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HorarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HorarioFragment extends Fragment implements SensorEventListener {

    private View view;
    private TextView tv;


    public HorarioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_horario, container, false);
        // Obtenemos el TextView del horario
        tv = view.findViewById(R.id.horarioFragment);

        Sensor pressure = MainActivity.sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        MainActivity.sensorManager.registerListener((SensorEventListener) this, pressure, SensorManager.SENSOR_DELAY_NORMAL);

        MainActivity.sfg.setDebug(true);
        MainActivity.sfg.setConsumeTouchEvents(true);
        MainActivity.sfg.setOnFingerGestureListener(new SimpleFingerGestures.OnFingerGestureListener() {
            @Override
            public boolean onSwipeUp(int fingers, long gestureDuration, double gestureDistance) {
                return false;
            }

            @Override
            public boolean onSwipeDown(int fingers, long gestureDuration, double gestureDistance) {
                return false;
            }

            @Override
            public boolean onSwipeLeft(int fingers, long gestureDuration, double gestureDistance) {
                getParentFragmentManager().beginTransaction().replace(R.id.container, MainActivity.bibliotecaFragment).commit();
                MainActivity.bottomNavigationView.setSelectedItemId(R.id.biblioteca);
                return false;
            }

            @Override
            public boolean onSwipeRight(int fingers, long gestureDuration, double gestureDistance) {
                getParentFragmentManager().beginTransaction().replace(R.id.container, MainActivity.asistenciaFragment).commit();
                MainActivity.bottomNavigationView.setSelectedItemId(R.id.asistencia);
                return false;
            }

            @Override
            public boolean onPinch(int fingers, long gestureDuration, double gestureDistance) {
                return false;
            }

            @Override
            public boolean onUnpinch(int fingers, long gestureDuration, double gestureDistance) {
                return false;
            }

            @Override
            public boolean onDoubleTap(int fingers) {
                return false;
            }
        });

        view.setOnTouchListener(MainActivity.sfg);

        return view;
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float presure_value = 0.0f;
        float height = 0.0f;

        //tv.setText("Altura" + Float.toString(height));
        //Toast.makeText(this, "Hay sensor de presión: ", Toast.LENGTH_SHORT).show();
        if(Sensor.TYPE_PRESSURE == event.sensor.getType()){
            presure_value = event.values[0];
            height = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, presure_value);
            tv.setText("Altura: " + Float.toString(height));
        }
    }

}