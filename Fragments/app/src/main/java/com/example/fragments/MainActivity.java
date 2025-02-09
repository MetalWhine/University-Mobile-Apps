package com.example.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.Configuration;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Configuration configInfo = getResources().getConfiguration();
        if(configInfo.orientation == Configuration.ORIENTATION_LANDSCAPE){
            FragmentLandscape fragmentLandscape = new FragmentLandscape();
            fragmentTransaction.replace(android.R.id.content, fragmentLandscape );
//android.R.id.content - Activity's root view container

        }else {
            FragmentPortrait fragmentPortrait = new FragmentPortrait();
            fragmentTransaction.replace(android.R.id.content, fragmentPortrait);
        }
        fragmentTransaction.commit();
    }
}