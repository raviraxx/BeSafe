package com.example.ravi.besafe;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;



import static android.app.Activity.RESULT_OK;

/**
 * Created by ravi on 06-06-2018.
 */

public class LocationFragment extends Fragment {

    //line 251      onclick lf java 129     sendsms lf 251

    Context context;
    LocationManager locationManager;
    LocationListener locationListener;
    Double latitude = 0.0;
    Double longitude = 0.0;

    long oldTime=0;
    boolean dialogFlag=false;


    public static final int PERMISSION_REQUEST = 1000;
    public static final int GPS_REQUEST = 2000;
    boolean inDanger = false;
    public Geocoder geocoder;
    public List<Address> addresses;
    public SmsManager smsManager;
    public DatabaseHelper databaseHelper;
    String coordinates;
    Button btn_alert;
    TextView tv_location;
    boolean runThread=false;
    Activity activity;


    public LocationFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.location_fragment, null);
        context=container.getContext();
        activity= (Activity) context;



        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        btn_alert=view.findViewById(R.id.btn_alert);
        tv_location=view.findViewById(R.id.tv_location);
        databaseHelper=new DatabaseHelper(context);
        smsManager = SmsManager.getDefault();

    }

    @Override
    public void onResume() {
        super.onResume();
        runThread=true;
        Thread locationThread=new Thread(new locationRunnable());
        locationThread.start();



        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);


        handleLocationUpdates();

        btn_alert.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if((latitude>0.0 && longitude>0.0) || true){

                    inDanger=!inDanger;

                    if(inDanger){

                        btn_alert.setText(getString(R.string.btn_safeText));
                        btn_alert.setBackgroundResource(R.drawable.safe_btn);

                        sendSMS();


                    }else{
                        btn_alert.setText(getString(R.string.btn_dangerText));
                        String message=getString(R.string.btn_safeText)+"\n"+tv_location.getText().toString();
                        btn_alert.setBackgroundResource(R.drawable.danger_btn);
                        sendSafeSMS(message);

                    }

                    Toast.makeText(context, ""+getString(R.string.toast_message), Toast.LENGTH_SHORT).show();

                }

                }//end of onClick
        });

    }

    private void handleLocationUpdates() {


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST);
            return;
        }
        Location tempLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (tempLocation != null) {
            latitude = tempLocation.getLatitude();
            longitude = tempLocation.getLongitude();
            printaddress(latitude, longitude,"My Last Known Location");
        }


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                printaddress(latitude, longitude,"");

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    activity.startActivityForResult(intent, GPS_REQUEST);
                }

            }
        };


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);




    }

    public void printaddress(double latitude, double longitude,String text){
        try {
            geocoder=new Geocoder(context, Locale.getDefault());

            if(latitude>0.0 && longitude>0.0) {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String address = addresses.get(0).getAddressLine(0);

                if(text.equalsIgnoreCase("My Last Known Location"))
                    tv_location.setText(text+"\n"+address);
                else
                    tv_location.setText(address);

                coordinates = "\n\nLatitude-> " + latitude + "\nLongitude->" + longitude;
                tv_location.append(coordinates);
            }
            if(inDanger){


                sendSMS();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void sendSMS(){


        long newTime= Calendar.getInstance().getTimeInMillis();
        if(newTime-oldTime >= 3000) {

            oldTime=newTime;

            String message=getString(R.string.btn_dangerText)+"\n"+tv_location.getText().toString();

            List<String> contactsList = databaseHelper.readContacts();

            if (contactsList.size() > 0) {


                for (String contact : contactsList) {


                    smsManager.sendTextMessage(contact, null, message, null, null);
                }



            }
        }


    }


    public void sendSafeSMS(String message){


        List<String> contactsList = databaseHelper.readContacts();

            if (contactsList.size() > 0) {


                for (String contact : contactsList) {


                    smsManager.sendTextMessage(contact, null, message, null, null);
                }



            }



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST);
                return;
            }
            Location tempLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (tempLocation != null) {
                latitude = tempLocation.getLatitude();
                longitude = tempLocation.getLongitude();
                printaddress(latitude, longitude,"My last known Location");
            }


        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GPS_REQUEST && resultCode == RESULT_OK) {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST);
                return;
            }
            Location tempLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(tempLocation!=null){
                latitude = tempLocation.getLatitude();
                longitude = tempLocation.getLongitude();
                printaddress(latitude, longitude,"My last known location");
            }


        }

        if(requestCode==50){
            runThread=true;
            if(resultCode==RESULT_OK){
                handleLocationUpdates();
            }
        }
    }

    void createDialog(){

        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));

        dialog.setPositiveButton(context.getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                // TODO Auto-generated method stub
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivityForResult(myIntent,50);

            }
        });
        dialog.show();



    }


    Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {


            if(dialogFlag) {

                switch (message.what) {

                    case 10:


                        createDialog();
                        return true;


                }

            }




            return false;
        }
    });

    class locationRunnable implements Runnable{
        @Override
        public synchronized void run() {

            while (runThread) {


                if (Build.VERSION.SDK_INT > 22) {

                    LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                    boolean gps_enabled = false;
                    boolean network_enabled = false;

                    try {
                        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    }
                    catch (Exception ex) {
                    }

                    try {
                        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    } catch (Exception ex) {
                    }

                    if (!gps_enabled && !network_enabled) {

                        Message message=Message.obtain();
                        message.what=10;
                        handler.sendMessage(message);
                        runThread=false;
                    }

                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        dialogFlag=false;
    }

    @Override
    public void onStart() {
        super.onStart();
        dialogFlag=true;

    }
}
