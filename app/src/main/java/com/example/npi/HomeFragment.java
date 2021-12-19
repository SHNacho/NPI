package com.example.npi;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.tlaabs.timetableview.Schedule;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;

import in.championswimmer.sfg.lib.SimpleFingerGestures;

public class HomeFragment extends Fragment {

    TextView text;
    TextView resumen;
    static EditText editText;
    static ImageView micButton;
    static Button ttsButton;
    static FloatingActionButton dialog;

    public HomeFragment(){
        // require a empty public constructor
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        text = view.findViewById(R.id.homeFragment);


        resumen = view.findViewById(R.id.resumenDia);
        editText = view.findViewById(R.id.text);
        micButton = view.findViewById(R.id.button);
        ttsButton = view.findViewById(R.id.tts);
        dialog = view.findViewById(R.id.dialogflowButton);

        resumen.setText(resumenDelDia());

        micButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    micButton.setImageResource(R.drawable.baseline_mic_black_24dp);
                    MainActivity.speechRecognizer.stopListening();
                    return true;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    micButton.setImageResource(R.drawable.baseline_mic_red_400_24dp);
                    editText.setHint("Escuchando...");
                    editText.getText().clear();
                    MainActivity.speechRecognizer.startListening(MainActivity.speechRecognizerIntent);
                    return true;
                }
                return true;
            }
        });

        ttsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text = resumenDelDia();
                if (!text.isEmpty())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        MainActivity.textToSpeechEngine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts1");
                    }
            }
        });

        dialog.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent i = new Intent(getContext(),BotWebActivity.class);
                startActivity(i);
                return true;
            }
        });


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
                    getParentFragmentManager().beginTransaction().replace(R.id.container, MainActivity.bibliotecaFragment).commit();
                    MainActivity.bottomNavigationView.setSelectedItemId(R.id.biblioteca);
                }
                return false;
            }

            @Override
            public boolean onSwipeRight(int fingers, long gestureDuration, double gestureDistance) {

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
    public void onStart() {
        super.onStart();
        MainActivity.next_fragment_left = "asistencia";
        MainActivity.next_fragment_right = "biblioteca";
    }

    public String resumenDelDia(){
        Calendar calendar = Calendar.getInstance();
        int dia = calendar.get(Calendar.DAY_OF_WEEK)-2;
        ArrayList<Schedule> clases = HorarioFragment.crearAgenda();
        ArrayList<Schedule> clases_del_dia = new ArrayList<Schedule>();
        String horario = "Hoy tienes clase de ";

        for (Schedule clase:clases){
            if (clase.getDay()==dia){
                clases_del_dia.add(clase);
            }
        }
        if(clases_del_dia.isEmpty()) {
            horario = "Hoy no tienes clase";
        }
        else {
            for (Schedule clase : clases_del_dia) {
                if (clase == clases_del_dia.get(clases_del_dia.size() - 1))
                    horario = horario + "y ";
                horario = horario + clase.getClassTitle() + " ";
            }
        }

        return horario;
    }

}