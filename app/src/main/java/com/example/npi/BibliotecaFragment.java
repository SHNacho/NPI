package com.example.npi;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import in.championswimmer.sfg.lib.SimpleFingerGestures;

public class BibliotecaFragment extends Fragment {

    public BibliotecaFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_biblioteca, container, false);


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
                    getParentFragmentManager().beginTransaction().replace(R.id.container, MainActivity.homeFragment).commit();
                    MainActivity.bottomNavigationView.setSelectedItemId(R.id.home);
                }
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
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        MainActivity.next_fragment_left = "home";
        MainActivity.next_fragment_right = "horario";
    }
}