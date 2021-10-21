package com.example.npi;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.championswimmer.sfg.lib.SimpleFingerGestures;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AsistenciaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AsistenciaFragment extends Fragment {

    public AsistenciaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_asistencia, container, false);

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
                getParentFragmentManager().beginTransaction().replace(R.id.container, MainActivity.horarioFragment).commit();
                MainActivity.bottomNavigationView.setSelectedItemId(R.id.horario);
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
        // Inflate the layout for this fragment
        return view;
    }
}