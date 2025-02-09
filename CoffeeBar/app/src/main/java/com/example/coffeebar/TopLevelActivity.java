package com.example.coffeebar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class TopLevelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_level);

        //in method onCreate(…)
        ListView listView = (ListView) findViewById(R.id.list_options);
        listView.setOnItemClickListener(itemClickListener);
    }

    //implementation of the listener – after method onCreate(…)
    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> listView, View itemView, int position, long id) {
            if (position==0){
// if the selected position of the item within the view ==0 , user choice is coffee
// position - The position of the view (item) in the adapter (ListView),
// id - The row id of the item that was clicked.
                Intent intent = new Intent(TopLevelActivity.this, CoffeeCategoryActivity.class);
                startActivity(intent);
            }
            else { //if the user selects other then coffee
                CharSequence text = "position=" + position + "row id=" + id;
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();
            }
        }
    };
}