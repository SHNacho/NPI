package com.example.npi;
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

import in.championswimmer.sfg.lib.SimpleFingerGestures;

public class HomeFragment extends Fragment {

    TextView text;
    static EditText editText;
    static ImageView micButton;
    static Button ttsButton;

    public HomeFragment(){
        // require a empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        text = view.findViewById(R.id.homeFragment);

        editText = view.findViewById(R.id.text);
        micButton = view.findViewById(R.id.button);
        ttsButton = view.findViewById(R.id.tts);

        micButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    MainActivity.speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    micButton.setImageResource(R.drawable.baseline_mic_red_400_24dp);
                    editText.setText("...");
                    MainActivity.speechRecognizer.startListening(MainActivity.speechRecognizerIntent);
                }
                return false;
            }
        });

        ttsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String text = String.valueOf(editText.getText());
                if (!text.isEmpty())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        MainActivity.textToSpeechEngine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts1");
                    }
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
}