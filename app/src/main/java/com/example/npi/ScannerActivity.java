package com.example.npi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.github.tlaabs.timetableview.Schedule;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class ScannerActivity extends AppCompatActivity implements SensorEventListener, ActivityCompat.OnRequestPermissionsResultCallback, QRCodeReaderView.OnQRCodeReadListener {

    private static final int MY_PERMISSION_REQUEST_CAMERA = 0;

    private ViewGroup mainLayout;

    private TextView resultTextView;
    private QRCodeReaderView qrCodeReaderView;
    private CheckBox flashlightCheckBox;
    private CheckBox enableDecodingCheckBox;
    private PointsOverlayView pointsOverlayView;

    private Schedule asignatura;
    private int plantaActual,
                plantaDestino,
                aulaDestino;
    private SensorManager sensorManager;
    private Sensor pressure;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scanner);

        mainLayout = (ViewGroup) findViewById(R.id.scanner_layout);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        pressure = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        sensorManager.registerListener(
                (SensorEventListener) this,
                pressure,
                SensorManager.SENSOR_DELAY_NORMAL
        );

        // Comprobamos si tiene permisos
        // Si los tiene iniciamos la vista del lector QR
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            initQRCodeReaderView();
        } else {
            // Si no los tiene, los pide
            requestCameraPermission();
        }
    }

    @Override protected void onResume() {
        super.onResume();

        if (qrCodeReaderView != null) {
            qrCodeReaderView.startCamera();
        }
        sensorManager.registerListener(
                (SensorEventListener) this,
                pressure,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    @Override protected void onPause() {
        sensorManager.unregisterListener(this);

        if (qrCodeReaderView != null) {
            qrCodeReaderView.stopCamera();
        }
        super.onPause();
    }

    @Override public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                     int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != MY_PERMISSION_REQUEST_CAMERA) {
            return;
        }

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(mainLayout, "Permiso de c??mara concedido.", Snackbar.LENGTH_SHORT).show();
            initQRCodeReaderView();
        } else {
            Snackbar.make(mainLayout, "Permiso de c??mara denegado.", Snackbar.LENGTH_SHORT)
                    .show();
        }
    }


    // Cuando lee un c??digo QR
    // text: texto contenido en el c??digo
    // points: puntos principales del c??digo QR
    @Override public void onQRCodeRead(String text, PointF[] points) {
        String[] aux = text.split("\n");
        int izq_qr = Integer.parseInt(aux[0]);

        Direccion dir;

        if(aulaDestino <= izq_qr){
            dir = Direccion.IZQUIERDA;
        }else{
            dir = Direccion.DERECHA;
        }


        //resultTextView.setText(text);
        pointsOverlayView.setPoints(points, dir);
    }

    private void requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Snackbar.make(mainLayout, "Se requiere el acceso a la camara.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override public void onClick(View view) {
                    ActivityCompat.requestPermissions(ScannerActivity.this, new String[] {
                            Manifest.permission.CAMERA
                    }, MY_PERMISSION_REQUEST_CAMERA);
                }
            }).show();
        } else {
            Snackbar.make(mainLayout, "No tienes permiso de c??mara. Solicitando permiso.",
                    Snackbar.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.CAMERA
            }, MY_PERMISSION_REQUEST_CAMERA);
        }
    }

    private int calcularPlantaActual(float altura){
        if (altura >= 637.0){
            plantaActual = 3;
        }else if (altura < 637.0 && altura > 633.5){
            plantaActual = 2;
        }else if (altura < 633.5 && altura > 629.5){
            plantaActual = 1;
        }else if (altura < 629.5 && altura > 627.0){
            plantaActual = 0;
        }else{
            plantaActual = -1;
        }
        return plantaActual;
    }

    private void initQRCodeReaderView() {
        View content = getLayoutInflater().inflate(R.layout.content_decoder, mainLayout, true);

        qrCodeReaderView = (QRCodeReaderView) content.findViewById(R.id.qrdecoderview);
        resultTextView = (TextView) content.findViewById(R.id.result_text_view);
        flashlightCheckBox = (CheckBox) content.findViewById(R.id.flashlight_checkbox);
        enableDecodingCheckBox = (CheckBox) content.findViewById(R.id.enable_decoding_checkbox);
        pointsOverlayView = (PointsOverlayView) content.findViewById(R.id.points_overlay_view);

        Intent i = getIntent();
        int idx = i.getIntExtra("idx", -1);
        ArrayList<Schedule> schedules = (ArrayList<Schedule>)i.getSerializableExtra(
                "schedules");
        asignatura = schedules.get(idx);
        String aula = asignatura.getClassPlace();
        String[] partes = aula.split("\\.");
        plantaDestino = Integer.parseInt(partes[0]);
        aulaDestino = Integer.parseInt(partes[1]);


        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setBackCamera();
        flashlightCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                qrCodeReaderView.setTorchEnabled(isChecked);
            }
        });
        enableDecodingCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                qrCodeReaderView.setQRDecodingEnabled(isChecked);
            }
        });
        qrCodeReaderView.startCamera();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float presure_value = 0.0f;
        float height = 0.0f;

        if(Sensor.TYPE_PRESSURE == event.sensor.getType()){
            presure_value = event.values[0];
            height = SensorManager.getAltitude(
                    SensorManager.PRESSURE_STANDARD_ATMOSPHERE,
                    presure_value
            );

            calcularPlantaActual(height);
            //resultTextView.setText(Float.toString(height));
            if(plantaActual < plantaDestino){
                resultTextView.setText("Sube hasta la planta " + Integer.toString(plantaDestino));
            }else if(plantaActual > plantaDestino){
                resultTextView.setText("Baja hasta la planta " + Integer.toString(plantaDestino));
            }else{
                resultTextView.setText("Escanea el c??digo QR junto a las escaleras." +
                        " En el se indicar?? la direcci??n del aula");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}