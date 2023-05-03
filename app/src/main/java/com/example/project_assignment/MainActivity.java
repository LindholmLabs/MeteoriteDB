package com.example.project_assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("FieldCanBeLocal")
public class MainActivity extends AppCompatActivity  implements JsonTask.JsonTaskListener {

    private final String JSON_URL = "https://data.nasa.gov/resource/y77d-th95.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new JsonTask(this).execute(JSON_URL);
    }

    @Override
    public void onPostExecute(String json) {
        Gson gson = new Gson();

        Type type = new TypeToken<List<Meteorite>>() {}.getType();
        ArrayList<Meteorite> meteorites = gson.fromJson(json, type);

        for (Meteorite m : meteorites) {
            Log.d("Meteorite: ", m.toString());
        }
    }
}