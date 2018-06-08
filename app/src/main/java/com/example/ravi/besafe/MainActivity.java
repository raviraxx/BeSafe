package com.example.ravi.besafe;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    //Todo Add Volume Rockers description in OnBoarding
    //Todo Add Network Check in Videos fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        loadFragment(new LocationFragment(getApplicationContext(),MainActivity.this));



    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isRunning()) {

            stopService(new Intent(getApplicationContext(), ExampSer.class));
        }
    }

    private boolean loadFragment(Fragment fragment){
        if(fragment!=null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();

            return true;
        }

        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment=null;

        switch (item.getItemId())
        {
            case R.id.navigation_home: fragment=new LocationFragment(MainActivity.this,MainActivity.this);break;
            case R.id.navigation_dashboard: fragment=new VideoFragment();break;
        }

        return loadFragment(fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settingss,menu);
        return true;
    }

    public boolean isRunning()
    {
        ActivityManager manager=(ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo serviceInfo:manager.getRunningServices(Integer.MAX_VALUE))
        {
            if("com.example.ravi.besafe.ExampSer".equals(serviceInfo.service.getClassName()))
            {

                return true;
            }

        }
        return false;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();


        if(id==R.id.settings_change_number)
        {
            startActivity(new Intent(getApplicationContext(),ContactsSettings.class));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

