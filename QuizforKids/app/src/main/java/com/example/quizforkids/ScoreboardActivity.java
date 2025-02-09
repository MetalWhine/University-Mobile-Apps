package com.example.quizforkids;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class ScoreboardActivity extends AppCompatActivity implements View.OnClickListener {

    TextView TotalTextView, ScoreBoardTextView;

    Button SortByDateButton, SortByQuizButton, BackButton;

    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    Animation shrink;

    int Id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        shrink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_click_shrink);

        TotalTextView = findViewById(R.id.TotalScoreText);
        ScoreBoardTextView = findViewById(R.id.ScoreboardText);

        SortByDateButton = findViewById(R.id.SortByDateButton);
        SortByQuizButton = findViewById(R.id.SortByQuizButton);
        BackButton = findViewById(R.id.BackButton);

        SortByDateButton.setOnClickListener(this);
        SortByQuizButton.setOnClickListener(this);
        BackButton.setOnClickListener(this);

        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        String user = "USERNAME";
        String score = "SCORE";

        Cursor cursor = database.rawQuery("SELECT * FROM users WHERE loggedIn=1",null);
        if(cursor.moveToFirst()){
            int columnIndex = cursor.getColumnIndex("username");
            user = cursor.getString(columnIndex);
            columnIndex = cursor.getColumnIndex("totalScore");
            score = cursor.getString(columnIndex);
            columnIndex = cursor.getColumnIndex("id");
            Id = cursor.getInt(columnIndex);
        }
        cursor.close();

        database.close();

        loadData(Id, "");
        TotalTextView.setText("Hello " + user + ", your total score is: " + score);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.SortByDateButton){
            SortByDateButton.startAnimation(shrink);
            loadData(Id, "date");
        }
        else if (id == R.id.SortByQuizButton) {
            SortByQuizButton.startAnimation(shrink);
            loadData(Id, "quizName");
        }
        else if (id == R.id.BackButton) {
            BackButton.startAnimation(shrink);
            startActivity(getParentActivityIntent());
        }
    }

    private void loadData(int id, String sort){
        SQLiteDatabase database = databaseHelper.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM scores WHERE userID="+id, null);
        if (!sort.isEmpty()){
            cursor = database.rawQuery("SELECT * FROM scores WHERE userID=? ORDER BY " + sort, new String[]{String.valueOf(id)});
        }

        if(cursor.getCount()==0){
            ScoreBoardTextView.setText("No scores yet! Play some quizzes first and come back later!");
        }
        else {
            int i = 1;
            StringBuffer stringBuffer = new StringBuffer();
            while (cursor.moveToNext()){
                stringBuffer.append(i + ". ");
                stringBuffer.append(cursor.getString(1));
                stringBuffer.append(" - Points: " + cursor.getString(2));
                stringBuffer.append(" - Date attempted: " + cursor.getString(3) + "\n");
                i++;
            }
            ScoreBoardTextView.setText(stringBuffer.toString());
        }
        cursor.close();
    }
}