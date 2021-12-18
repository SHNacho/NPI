package com.example.npi;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class BotWebActivity extends AppCompatActivity {
    WebView web;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        web = new WebView(this);
        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
        setContentView(web);

        web.loadUrl("https://console.dialogflow.com/api-client/demo/embedded/0beee468-5253-4cfb-a1f6-83275de83d5e");

    }

}
