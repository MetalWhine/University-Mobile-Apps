package com.example.shoppinglist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "shoppinglist.db";
    public static final String TABLE_NAME = "items";

    public static final String COL_1 = "ID";
    public static final String COL_2 = "ITEMNAME";
    public static final String COL_3 = "REMARKS";
    public static final String COL_4 = "QTY";

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    //creating the table
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ITEMNAME TEXT, REMARKS TEXT, QTY INTEGER)");
    }

    // called when the database needs to be upgraded to the new schema version
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //adding a record
    public boolean insertData(String itemname, String remarks, String qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //This class is used to store a set of values that the ContentResolver can process
        contentValues.put(COL_2, itemname);
        contentValues.put(COL_3, remarks);
        contentValues.put(COL_4, qty);

        long result = db.insert(TABLE_NAME, null, contentValues);
        // null - the framework does not insert a row when there are no values

        if (result == -1)
            return false;
        else
            return true;
    }

    //retrieving all records
    public Cursor viewAllRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return res;
    }

    //retrieving single record matching the id
    public Cursor viewRecord(int id) {

        SQLiteDatabase db = this.getWritableDatabase();
    /*
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME +
                " WHERE ID=" +id, null);
     */
        String[] projection = new String[] { COL_1, COL_2, COL_3, COL_4 };
        String selection = COL_1 + " = ?";
        String[] selectionArgs = new String[]{String.valueOf(id)};
        String sortOrder = COL_1 + " DESC";
        Cursor res = db.query(
                TABLE_NAME,             // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order (or null in no need to sort)
        );

        return res;
    }

    //updating the record matching the id
    public boolean updateRecord(String id, String itemname, String remarks,
                                String qty){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_2, itemname);
        contentValues.put(COL_3, remarks);
        contentValues.put(COL_4, qty);

        db.update(TABLE_NAME, contentValues,"ID = ?",
                new String[] {id});
        return true;
    }

    //deleting the record matching the id
    public Integer deleteRecord(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return  db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }
}

