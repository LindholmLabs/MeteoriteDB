package com.example.project_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        WebView aboutView = findViewById(R.id.aboutWebView);
        aboutView.loadUrl("file:///android_asset/index.html");
    }
}