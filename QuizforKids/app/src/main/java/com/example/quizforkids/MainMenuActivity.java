package com.example.quizforkids;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    Button ScoreButton, InstructionButton, AnimalQuizButton, CartoonQuizButton, ChangePasswordButton, LogoutButton;

    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    Animation shrink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ScoreButton = findViewById(R.id.ScoresButton);
        InstructionButton = findViewById(R.id.InstructionsButton);
        AnimalQuizButton = findViewById(R.id.AnimalQuizButton);
        CartoonQuizButton = findViewById(R.id.CartoonsQuizButton);
        ChangePasswordButton = findViewById(R.id.ChangePasswordButton);
        LogoutButton = findViewById(R.id.LogoutButton);

        ScoreButton.setOnClickListener(this);
        InstructionButton.setOnClickListener(this);
        AnimalQuizButton.setOnClickListener(this);
        CartoonQuizButton.setOnClickListener(this);
        ChangePasswordButton.setOnClickListener(this);
        LogoutButton.setOnClickListener(this);

        shrink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_click_shrink);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ScoresButton) {
            ScoreButton.startAnimation(shrink);
            Intent intent = new Intent(MainMenuActivity.this, ScoreboardActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.InstructionsButton) {
            InstructionButton.startAnimation(shrink);
            Intent intent = new Intent(MainMenuActivity.this, InstructionsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.AnimalQuizButton) {
            AnimalQuizButton.startAnimation(shrink);
            Intent intent = new Intent(MainMenuActivity.this, AnimalsQuizActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.CartoonsQuizButton) {
            CartoonQuizButton.startAnimation(shrink);
            Intent intent = new Intent(MainMenuActivity.this, CartoonsQuizActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.ChangePasswordButton) {
            ChangePasswordButton.startAnimation(shrink);
            Intent intent = new Intent(MainMenuActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.LogoutButton) {
            LogoutButton.startAnimation(shrink);
            logout();
        }
    }


    private void logout(){
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        String user = "USERNAME";
        String score = "SCORE";

        Cursor cursor = database.rawQuery("SELECT * FROM users WHERE loggedIn=1",null);
        if(cursor.moveToFirst()){
            int columnIndex = cursor.getColumnIndex("username");
            user = cursor.getString(columnIndex);
            columnIndex = cursor.getColumnIndex("totalScore");
            score = cursor.getString(columnIndex);
        }
        cursor.close();

        ContentValues contentValues = new ContentValues();
        contentValues.put("loggedIn", "0");
        String whereClause = "loggedIn = ?";
        String[] whereArgs = {"1"};
        database.update("users", contentValues, whereClause, whereArgs);

        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Logged Out Succesfully!").setMessage(user + ", your total points is: " + score).setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        builder.show();
        database.close();
    }
}