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
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class CartoonsQuizActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseHelper databaseHelper = new DatabaseHelper(this);

    TextView QuestionTextView;

    Button Answer1Button, Answer2Button, Answer3Button, PreviousButton;

    List<CartoonQuestion> CartoonQuestions;

    int CurrentQuestion = 0;
    int CorrectAnswers = 0;
    int IncorrectAnswers = 0;
    boolean isPrevCorrect = false;

    Animation shrink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartoons_quiz);

        shrink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_click_shrink);

        QuestionTextView = findViewById(R.id.QuestionsText);
        Answer1Button = findViewById(R.id.AnswerButton1);
        Answer2Button = findViewById(R.id.AnswerButton2);
        Answer3Button = findViewById(R.id.AnswerButton3);
        PreviousButton = findViewById(R.id.PreviousButton);

        Answer1Button.setOnClickListener(this);
        Answer2Button.setOnClickListener(this);
        Answer3Button.setOnClickListener(this);
        PreviousButton.setOnClickListener(this);

        CartoonQuestions = generateQuestions();
        if(!CartoonQuestions.isEmpty()){
            displayQuestion();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.AnswerButton1){
            Answer1Button.startAnimation(shrink);
            check(0);
        }
        else if (id == R.id.AnswerButton2) {
            Answer2Button.startAnimation(shrink);
            check(1);
        }
        else if (id == R.id.AnswerButton3) {
            Answer3Button.startAnimation(shrink);
            check(2);
        }
        else if (id == R.id.PreviousButton) {
            PreviousButton.startAnimation(shrink);
            CurrentQuestion--;
            if(CurrentQuestion >= 0){
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

    public class CartoonQuestion{
        public String question;
        public String[] answers;
        public int correctAnswer;

        public CartoonQuestion(String question, String[] answers, int correctAnswer){
            this.question = question;
            this.answers = answers;
            this.correctAnswer = correctAnswer;
        }
        public boolean checkIfCorrectAnswer(int answer){
            return correctAnswer == answer;
        }
    }

    private void check(int answerButton){
        CartoonQuestion question = CartoonQuestions.get(CurrentQuestion);
        if(question.checkIfCorrectAnswer(answerButton)){
            CorrectAnswers++;
            isPrevCorrect = true;
        }
        else {
            IncorrectAnswers++;
            isPrevCorrect = false;
        }

        if (CurrentQuestion == 3){
            finishQuiz();
        }
        else {
            CurrentQuestion++;
            displayQuestion();
        }
    }

    private void displayQuestion(){
        CartoonQuestion question = CartoonQuestions.get(CurrentQuestion);
        QuestionTextView.setText(question.question);
        Answer1Button.setText(question.answers[0]);
        Answer2Button.setText(question.answers[1]);
        Answer3Button.setText(question.answers[2]);
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

    private List<CartoonQuestion> generateQuestions(){
        List<CartoonQuestion> cartoonQuestions = new ArrayList<>();
        String[] questions = {"What shape is Spongebob from Spongebob Squarepants?", "What shape is Patrick from Spongebob Squarepants?", "What animal is Mr.Krabs from Spongebob Squarepants?",
                "What animal is Tom from Tom & Jerry?", "What animal is Jerry from Tom & Jerry?", "What animal is Spike from Tom & Jerry?",
                "What animal is Mickey from Mickey Mouse?", "What animal is Donald from Mickey Mouse?", "What animal is Goofy from Mickey Mouse?", "Who is Mickey's pet dog from Mickey Mouse?"};
        String[] answer1 = {"Square", "Hexagon", "Fish", "Cat", "Bird", "Bear", "Mouse", "Chicken", "Cat", "Pluto"};
        String[] answer2 = {"Circle", "Star", "Squid", "Lion", "Mouse", "Wolf", "Spider", "Duck", "Mouse", "Barto"};
        String[] answer3 = {"Triangle", "Triangle", "Crab", "Fox", "Bug", "Dog", "Snake", "Eagle", "Dog", "Goodo"};
        int[] correctAnswer = {0,1,2,0,1,2,0,1,2,0};

        List<Integer> AnswerIndexes = new ArrayList<>();
        for (int i = 0; 4 > i; i++){
            int random = new Random().nextInt(10);
            while (AnswerIndexes.contains(random)){
                random = new Random().nextInt(10);
            }
            AnswerIndexes.add(random);
            String[] answers = {answer1[random],answer2[random],answer3[random]};
            cartoonQuestions.add(new CartoonQuestion(questions[random],answers,correctAnswer[random]));
        }
        return cartoonQuestions;
    }

    private void insertScore(int score, int id){
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("''dd-MM-yyyy ' 'HH:mm");
        String currentDate = sdf.format(new Date());

        contentValues.put("quizName", "Cartoon Quiz");
        contentValues.put("points", score);
        contentValues.put("date", currentDate);
        contentValues.put("userID", id);

        database.insert("scores", null,contentValues);
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