package com.example.exhibitionguide;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MuseumPickActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum_pick);

        Button artButton = findViewById(R.id.artGalleryButton);
        artButton.setOnClickListener(clickListener);
        Button wwiButton = findViewById(R.id.wwiExhibitionButton);
        wwiButton.setOnClickListener(clickListener);
        Button spaceButton = findViewById(R.id.spaceExploringButton);
        spaceButton.setOnClickListener(clickListener);
        Button visualButton = findViewById(R.id.visualShowButton);
        visualButton.setOnClickListener(clickListener);
    }

    private final View.OnClickListener clickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MuseumPickActivity.this, DetailsActivity.class);
                    switch (v.getId()){
                        case R.id.artGalleryButton:
                            intent.putExtra(DetailsActivity.EXTRA_MESSAGE, "Art Gallery");
                            startActivity(intent);
                            break;
                        case R.id.wwiExhibitionButton:
                            intent.putExtra(DetailsActivity.EXTRA_MESSAGE, "WWI Exhibition");
                            startActivity(intent);
                            break;
                        case R.id.spaceExploringButton:
                            intent.putExtra(DetailsActivity.EXTRA_MESSAGE, "Exploring the Space");
                            startActivity(intent);
                            break;
                        case R.id.visualShowButton:
                            intent.putExtra(DetailsActivity.EXTRA_MESSAGE, "Visual Show");
                            startActivity(intent);
                            break;
                    }
                }
            };
}