package com.example.quizforkids;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText emailField, usernameField, passwordField;

    Button registerButton, backButton;

    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    String Username, Password, Email;

    Animation shrink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        shrink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_click_shrink);

        emailField = findViewById(R.id.EmailTextField);
        usernameField = findViewById(R.id.UsernameTextField);
        passwordField = findViewById(R.id.PasswordTextField);
        registerButton = findViewById(R.id.RegisterButton);
        backButton = findViewById(R.id.BackButton);

        registerButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.RegisterButton){
            registerButton.startAnimation(shrink);
            Email = emailField.getText().toString();
            Username = usernameField.getText().toString();
            Password = passwordField.getText().toString();
            if(Email.isEmpty() || Username.isEmpty() || Password.isEmpty()){
                ShowError("Field Error!", "Please fill in all fields!");
            }
            else {
                SQLiteDatabase database = databaseHelper.getWritableDatabase();
                Cursor cursor = database.rawQuery("SELECT * FROM users WHERE username=? OR email=?", new String[]{Username});
                boolean SameData = cursor.moveToFirst();
                cursor.close();
                if (SameData){
                    ShowError("Existing User Error", "Username/Email already exists! Try a different username/email");
                }
                else {
                    ContentValues values = new ContentValues();
                    values.put("email", Email);
                    values.put("username", Username);
                    values.put("password", Password);
                    values.put("totalScore", 0);
                    values.put("loggedIn", "1");

                    long Id = -1;
                    try {
                        Id = database.insertOrThrow("users", null, values);
                    } catch (android.database.SQLException e) {
                        Log.d("Registration", "Error registering user", e);
                    }

                    if (Id == -1){
                        ShowError("Failed to register!", "Something went wrong");
                    }
                    else {
                        Intent intent = new Intent(RegisterActivity.this, MainMenuActivity.class);
                        startActivity(intent);
                    }
                }
                database.close();
            }
        }
        else if(id == R.id.BackButton){
            backButton.startAnimation(shrink);
            startActivity(getParentActivityIntent());
        }
    }

    private void ShowError(String Title, String Message){
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle(Title).setMessage(Message).setCancelable(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}