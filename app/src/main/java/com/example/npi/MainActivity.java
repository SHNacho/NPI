package com.example.npi;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.Math;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.squareup.seismic.ShakeDetector;

import in.championswimmer.sfg.lib.SimpleFingerGestures;



public class MainActivity extends AppCompatActivity implements SensorEventListener, NavigationBarView.OnItemSelectedListener, ShakeDetector.Listener {

    private static final int MY_PERMISSION_REQUEST_CAMERA = 0;

    // Variable para la barra de navegación inferior
    static BottomNavigationView bottomNavigationView;
    // Clase controladora de gestos
    static SimpleFingerGestures sfg = new SimpleFingerGestures();
    // Fragmentos
    static HomeFragment homeFragment = new HomeFragment();
    static BibliotecaFragment bibliotecaFragment = new BibliotecaFragment();
    static HorarioFragment horarioFragment = new HorarioFragment();
    static AsistenciaFragment asistenciaFragment = new AsistenciaFragment();
    // Controlador de los sensores
    static SensorManager sensorManager;

    static Button button;

    // Para la detección de rotaciones
    static String next_fragment;
    // Para el sensor de agitación
    static ShakeDetector sd;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        Sensor rotacion = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener((SensorEventListener) this, rotacion, SensorManager.SENSOR_DELAY_NORMAL);

        sd = new ShakeDetector(this);
        sd.start(sensorManager);
        next_fragment = "asistencia";
    }

    @Override
    public void onStart() {
        super.onStart();
        sd.start(sensorManager);
    }

    // Acción al agitar
    @Override public void hearShake() {
        Intent i = new Intent(getApplicationContext(),notesActivity.class);
        startActivity(i);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        float EPSILON = 1.0F;
        if(Sensor.TYPE_GYROSCOPE == event.sensor.getType()){
            float ejeY = event.values[1];
            // Si la velociadad de rotación en el eje Y es mayor de 5 radianes por segundo
            // en el sentido de las agujas del reloj
            if(ejeY < -5) {
                switch (next_fragment){
                    case "asistencia":
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, asistenciaFragment).commit();
                        bottomNavigationView.setSelectedItemId(R.id.asistencia);
                        break;
                    case "horario":
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, horarioFragment).commit();
                        bottomNavigationView.setSelectedItemId(R.id.horario);
                        break;
                    case "biblioteca":
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, bibliotecaFragment).commit();
                        bottomNavigationView.setSelectedItemId(R.id.biblioteca);
                        break;
                    case "home":
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        bottomNavigationView.setSelectedItemId(R.id.home);
                        break;
                }
            }
        }
    }



    // Acción al seleccionar un item de la barra inferior de navegación
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                return true;
            case R.id.biblioteca:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, bibliotecaFragment).commit();
                return true;
            case R.id.horario:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, horarioFragment).commit();
                return true;
            case R.id.asistencia:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, asistenciaFragment).commit();
                return true;
        }
        return false;
    }




}
