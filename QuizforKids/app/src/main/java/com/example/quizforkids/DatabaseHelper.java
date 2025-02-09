package com.example.quizforkids;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    String UserTable = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, username TEXT, password TEXT, totalScore INTEGER, loggedIn TEXT)";
    String ScoresTable = "CREATE TABLE scores (id INTEGER PRIMARY KEY AUTOINCREMENT, quizName TEXT, points INTEGER, date TEXT, userID INTEGER)";

    public DatabaseHelper(Context context) {
        super(context, "Database.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(UserTable);
        db.execSQL(ScoresTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS INTEGER");
        onCreate(db);
    }
}
