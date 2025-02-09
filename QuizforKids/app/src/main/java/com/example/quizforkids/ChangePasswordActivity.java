package com.example.quizforkids;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    EditText passwordChangeField;

    Button changePasswordButton, backButton;

    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    Animation shrink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        shrink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_click_shrink);

        passwordChangeField = findViewById(R.id.NewPasswordTextField);

        changePasswordButton = findViewById(R.id.ChangePasswordButton);
        backButton = findViewById(R.id.BackButton);

        changePasswordButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.ChangePasswordButton){
            changePasswordButton.startAnimation(shrink);
            String NewPassword = passwordChangeField.getText().toString();
            if (NewPassword.isEmpty()){
                ShowMessage("Field Error!", "New Password cannot be blank!");
            }
            else {
                SQLiteDatabase database = databaseHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("password", NewPassword);
                String whereClause = "loggedIn = ?";
                String[] whereArgs = {"1"};
                database.update("users", contentValues, whereClause, whereArgs);
                database.close();
                ShowMessage("Succesfull Change!", "Your password has been changed!");
            }
        }
        else if (id == R.id.BackButton) {
            backButton.startAnimation(shrink);
            startActivity(getParentActivityIntent());
        }
    }

    private void ShowMessage(String Title, String Message){
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