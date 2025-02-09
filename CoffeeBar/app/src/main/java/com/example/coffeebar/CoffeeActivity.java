package com.example.coffeebar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class CoffeeActivity extends AppCompatActivity {
    public static final String EXTRA_COFFEEID = "coffeeID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coffee);
        //Get the coffeeID from the intent
        int drinkID = (Integer)getIntent().getExtras().get(EXTRA_COFFEEID);
        Coffee coffee = Coffee.coffees[drinkID];

        //insert associated fields
        TextView name = (TextView) findViewById(R.id.name);
        name.setText(coffee.getName());

        TextView description = (TextView) findViewById(R.id.description);
        description.setText(coffee.getDescription());

        ImageView photo = (ImageView)findViewById(R.id.photo);
        photo.setImageResource(coffee.getImgResourceID());
        photo.setContentDescription(coffee.getName());
    }
}
