package com.example.quizforkids;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button RegisterButton, LoginButton;

    Animation shrink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RegisterButton = findViewById(R.id.RegisterButton);
        LoginButton = findViewById(R.id.LoginButton);

        RegisterButton.setOnClickListener(this);
        LoginButton.setOnClickListener(this);

        shrink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_click_shrink);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id ==R.id.RegisterButton){
            RegisterButton.startAnimation(shrink);
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        else if(id== R.id.LoginButton){
            LoginButton.startAnimation(shrink);
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}