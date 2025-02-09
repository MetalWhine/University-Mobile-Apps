package com.example.exhibitionguide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;
import android.content.Intent;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DetailsActivity extends AppCompatActivity {

    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    public static final String EXTRA_MESSAGE = "thing";
    private TextView ResultLabel;
    private TextView Visitors;
    private String exhibition;

    private int basePrice;
    private int people = 1;
    private final ArrayList<String> daysOfTheWeek = new ArrayList<>();

    final Calendar c = Calendar.getInstance();
    private int day = c.get(Calendar.DAY_OF_WEEK)-1;

    private int hour = c.get(Calendar.HOUR_OF_DAY);
    private int minutes = (hour*60);

    private boolean isVisualShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        isVisualShow = false;

        daysOfTheWeek.add("Sunday");
        daysOfTheWeek.add("Monday");
        daysOfTheWeek.add("Tuesday");
        daysOfTheWeek.add("Wednesday");
        daysOfTheWeek.add("Thursday");
        daysOfTheWeek.add("Friday");
        daysOfTheWeek.add("Saturday");

        Intent intent = getIntent();
        exhibition = intent.getStringExtra(EXTRA_MESSAGE);

        switch (exhibition) {
            case "Art Gallery":
                basePrice = 25;
                break;
            case "WWI Exhibition":
                basePrice = 20;
                break;
            case "Exploring the Space":
                basePrice = 30;
                break;
            case "Visual Show":
                isVisualShow = true;
                basePrice = 40;
                break;
        }

        Visitors = findViewById(R.id.visitorCountLabel);

        SeekBar seekBar = findViewById(R.id.visitorCount);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                people = progress;
                Calculate();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Day slider settings
        SeekBar daySeekBar = findViewById(R.id.daySlider);
        TextView dayLabel = findViewById(R.id.dayLabel);
        String a = daysOfTheWeek.get(day);
        dayLabel.setText(a);
        daySeekBar.setProgress(day);
        daySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                day = progress;
                String a = daysOfTheWeek.get(day);
                dayLabel.setText(a);
                Calculate();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // Time slider settings
        int b = c.get(Calendar.MINUTE);
        if (b <= 30){
            minutes += 30;
        }
        else{
            minutes += 60;
            hour += 1;
        }

        SeekBar timeSeekBar = findViewById(R.id.timeSlider);
        TextView timeLabel = findViewById(R.id.timeLabel);
        if (!isVisualShow){
            timeSeekBar.setMax(21);
            // For not visual show
            if (hour >= 20){
                minutes = 0;
            }
            else if (hour < 9){
                minutes = 0;
            }
            else {
                minutes -= 540;
            }
            timeSeekBar.setProgress(minutes/30);
        }
        // For Visual Show
        else {
            if (minutes <= 360){minutes = 360;}
            else if (minutes <= 480){minutes = 480;}
            else if (minutes <= 600){minutes = 600;}
            else if (hour > 19){minutes = 360;}
            timeSeekBar.setMax(2);
            timeSeekBar.setProgress((minutes-360)/120);
        }
        int d = minutes + 540;
        String e = String.format("%02d", d%60);
        timeLabel.setText(d/60+":"+e);

        timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!isVisualShow){
                    minutes = progress*30;
                }
                else {
                    minutes = 360 + (120*progress);
                }
                int d = minutes + 540;
                String e = String.format("%02d", d%60);
                timeLabel.setText(d/60+":"+e);
                Calculate();
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        ResultLabel = findViewById(R.id.detailsLabel);
        Calculate();
    }

    private void Calculate(){
        float finalPrice;
        int i;
        i = basePrice;
        if (!isVisualShow){
            if (day == 0 || day == 6){
                i += 5;
            }
        }
        finalPrice = people * i;
        String string;
        int g = minutes + 540;
        String e = String.format("%02d", g%60);
        String Start = g/60+":"+e;
        String End;
        if(!isVisualShow){
            int f = g+90;
            End = f/60+":"+String.format("%02d", f%60);
        }
        else { End = (g/60)+2+":"+e;}
        if (people >= 4){
            finalPrice = finalPrice*0.9f;
            string = "Exhibition: " + exhibition + "\nTime: " + daysOfTheWeek.get(day) + "\nStart Time: " + Start + "\nEnd Time: " + End + "\nPrice (10% Off!): " + currencyFormat.format(finalPrice);
        }
        else {
            string = "Exhibition: " + exhibition + "\nTime: " + daysOfTheWeek.get(day) + "\nStart Time: " + Start + "\nEnd Time: " + End + "\nPrice: " + currencyFormat.format(finalPrice);
        }

        Visitors.setText(""+people);
        ResultLabel.setText(string);
    }
}