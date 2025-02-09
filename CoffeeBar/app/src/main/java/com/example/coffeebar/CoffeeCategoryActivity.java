package com.example.coffeebar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class CoffeeCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee_category);
        ListView listCoffees = (ListView) findViewById(R.id.list_coffees);

        // adapterView to add items from the class Coffee (object coffees) to ListView
        ArrayAdapter<Coffee> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, Coffee.coffees);
        //A new ArrayAdapter is initialized to bind each item in the coffees array to a TextView
        // that exists in the simple_list_item_1 layout (this is a layout provided by Android and provides a standard appearance for text in a list)

        listCoffees.setAdapter(listAdapter);
        listCoffees.setOnItemClickListener(itemClickListener);
    }

    //listener for the items
    AdapterView.OnItemClickListener itemClickListener =
            new AdapterView.OnItemClickListener(){
                public void onItemClick(AdapterView<?> listCoffees, View itemView, int position, long id){
                    //creating intent that will start CoffeeActivity
                    Intent intent = new Intent (CoffeeCategoryActivity.this, CoffeeActivity.class);
                    //we'll add additional value EXTRA_COFFEEID -> row ID of the drink the user clicked on
                    //so the CoffeeActivity knows what to display
                    intent.putExtra(CoffeeActivity.EXTRA_COFFEEID, (int)id);

                    startActivity(intent);
                }
            };
}
