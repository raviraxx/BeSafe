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
        //loadFragment(new LocationFragment(MainActivity.this));
       // ActivityManager manager=(ActivityManager)getSystemService(ACTIVITY_SERVICE);
//        SharedPreferences sharedPreferences=getSharedPreferences("firstTime",0);
//        boolean first = sharedPreferences.getBoolean("pehliBaar",false);
//        if (!first) {
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            final TapTargetSequence sequence = new TapTargetSequence(MainActivity.this)
//                    .targets(
////                            TapTarget.forView(findViewById(R.id.settings_change_number),"Contact Settings","Add new or Delete saved contacts!").id(1)
////                                    .dimColor(android.R.color.black)
////                                    .outerCircleColor(R.color.color_dark_material_bold)
////                                    .targetCircleColor(android.R.color.black)
////                                    .textColor(android.R.color.black),
//
//
//                            TapTarget.forView(findViewById(R.id.navigation_dashboard),"Videos","Watch videos for self defence practices").id(1)
//                                    .dimColor(android.R.color.black)
//                                    .outerCircleColor(R.color.color_dark_permissions)
//                                    .targetCircleColor(android.R.color.black)
//                                    .textColor(android.R.color.black),
////                            TapTarget.forView(findViewById(R.id.settings_change_number),"Contact Settings","Add new or Delete saved contacts!").id(1)
////                                    .dimColor(android.R.color.black)
////                                    .outerCircleColor(R.color.color_dark_material_bold)
////                                    .targetCircleColor(android.R.color.black)
////                                    .textColor(android.R.color.black)
//
//
//                            TapTarget.forView(findViewById(R.id.navigation_home),"Send Sms","After clicking Danger Button Location would be shared to contacts each time location gets changed until safe button is clicked")
//                                    .id(2)
//                                    .dimColor(android.R.color.black)
//                                    .outerCircleColor(R.color.color_dark_permissions)
//                                    .targetCircleColor(android.R.color.black)
//                                    .textColor(android.R.color.black)
//
//
//                    )
//
//                    .listener(new TapTargetSequence.Listener() {
//                        @Override
//                        public void onSequenceFinish() {
//                            Toast.makeText(getApplicationContext(), "You got it", Toast.LENGTH_SHORT).show();
//                        }
//
//                        @Override
//                        public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {
//                            if(lastTarget.id()==1)
//                            {
//                                //Toast.makeText(getApplicationContext(), "You clicked Home", Toast.LENGTH_SHORT).show();
//                            }
//                            if(lastTarget.id()==2)
//                            {
//                                //Toast.makeText(Main2Activity.this, "You clicked Dashboard", Toast.LENGTH_SHORT).show();
//                            }
//                            if(lastTarget.id()==-1)
//                            {
//                                //Toast.makeText(Main2Activity.this, "You clicked Notifications", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                        @Override
//                        public void onSequenceCanceled(TapTarget lastTarget) {
//
//                        }
//                    });
//            sequence.start();
//            editor.putBoolean("pehliBaar", true);
//            editor.commit();
//        }
        loadFragment(new LocationFragment(getApplicationContext(),MainActivity.this));




    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isRunning()) {
            Toast.makeText(this, "Stopping Service", Toast.LENGTH_SHORT).show();
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

//        if(id==R.id.settings_start_service)
//        {
//
//            return true;
//        }
        if(id==R.id.settings_change_number)
        {
            startActivity(new Intent(getApplicationContext(),ContactsSettings.class));

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onPrepareOptionsMenu(final Menu menu) {
//
//            menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem menuItem) {
//
//                    if (isRunning()) {
//                        menu.getItem(0).setTitle("Start Service");
//                        menu.getItem(0).setIcon(R.drawable.ic_play_circle_outline_black_24dp);
//                        stopService(new Intent(getApplication(),ExampSer.class));
//                    }
//                    else {
//                        menu.getItem(0).setTitle("Stop Service");
//                        menu.getItem(0).setIcon(R.drawable.ic_play_circle_filled_black_24dp);
//                        startService(new Intent(getApplicationContext(), ExampSer.class));
//                    }
//
//                    return false;
//                }
//            });
//
//        return super.onPrepareOptionsMenu(menu);
//    }
}

