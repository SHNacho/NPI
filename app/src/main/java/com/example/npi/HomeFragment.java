package com.example.npi;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.*;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import in.championswimmer.sfg.lib.SimpleFingerGestures;

public class HomeFragment extends Fragment {

    TextView text;

    public HomeFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        text = view.findViewById(R.id.homeFragment);


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
                text.setText("Double tap");
                return false;
            }
        });


        view.setOnTouchListener(MainActivity.sfg);
        return view;
    }
}