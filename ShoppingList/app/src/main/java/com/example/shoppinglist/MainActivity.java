package com.example.shoppinglist;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText txtItemName, txtRemarks, txtID, txtQty;
    Button addItemButton, viewAllButton, updateButton,
            deleteButton, getRecodButton;
    DatabaseHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new DatabaseHelper(this, null, null, 1);

        txtID = (EditText) findViewById(R.id.txtID);
        txtItemName = (EditText) findViewById(R.id.txtItemName);
        txtItemName.requestFocus();
        txtRemarks = (EditText) findViewById(R.id.txtRemarks);
        txtQty = (EditText) findViewById(R.id.txtQty);


        addItemButton = (Button) findViewById(R.id.addItemButton);
        viewAllButton = (Button) findViewById(R.id.viewAllButton);
        updateButton = (Button) findViewById(R.id.updateButton);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        getRecodButton = (Button) findViewById(R.id.getRecordButton);

        addItemButton.setOnClickListener(clickListener);
        viewAllButton.setOnClickListener(clickListener);
        updateButton.setOnClickListener(clickListener);
        deleteButton.setOnClickListener(clickListener);
        getRecodButton.setOnClickListener(clickListener);
    }
    private final View.OnClickListener clickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.addItemButton:
                            addRecord();
                            break;
                        case R.id.viewAllButton:
                            viewAll();
                            break;
                        case R.id.updateButton:
                            updateRecord();
                            break;
                        case R.id.deleteButton:
                            deleteRecord();
                            break;
                        case R.id.getRecordButton:
                            getItem();;
                            break;
                    }
                }
            };

    public void addRecord() {
        boolean isInserted = myDB.insertData(txtItemName.getText().toString(),
                txtRemarks.getText().toString(),
                txtQty.getText().toString());

        if (isInserted == true) {
            Toast.makeText(MainActivity.this, "Record Added",Toast.LENGTH_LONG).show();
            resetDataFields();
        } else {
            Toast.makeText(MainActivity.this, "Error, no record added",Toast.LENGTH_LONG).show();
        }
    }

    public void viewAll() {
        Cursor res = myDB.viewAllRecords();
        if (res.getCount() == 0) {
            showMessage("Error", "No record found");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("ID :" + res.getString(0) + "\n");
            buffer.append("Item Name :" + res.getString(1) + "\n");
            buffer.append("Remarks :" + res.getString(2) + "\n");
            buffer.append("Quantity :" + res.getString(3) + "\n\n");
        }
        showMessage("Record(s) View:", buffer.toString());
    }

    public void updateRecord() {
        boolean isUpdated = myDB.updateRecord(txtID.getText().toString(),
                txtItemName.getText().toString(),
                txtRemarks.getText().toString(),
                txtQty.getText().toString());
        if (isUpdated == true) {
            Toast.makeText(MainActivity.this, "Record Updated", Toast.LENGTH_LONG).show();
            resetDataFields();
        } else {
            Toast.makeText(MainActivity.this, "Record Not Updated", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteRecord() {
        Integer deleteRow = myDB.deleteRecord(txtID.getText().toString());
        if (deleteRow > 0) {
            Toast.makeText(MainActivity.this, "Record Deleted", Toast.LENGTH_LONG).show();
            resetDataFields();
        } else {
            Toast.makeText(MainActivity.this, "No Record Deleted", Toast.LENGTH_LONG).show();
        }
    }

    public void getItem() {

        if (!txtID.getText().toString().isEmpty()) {
            int s = Integer.parseInt( txtID.getText().toString());
            Cursor res = myDB.viewRecord(s);
            if (res.getCount() == 0) {
                showMessage("Error", "No record found");
                return;
            }
            res.moveToFirst();
            txtID.setText(res.getString(0));
            txtItemName.setText(res.getString(1));
            txtRemarks.setText(res.getString(2));
            txtQty.setText(res.getString(3));
        } else {
            showMessage("Error", "Wrong ID"); }
    }

    public void showMessage(String title, String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.show();
    }

    public void resetDataFields(){
        txtID.setText("");
        txtItemName.setText("");
        txtRemarks.setText("");
        txtQty.setText("");
        txtItemName.requestFocus();
    }

}
