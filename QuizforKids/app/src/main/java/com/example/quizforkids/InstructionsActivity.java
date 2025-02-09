package com.example.quizforkids;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class InstructionsActivity extends AppCompatActivity {

    Button backButton;

    Animation shrink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        shrink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_click_shrink);

        backButton = findViewById(R.id.BackButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButton.setAnimation(shrink);
                startActivity(getParentActivityIntent());
            }
        });
    }
}