package com.example.mymessenger;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ViewMessageActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_message);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String messageText = intent.getStringExtra(EXTRA_MESSAGE);

        TextView messageView = (TextView)findViewById(R.id.theMessage);
        messageView.setText(messageText);
    }
}