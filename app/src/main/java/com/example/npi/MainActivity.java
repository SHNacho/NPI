package com.example.npi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.squareup.seismic.ShakeDetector;

import java.util.ArrayList;
import java.util.Locale;

import in.championswimmer.sfg.lib.SimpleFingerGestures;



public class MainActivity extends AppCompatActivity implements SensorEventListener, NavigationBarView.OnItemSelectedListener, ShakeDetector.Listener {

    private static final int MY_PERMISSION_REQUEST_CAMERA = 0;
    public static final Integer RecordAudioRequestCode = 1;
    static SpeechRecognizer speechRecognizer;
    static TextToSpeech textToSpeechEngine;
    static Intent speechRecognizerIntent;

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
    Sensor rotacion;
    // Para el sensor de agitación
    static ShakeDetector sd;

    static Button button;

    // Para la detección de rotaciones
    static String next_fragment_left,
                  next_fragment_right;

    // Para el delay de los sensores
    Handler handler;
    int interval= 1000; // read sensor data each 1000 ms
    boolean flag = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkPermission();
        }

        textToSpeechEngine = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.SUCCESS) {
                    Log.e("TTS", "Inicio de la síntesis fallido");
                }
            }
        });

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {
                System.out.println("Error"+i);
                HomeFragment.editText.setText("Error: "+i);
            }

            @Override
            public void onResults(Bundle bundle) {
                HomeFragment.micButton.setImageResource(R.drawable.baseline_mic_black_24dp);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                HomeFragment.editText.setText(data.get(0));
                System.out.println(data.get(0));
                if(data.get(0).equals("qué horario tengo hoy")){
                    getSupportFragmentManager().beginTransaction().replace(
                            R.id.container,
                            horarioFragment
                    ).commit();
                    bottomNavigationView.setSelectedItemId(R.id.horario);
                }
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });



        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);



        // Inicializamos el controlador de mensajes
        handler = new Handler();
        // Creamos el sensor de rotación y ejecutamos el Listener
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        rotacion = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener((SensorEventListener) this, rotacion,
                                        SensorManager.SENSOR_DELAY_NORMAL);

        sd = new ShakeDetector(this);
        sd.start(sensorManager);
        next_fragment_left = "asistencia";
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, rotacion,
                SensorManager.SENSOR_DELAY_NORMAL);
        sd.start(sensorManager);
        handler.post(processSensors);
    }

    @Override
    public void onPause() {
        handler.removeCallbacks(processSensors);
        sd.stop();
        super.onPause();
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
            if(flag == true) {
                if (ejeY < -5) {
                    flag = false;
                    // Esto hace que el objeto runnable se ejecute cuando pase un tiempo igual a
                    // 'interval'
                    handler.postDelayed(processSensors, interval);
                    switch (next_fragment_left) {
                        case "asistencia":
                            getSupportFragmentManager().beginTransaction().replace(
                                    R.id.container,
                                    asistenciaFragment
                            ).commit();
                            bottomNavigationView.setSelectedItemId(R.id.asistencia);
                            break;
                        case "horario":
                            getSupportFragmentManager().beginTransaction().replace(
                                    R.id.container,
                                    horarioFragment
                            ).commit();
                            bottomNavigationView.setSelectedItemId(R.id.horario);
                            break;
                        case "biblioteca":
                            getSupportFragmentManager().beginTransaction().replace(
                                    R.id.container,
                                    bibliotecaFragment
                            ).commit();
                            bottomNavigationView.setSelectedItemId(R.id.biblioteca);
                            break;
                        case "home":
                            getSupportFragmentManager().beginTransaction().replace(
                                    R.id.container,
                                    homeFragment
                            ).commit();
                            bottomNavigationView.setSelectedItemId(R.id.home);
                            break;
                    }
                }
                if (ejeY > 5) {
                    flag = false;
                    handler.postDelayed(processSensors, interval);
                    switch (next_fragment_right) {
                        case "asistencia":
                            getSupportFragmentManager().beginTransaction().replace(
                                    R.id.container,
                                    asistenciaFragment
                            ).commit();
                            bottomNavigationView.setSelectedItemId(R.id.asistencia);
                            break;
                        case "horario":
                            getSupportFragmentManager().beginTransaction().replace(
                                    R.id.container,
                                    horarioFragment
                            ).commit();
                            bottomNavigationView.setSelectedItemId(R.id.horario);
                            break;
                        case "biblioteca":
                            getSupportFragmentManager().beginTransaction().replace(
                                    R.id.container,
                                    bibliotecaFragment
                            ).commit();
                            bottomNavigationView.setSelectedItemId(R.id.biblioteca);
                            break;
                        case "home":
                            getSupportFragmentManager().beginTransaction().replace(
                                    R.id.container,
                                    homeFragment
                            ).commit();
                            bottomNavigationView.setSelectedItemId(R.id.home);
                            break;
                    }
                }
            }
        }
    }

    // Acción al seleccionar un item de la barra inferior de navegación
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(
                        R.id.container,
                        homeFragment
                ).commit();
                return true;
            case R.id.biblioteca:
                getSupportFragmentManager().beginTransaction().replace(
                        R.id.container,
                        bibliotecaFragment
                ).commit();
                return true;
            case R.id.horario:
                getSupportFragmentManager().beginTransaction().replace(
                        R.id.container,
                        horarioFragment
                ).commit();
                return true;
            case R.id.asistencia:
                getSupportFragmentManager().beginTransaction().replace(
                        R.id.container,
                        asistenciaFragment
                ).commit();
                return true;
        }
        return false;
    }

    // variable runnable que contiene un método que se ejcuta cada vez que se llama a
    // handler.post
    private final Runnable processSensors = new Runnable() {
        @Override
        public void run() {
            flag = true;
        }
    };





}
