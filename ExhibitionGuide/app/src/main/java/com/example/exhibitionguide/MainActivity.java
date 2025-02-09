package com.example.exhibitionguide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button toMuseumPick = findViewById(R.id.toMuseumPick);

        toMuseumPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MuseumPickScene();
            }
        });
    }

    private void MuseumPickScene(){
        Intent intent = new Intent(MainActivity.this, MuseumPickActivity.class);
        startActivity(intent);
    }
}