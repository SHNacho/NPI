// Ignacio Sánchez Herrera
// Jacobo Casado de Gracia
// Luis Fernández García
package com.example.npi;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import in.championswimmer.sfg.lib.SimpleFingerGestures;

public class AsistenciaFragment extends Fragment {

    public AsistenciaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_asistencia, container, false);

        MainActivity.sfg.setDebug(true);
        MainActivity.sfg.setConsumeTouchEvents(true);

        MainActivity.button = (Button) view.findViewById(R.id.botonNFC);
        MainActivity.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), NFCActivity.class);
                startActivity(intent);
            }
        });

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

                return false;
            }

            @Override
            public boolean onSwipeRight(int fingers, long gestureDuration, double gestureDistance) {
                if (fingers == 1){
                    getParentFragmentManager().beginTransaction().replace(R.id.container, MainActivity.horarioFragment).commit();
                    MainActivity.bottomNavigationView.setSelectedItemId(R.id.horario);
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
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity.next_fragment_left = "horario";
        MainActivity.next_fragment_right = "home";
    }
}