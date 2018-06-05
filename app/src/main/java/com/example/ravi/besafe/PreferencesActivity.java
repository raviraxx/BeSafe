package com.example.ravi.besafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class PreferencesActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


       // setContentView(R.layout.activity_preferences);
        SharedPreferences sharedPreferences;

        sharedPreferences=getSharedPreferences("prefs",0);
        boolean first=sharedPreferences.getBoolean("firstTime",false);
        if(first==false)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstTime",true);
            editor.commit();
            startActivity(new Intent(getApplicationContext(),OnBoardingWithPer.class));
            finish();
        }

        else{
           startActivity(new Intent(getApplicationContext(),ContactsSettings.class));
            finish();
        }


    }
}
