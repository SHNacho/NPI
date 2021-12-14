package com.example.npi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class contactosActivity extends AppCompatActivity {
    static ImageView microNombre;
    static ImageView microCorreo;
    static ImageView microAsunto;
    static ImageView microContenido;
    static SpeechRecognizer speechRecognizer;
    static Intent speechRecognizerIntent;
    int escribiendo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactos);

        final EditText your_name = (EditText) findViewById(R.id.your_name);
        final EditText your_email = (EditText) findViewById(R.id.your_email);
        final EditText your_subject = (EditText) findViewById(R.id.your_subject);
        final EditText your_message = (EditText) findViewById(R.id.your_message);
        microNombre = (ImageView) findViewById(R.id.microNombre);
        microCorreo = (ImageView) findViewById(R.id.microEmail);
        microAsunto = (ImageView) findViewById(R.id.microAsunto);
        microContenido = (ImageView) findViewById(R.id.microContenido);
        Button email = (Button) findViewById(R.id.post_message);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                switch (escribiendo){
                    case 1:
                        your_name.setText(data.get(0));
                        break;
                    case 2:
                        your_email.setText(data.get(0));
                        break;
                    case 3:
                        your_subject.setText(data.get(0));
                        break;
                    case 4:
                        your_message.setText(data.get(0));
                        break;
                }

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = your_name.getText().toString();
                String email = your_email.getText().toString();
                String subject = your_subject.getText().toString();
                String message = your_message.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    your_name.setError("Inserta tu nombre");
                    your_name.requestFocus();
                    return;
                }

                Boolean onError = false;
                if (!esValido(email)) {
                    onError = true;
                    your_email.setError("e-mail del destinatario inválido");
                    return;
                }

                if (TextUtils.isEmpty(subject)) {
                    your_subject.setError("Inserta el asunto");
                    your_subject.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(message)) {
                    your_message.setError("Inserta el mensaje");
                    your_message.requestFocus();
                    return;
                }

                Intent sendEmail = new Intent(android.content.Intent.ACTION_SEND);

                sendEmail.setType("plain/text");
                sendEmail.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {
                        email
                });
                sendEmail.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
                sendEmail.putExtra(android.content.Intent.EXTRA_TEXT,
                        "Nombre:" + name + '\n' + "Dirección de correo:" + email + '\n' + "Mensaje:" + '\n' + message);

                startActivity(Intent.createChooser(sendEmail, "Enviar mail..."));

            }
        });

        microNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escribiendo = 1;
                speechRecognizer.startListening(speechRecognizerIntent);
            }
        });

        microCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escribiendo = 2;
                speechRecognizer.startListening(speechRecognizerIntent);
            }
        });

        microAsunto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escribiendo = 3;
                speechRecognizer.startListening(speechRecognizerIntent);
            }
        });

        microContenido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                escribiendo = 4;
                speechRecognizer.startListening(speechRecognizerIntent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //Get a Tracker (should auto-report)
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    // Para comprobar si el eMail es válido
    private boolean esValido(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}