package com.example.npi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class NFCActivity extends AppCompatActivity {

    public static final String Error_Detectado = "No se ha detectado la etiqueta NFC";
    public static final String Exito_Escritura = "Se ha pasado lista correctamente.";
    public static final String Error_Escritura = "Error en la escritura. Prueba de nuevo";

    String usuario = "77146828K";

    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writingTagFilters[];
    boolean modoEscritura;
    Tag myTag;
    Context context;
    TextView contenidos_nfc;
    TextView edit_message;
    Button ActivateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcactivity);
        edit_message = (TextView) findViewById(R.id.edit_message);
        contenidos_nfc = (TextView) findViewById(R.id.nfc_contents);
        ActivateButton = findViewById(R.id.activateButton);
        context = this;

        ActivateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(myTag == null){
                        Toast.makeText(context, Error_Detectado, Toast.LENGTH_LONG).show();
                    }
                    else{
                        write(usuario.toString(), myTag);
                        Toast.makeText(context, Exito_Escritura, Toast.LENGTH_LONG).show();
                    }
                }
                catch (IOException e){
                    Toast.makeText(context, Exito_Escritura, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                catch (FormatException e){
                    Toast.makeText(context, Exito_Escritura, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter == null){
            Toast.makeText(this, "Este dispositivo no sporta NFC.", Toast.LENGTH_SHORT).show();
        }
        readfromIntent(getIntent());
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetectado = new IntentFilter((NfcAdapter.ACTION_TAG_DISCOVERED));
        tagDetectado.addCategory(Intent.CATEGORY_DEFAULT);
        writingTagFilters = new IntentFilter[]{tagDetectado};
    }

    private void readfromIntent(Intent intent){
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
        || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
        || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)){
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage [] msgs = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; ++i){
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            buildTagViews(msgs);
        }
    }
    private void buildTagViews(NdefMessage [] msgs){
        if (msgs == null || msgs.length == 0) return;

        String text = "";
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16" ;
        int languageCodeLength = payload[0] & 0063; // Cogemos el código del lenguaje, por ejemplo, "en" de inglés

        try{
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }
        catch(UnsupportedEncodingException e){
            Log.e("UnsupportedEncoding", e.toString());
        }
        contenidos_nfc.setText("Se ha registrado en la asignatura " + text + " el usuario " + usuario);
    }

    private void write(String text, Tag tag) throws  IOException, FormatException{
        NdefRecord [] records = {createRecord(text)};
        NdefMessage message = new NdefMessage(records);

        Ndef ndef = Ndef.get(tag);

        ndef.connect();
        ndef.writeNdefMessage(message);
        ndef.close();
    }

    private NdefRecord createRecord (String text) throws UnsupportedEncodingException {
        String lang = "en";
        byte[] textBytes = text.getBytes();
        byte[] langBytes = lang.getBytes("US-ASCII");
        int langLength = langBytes.length;
        int textLength = textBytes.length;
        byte[] payload = new byte [1 + langLength + textLength];

        payload[0] = (byte) langLength;

        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, 1+langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);

        return recordNFC;
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        setIntent(intent);
        readfromIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        WriteModeOff();
    }

    @Override
    public void onResume(){
        super.onResume();
        WriteModeOn();
    }

    private void WriteModeOn(){
        modoEscritura = true;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writingTagFilters, null);
    }

    private void WriteModeOff(){
        modoEscritura = false;
        nfcAdapter.disableForegroundDispatch(this);
    }
}