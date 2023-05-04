package com.example.project_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MeteoriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meteorite);

        Intent intent = getIntent();
        Meteorite meteorite = (Meteorite) intent.getParcelableExtra("meteorite");

    }
}