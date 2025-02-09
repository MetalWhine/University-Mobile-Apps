package com.example.imageanimation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageView;
    Animation fade,move,rotate, scale;

    Button fadeB, moveB, rotateB, scaleB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        fadeB = findViewById(R.id.fade);
        moveB = findViewById(R.id.move);
        rotateB = findViewById(R.id.rotate);
        scaleB = findViewById(R.id.scaleButton);

        fadeB.setOnClickListener(this);
        moveB.setOnClickListener(this);
        rotateB.setOnClickListener(this);
        scaleB.setOnClickListener(this);

        fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        move = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move);
        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        scale = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fade){
            imageView.startAnimation(fade);
        }
        else if (id == R.id.rotate){
            imageView.startAnimation(rotate);
        }
        else if (id == R.id.move){
            imageView.startAnimation(move);
        }
        else if (id == R.id.scaleButton){
            imageView.startAnimation(scale);
        }
    }
}