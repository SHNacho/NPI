package com.example.npi;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.github.tlaabs.timetableview.TimetableView;

import java.util.ArrayList;

import in.championswimmer.sfg.lib.SimpleFingerGestures;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@l ink HorarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HorarioFragment extends Fragment implements SensorEventListener {

    private View view;
    private TextView tv;
    private TimetableView timetable;


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
        timetable = view.findViewById(R.id.timetable);

        timetable.add(crearAgenda());

        Sensor pressure = MainActivity.sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        MainActivity.sensorManager.registerListener((SensorEventListener) this, pressure, SensorManager.SENSOR_DELAY_NORMAL);

        MainActivity.sfg.setDebug(true);
        MainActivity.sfg.setConsumeTouchEvents(true);
        MainActivity.sfg.setOnFingerGestureListener(new SimpleFingerGestures.OnFingerGestureListener() {
            @Override
            public boolean onSwipeUp(int fingers, long gestureDuration, double gestureDistance) {
                if (fingers == 3){
                    Intent i = new Intent(getActivity().getApplicationContext(),contactosActivity.class);
                    startActivity(i);
                }
                return false;
            }

            @Override
            public boolean onSwipeDown(int fingers, long gestureDuration, double gestureDistance) {
                return false;
            }

            @Override
            public boolean onSwipeLeft(int fingers, long gestureDuration, double gestureDistance) {
                if (fingers == 1){
                    getParentFragmentManager().beginTransaction().replace(R.id.container, MainActivity.asistenciaFragment).commit();
                    MainActivity.bottomNavigationView.setSelectedItemId(R.id.asistencia);
                }
                return false;
            }

            @Override
            public boolean onSwipeRight(int fingers, long gestureDuration, double gestureDistance) {
                if (fingers == 1){
                    getParentFragmentManager().beginTransaction().replace(R.id.container, MainActivity.bibliotecaFragment).commit();
                    MainActivity.bottomNavigationView.setSelectedItemId(R.id.biblioteca);
                }
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

        final Button button = view.findViewById(R.id.scanner_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openScanner(view);
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                openScanner(view);
                return true;
            }
        });

        timetable.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {
            @Override
            public void OnStickerSelected(int idx, ArrayList<Schedule> schedules) {
               openScanner(view);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity.next_fragment_left = "biblioteca";
        MainActivity.next_fragment_right = "asistencia";
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
            //tv.setText("Altura: " + Float.toString(height));
        }
    }

    /** Called when the user taps the Send button */
    public void openScanner(View view) {
        Intent intent = new Intent(getActivity(), ScannerActivity.class);
        startActivity(intent);
    }

    public ArrayList<Schedule> crearAgenda(){
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        Schedule derecho = new Schedule();
        derecho.setClassTitle("Derecho Fiscal"); // sets subject
        derecho.setClassPlace("E-21"); // sets place
        derecho.setProfessorName("Clotilde Martin Pascual");// sets professor
        derecho.setDay(0);
        derecho.setStartTime(new Time(8,30)); // sets the beginning of class time (hour,minute)
        derecho.setEndTime(new Time(10,30)); // sets the end of class time (hour,minute)
        schedules.add(derecho);
        Schedule ddsi_teoria = new Schedule();
        ddsi_teoria.setClassTitle("DDSI - Teoría"); // sets subject
        ddsi_teoria.setClassPlace("0.3"); // sets place
        ddsi_teoria.setProfessorName("Ignacio Jose Blanco Medina");// sets professor
        ddsi_teoria.setDay(0);
        ddsi_teoria.setStartTime(new Time(11,30)); // sets the beginning of class time (hour,minute)
        ddsi_teoria.setEndTime(new Time(12,30)); // sets the end of class time (hour,minute)
        schedules.add(ddsi_teoria);
        Schedule ddsi_practicas = new Schedule();
        ddsi_practicas.setDay(1);
        ddsi_practicas.setClassTitle("DDSI - Prácticas (A1)");
        ddsi_practicas.setProfessorName("Maria José Martin Bautista");// sets professor
        ddsi_practicas.setStartTime(new Time(11,30)); // sets the beginning of class time (hour,minute)
        ddsi_practicas.setEndTime(new Time(14,30)); // sets the end of class time (hour,minute)
        ddsi_teoria.setClassPlace("2.4"); // sets place
        schedules.add(ddsi_practicas);
        Schedule derecho_2 = new Schedule();
        derecho_2.setClassTitle("Derecho Fiscal"); // sets subject
        derecho_2.setClassPlace("D-15"); // sets place
        derecho_2.setProfessorName("Clotilde Martín Pascual");// sets professor
        derecho_2.setDay(2);
        derecho_2.setStartTime(new Time(8,30)); // sets the beginning of class time (hour,minute)
        derecho_2.setEndTime(new Time(10,30)); // sets the end of class time (hour,minute)
        schedules.add(derecho_2);
        Schedule npi_practicas = new Schedule();
        npi_practicas.setClassTitle("Nuevos Paradigmas de la Interaccion - Prácticas"); // sets subject
        npi_practicas.setClassPlace("D-15"); // sets place
        npi_practicas.setProfessorName("Marcelino Jose Cabrera Cuevas");// sets professor
        npi_practicas.setDay(3);
        npi_practicas.setStartTime(new Time(8,30)); // sets the beginning of class time (hour,minute)
        npi_practicas.setEndTime(new Time(10,30)); // sets the end of class time (hour,minute)
        schedules.add(npi_practicas);
        Schedule npi_teoria = new Schedule();
        npi_teoria.setClassTitle("Nuevos Paradigmas de la Interaccion - Teoría"); // sets subject
        npi_teoria.setClassPlace("D-15"); // sets place
        npi_teoria.setProfessorName("Clotilde Martin Pascual");// sets professor
        npi_teoria.setDay(3);
        npi_teoria.setStartTime(new Time(10,30)); // sets the beginning of class time (hour,minute)
        npi_teoria.setEndTime(new Time(12,30)); // sets the end of class time (hour,minute)
        schedules.add(npi_teoria);
        Schedule procesadores_t = new Schedule();
        procesadores_t.setClassTitle("Procesadores de Lenguajes - Teoría"); // sets subject
        procesadores_t.setClassPlace("D-15"); // sets place
        procesadores_t.setProfessorName("Clotilde Martin Pascual");// sets professor
        procesadores_t.setDay(3);
        procesadores_t.setStartTime(new Time(12,30)); // sets the beginning of class time (hour,minute)
        procesadores_t.setEndTime(new Time(14,30)); // sets the end of class time (hour,minute)
        schedules.add(procesadores_t);
        Schedule vision_teoria = new Schedule();
        vision_teoria.setClassTitle("Vision por Computador - Teoría"); // sets subject
        vision_teoria.setClassPlace("1.7"); // sets place
        vision_teoria.setProfessorName("Nicolas Perez de la Blanca");// sets professor
        vision_teoria.setDay(4);
        vision_teoria.setStartTime(new Time(8,30)); // sets the beginning of class time (hour,minute)
        vision_teoria.setEndTime(new Time(10,30)); // sets the end of class time (hour,minute)
        schedules.add(vision_teoria);
        Schedule vision_practicas = new Schedule();
        vision_practicas.setClassTitle("Vision por Computador - Prácticas"); // sets subject
        vision_practicas.setClassPlace("2.8"); // sets place
        vision_practicas.setProfessorName("Nicolas Perez de la Blanca");// sets professor
        vision_practicas.setDay(4);
        vision_practicas.setStartTime(new Time(10,30)); // sets the beginning of class time (hour,minute)
        vision_practicas.setEndTime(new Time(12,30)); // sets the end of class time (hour,minute)
        schedules.add(vision_practicas);
        Schedule pl_practicas = new Schedule();
        pl_practicas.setClassTitle("Procesadores de Lenguajes - Prácticas"); // sets subject
        pl_practicas.setClassPlace("3.4"); // sets place
        pl_practicas.setProfessorName("Nicolas Pérez de la Blanca");// sets professor
        pl_practicas.setDay(4);
        pl_practicas.setStartTime(new Time(12,30)); // sets the beginning of class time (hour,minute)
        pl_practicas.setEndTime(new Time(14,30)); // sets the end of class time (hour,minute)
        schedules.add(pl_practicas);
        
        return schedules;

    }

}