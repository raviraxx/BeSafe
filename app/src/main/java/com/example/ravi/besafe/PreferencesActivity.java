package com.example.ravi.besafe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class PreferencesActivity extends AppCompatActivity {

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper=new DatabaseHelper(getApplicationContext());
       // setContentView(R.layout.activity_preferences);
//        SharedPreferences sharedPreferences;
//
//        sharedPreferences=getSharedPreferences("prefs",0);
//        boolean first=sharedPreferences.getBoolean("firstTime",false);
//        if(first==false)
//        {
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putBoolean("firstTime",true);
//            editor.commit();
//           // startActivity(new Intent(getApplicationContext(),OnBoardingWithPer.class));
//            finish();
//        }
//
//        else{
//          //  startActivity(new Intent(getApplicationContext(),ContactSaver.class));
//            finish();
//        }

        ArrayList<String> contactList=databaseHelper.readContacts();
        if(contactList.size()==0){
            startActivity(new Intent(getApplicationContext(),ContactsSettings.class));
        }
        else{

            //finish(); this activity would be finished
            Toast.makeText(this, "Go To Main Activity", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),ContactsSettings.class));
        }


    }
}
