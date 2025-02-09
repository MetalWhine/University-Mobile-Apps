package com.example.quizforkids;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class AnimalsQuizActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView AnimalImageView;

    EditText AnswerTextField;

    Button NextButton, PreviousButton;

    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    Animation shrink;

    List<AnimalQuestion> AnimalQuestions;

    int CurrentQuestion = 0;
    int CorrectAnswers = 0;
    int IncorrectAnswers = 0;
    boolean isPrevCorrect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animals_quiz);

        shrink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_click_shrink);

        AnimalImageView = findViewById(R.id.AnimalImage);

        AnswerTextField = findViewById(R.id.AnswerTextField);

        NextButton = findViewById(R.id.NextButton);
        PreviousButton = findViewById(R.id.PreviousButton);

        NextButton.setOnClickListener(this);
        PreviousButton.setOnClickListener(this);

        AnimalQuestions = generateQuestions();
        if(!AnimalQuestions.isEmpty()){
            displayQuestion();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.NextButton){
            NextButton.startAnimation(shrink);
            if (CurrentQuestion == 3){
                check();
                finishQuiz();
            }
            else {
                check();
                CurrentQuestion++;
                displayQuestion();
            }
        }
        else if (id == R.id.PreviousButton) {
            PreviousButton.startAnimation(shrink);
            CurrentQuestion--;
            if (CurrentQuestion >= 0){
                if (isPrevCorrect){
                    CorrectAnswers--;
                    isPrevCorrect = false;
                }
                else{
                    IncorrectAnswers--;
                }
                displayQuestion();
            }
            else {
                ShowMessage("Error!", "This is the first question!");
            }
        }
    }

    public class AnimalQuestion{
        private int imageId;
        private String correctAnswer;

        public AnimalQuestion(int imageId, String correctAnswer){
            this.imageId = imageId;
            this.correctAnswer = correctAnswer;
        }

        public int getImageId(){return imageId;}

        public boolean checkIfCorrectAnswer(String answer){
            return correctAnswer.toLowerCase().equals(answer.toLowerCase());
        }
    }

    private void displayQuestion(){
        AnimalQuestion question = AnimalQuestions.get(CurrentQuestion);
        AnimalImageView.setImageResource(question.getImageId());
        AnswerTextField.setText("");
    }

    private void finishQuiz(){
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        int score = 0;
        int id = 0;

        Cursor cursor = database.rawQuery("SELECT * FROM users WHERE loggedIn=1",null);
        if(cursor.moveToFirst()){
            int columnIndex = cursor.getColumnIndex("totalScore");
            score = cursor.getInt(columnIndex);
            columnIndex = cursor.getColumnIndex("id");
            id = cursor.getInt(columnIndex);
        }
        cursor.close();

        int runScore = (CorrectAnswers*3) - IncorrectAnswers;
        if (runScore < 0) runScore = 0;
        score += runScore;

        ContentValues contentValues = new ContentValues();
        contentValues.put("totalScore", score);
        String whereClause = "loggedIn = ?";
        String[] whereArgs = {"1"};
        database.update("users", contentValues, whereClause, whereArgs);

        insertScore(runScore, id);

        String message = "Congrats on finishing the Animal Quiz!\n " +
                "You got " + CorrectAnswers +" out of 4 questions correct\n" +
                "And got "+ runScore +" points, you have "+ score +" points overall";
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setTitle("Finished Quiz!").setMessage(message).setCancelable(true);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                startActivity(getParentActivityIntent());
            }
        });
        builder.show();
    }

    private void insertScore(int score, int id){
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("''dd-MM-yyyy ' 'HH:mm");
        String currentDate = sdf.format(new Date());

        contentValues.put("quizName", "Animal Quiz");
        contentValues.put("points", score);
        contentValues.put("date", currentDate);
        contentValues.put("userID", id);

        database.insert("scores", null,contentValues);
    }

    private void check(){
        AnimalQuestion question = AnimalQuestions.get(CurrentQuestion);
        String answer = AnswerTextField.getText().toString().trim().toLowerCase();
        if(answer.isEmpty()){
            IncorrectAnswers++;
            isPrevCorrect = false;
        }
        else {
            if(question.checkIfCorrectAnswer(answer)){
                CorrectAnswers++;
                isPrevCorrect = true;
            }
            else {
                IncorrectAnswers++;
                isPrevCorrect = false;
            }
        }
    }

    private List<AnimalQuestion> generateQuestions(){
        List<AnimalQuestion> animalQuestions = new ArrayList<>();
        int[] ImageIDs = {R.drawable.cat, R.drawable.cow, R.drawable.dog, R.drawable.elephant, R.drawable.giraffe, R.drawable.monkey, R.drawable.penguin, R.drawable.sheep, R.drawable.squirrel, R.drawable.tiger};
        String[] AllAnswers = {"Cat","Cow","Dog","Elephant","Giraffe","Monkey","Penguin","Sheep","Squirrel","Tiger"};

        List<Integer> AnswerIndexes = new ArrayList<>();
        for (int i = 0; 4 > i; i++){
            int random = new Random().nextInt(10);
            while (AnswerIndexes.contains(random)){
                random = new Random().nextInt(10);
            }
            AnswerIndexes.add(random);
            animalQuestions.add(new AnimalQuestion(ImageIDs[random], AllAnswers[random]));
        }
        return animalQuestions;
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