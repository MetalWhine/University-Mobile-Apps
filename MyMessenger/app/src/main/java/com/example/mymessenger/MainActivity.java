package com.example.mymessenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public boolean onCreateOptionsMenu(Menu menu){
        //inflate the menu - adds items to the app bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        switch (item.getItemId()){
            case R.id.action_create_msg:
                Intent intent = new Intent(this, CreateMessageActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_sendby_msg:
                Intent intent_1;
                intent_1 = new Intent(this, SendbyMessageActivity.class);
                intent_1.putExtra(SendbyMessageActivity.EXTRA_MESSAGE,"This message is hardcoded from main activity");
                startActivity(intent_1);
                return true;
            case R.id.action_new_item:
                Intent intent_2;
                intent_2 = new Intent(this, NewItemActivity.class);
                startActivity(intent_2);
                return true;
            case R.id.action_new_item2:
                Intent intent_3;
                intent_3 = new Intent(this, NewItemActivity.class);
                startActivity(intent_3);
                return true;
            default:

        }
        return super.onOptionsItemSelected(item);
    }

}