package com.example.npi;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.squareup.seismic.ShakeDetector;

import in.championswimmer.sfg.lib.SimpleFingerGestures;



public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener, ShakeDetector.Listener {

    static BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start(sensorManager);

    }

    @Override public void hearShake() {
        Toast.makeText(this, "Don't shake me, bro!", Toast.LENGTH_SHORT).show();
    }

    HomeFragment homeFragment = new HomeFragment();
    BibliotecaFragment bibliotecaFragment = new BibliotecaFragment();
    HorarioFragment horarioFragment = new HorarioFragment();
    AsistenciaFragment asistenciaFragment = new AsistenciaFragment();

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
