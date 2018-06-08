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



        SharedPreferences sharedPreferences;

        sharedPreferences=getSharedPreferences("prefs",0);
        boolean first=sharedPreferences.getBoolean("firstTime",false);
        if(first==false)
        {

            startActivity(new Intent(getApplicationContext(),OnBoardingWithPer.class));
            finish();
        }

        else{

            if(new DatabaseHelper(PreferencesActivity.this).readContacts().size()>0){
                startActivity(new Intent(getApplicationContext(),MainActivity.class));

            }else {
                startActivity(new Intent(getApplicationContext(), ContactsSettings.class));

            }
            finish();
        }


    }
}
