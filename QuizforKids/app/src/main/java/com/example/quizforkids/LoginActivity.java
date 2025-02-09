package com.example.quizforkids;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity{
    EditText usernameField, passwordField;

    Button loginButton, backButton;

    Animation shrink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        shrink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_click_shrink);

        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();

        usernameField = findViewById(R.id.UsernameTextField);
        passwordField = findViewById(R.id.PasswordTextField);

        loginButton = findViewById(R.id.LoginButton);
        backButton = findViewById(R.id.BackButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.startAnimation(shrink);
                String Username = usernameField.getText().toString();
                String Password = passwordField.getText().toString();
                String[] projection = {"id"};
                String selection = "username = ? AND password = ?";
                String[] selectionArgs = {Username, Password};
                Cursor cursor = database.query(
                        "users",
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null
                );
                if(cursor.getCount() >0){
                    contentValues.put("loggedIn", "1");
                    database.update("users",contentValues,selection,selectionArgs);
                    Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                }
                else {
                    ShowError();
                }
                cursor.close();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButton.startAnimation(shrink);
                startActivity(getParentActivityIntent());
            }
        });

    }
    private void ShowError(){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Failed to login!").setMessage("The password or username you entered is incorrect").setCancelable(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}