package com.example.tipcalculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import java.text.NumberFormat;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(); //get the (local) currency number format
    private static final NumberFormat percentFormat = NumberFormat.getPercentInstance(); //get a format for displaying percentages

    private double billAmount = 0;
    private double percent = 0.15;
    private TextView amountTextView;
    private TextView percentTextView;
    private TextView tipTextView;
    private TextView totalTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get references to programmatically manipulated TextViews
        amountTextView = (TextView) findViewById(R.id.amountTextView);
        percentTextView = (TextView) findViewById(R.id.percentTextView);
        tipTextView = (TextView) findViewById(R.id.tipTextView);
        totalTextView = (TextView) findViewById(R.id.totalTextView);

        tipTextView.setText(currencyFormat.format(0));  //set text to 0
        totalTextView.setText(currencyFormat.format(0));    //set text to 0

        // set amountEditText's TextWatcher
        EditText amountEditText = (EditText) findViewById(R.id.amountEditText);
        amountEditText.addTextChangedListener(amountEditTextWatcher);

        // set percentSeekBar's OnSeekBarChangeListener
        SeekBar percentSeekBar = (SeekBar) findViewById(R.id.percentSeekBar);
        percentSeekBar.setOnSeekBarChangeListener(seekBarListener);
    }

    // calculate and display tip and total amounts
    private void calculate(){
        //format percent and display in percentTextView
        percentTextView.setText(percentFormat.format(percent));

        double tip = billAmount*percent;    //calculate tip
        double total = billAmount+tip;  //calculate total

        // display tip and total formatted as currency
        tipTextView.setText(currencyFormat.format(tip));
        totalTextView.setText(currencyFormat.format(total));

    }

    // anonymous inner class that implements interface OnSeekBarChangeListener
    // listener object for the seekBar's progress changed events
    private final SeekBar.OnSeekBarChangeListener seekBarListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    percent = progress/100.0;   //set percent based on progress
                    calculate();    //calculate and display tip and total
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            };


    // anonymous inner class that implements interface TextWatcher
    // listener object for the EditText's text-changed events
    private final TextWatcher amountEditTextWatcher =
            new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try{
                        billAmount = Double.parseDouble(s.toString())/100.0;    //get bill amount
                        amountTextView.setText(currencyFormat.format(billAmount));  //display amount formatted as currency value
                    }

                    catch(NumberFormatException e){
                        amountTextView.setText("");
                        billAmount = 0.0;
                    }

                    calculate();    //calculate and display tip and total

                }

                @Override
                public void afterTextChanged(Editable s) {}
            };


}
