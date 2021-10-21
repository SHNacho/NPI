package com.example.npi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.location.GnssAntennaInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Formatter;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private TextView infoTextView;

    HomeFragment homeFragment = new HomeFragment();
    BibliotecaFragment bibliotecaFragment = new BibliotecaFragment();
    HorarioFragment horarioFragment = new HorarioFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        infoTextView = (TextView) findViewById(R.id.textView);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnItemSelectedListener(this);
        //bottomNavigationView.setSelectedItemId(R.id.home);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.home:
//                getSupportFragmentManager().beginTransaction().add(R.id.container, homeFragment).commit();
//                infoTextView.setText("Hola");
//                return true;
            case R.id.biblioteca:
                //infoTextView.setText("Hola");
                getSupportFragmentManager().beginTransaction().replace(R.id.container, bibliotecaFragment).commit();
                return true;

            case R.id.horario:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, horarioFragment).commit();
                return true;
        }
        return false;
    }
}
