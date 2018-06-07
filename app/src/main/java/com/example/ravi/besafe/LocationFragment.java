package com.example.ravi.besafe;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ravi on 06-06-2018.
 */

public class LocationFragment extends Fragment {


    Context context;
    LocationManager locationManager;
    LocationListener locationListener;
    Double latitude = 0.0;
    Double longitude = 0.0;
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


    public LocationFragment() {
    }

    public LocationFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.location_fragment, null);


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


        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.SEND_SMS}, PERMISSION_REQUEST);
            return;
        }
        Location tempLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (tempLocation != null) {
            latitude = tempLocation.getLatitude();
            longitude = tempLocation.getLongitude();
            printaddress(latitude, longitude);
        }


        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                if(inDanger)
                printaddress(latitude, longitude);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, GPS_REQUEST);

            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);


        btn_alert.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(latitude>0.0 && longitude>0.0){

                    inDanger=!inDanger;

                    if(inDanger){

                        btn_alert.setText(getString(R.string.btn_safeText));
                        printaddress(latitude,longitude);


                    }else{
                        btn_alert.setText(getString(R.string.btn_dangerText));
                        String message=getString(R.string.btn_safeText)+tv_location.getText().toString();
                        sendSMS(message);

                    }

                    Toast.makeText(context, ""+getString(R.string.toast_message), Toast.LENGTH_SHORT).show();

                }

                }//end of onClick
        });

    }

    public void printaddress(double latitude, double longitude){
        try {
            geocoder=new Geocoder(context, Locale.getDefault());
            addresses=geocoder.getFromLocation(latitude,longitude,1);
            String address=addresses.get(0).getAddressLine(0);

            tv_location.setText(address);
            coordinates = "\n\nLatitude-> " + latitude + "\nLongitude->" + longitude;
            tv_location.append(coordinates);
            if(inDanger){

                String message=getString(R.string.btn_dangerText)+tv_location.getText().toString();
                //smsManager.sendTextMessage("8291565088",null,message,null,null);
                sendSMS(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

//    public void requestLocationUpdates(){
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
//    }

    public void sendSMS(String message){

        //Cursor cursor=databaseHelper.viewData();
        List<String> contactsList=databaseHelper.readContacts();

        if(contactsList.size()>0){

            //StringBuffer stringBuffer=new StringBuffer();
            for(String contact : contactsList){

                //String contact=cursor.getString(1);
                smsManager.sendTextMessage(contact,null, message,null,null);
            }
            //Toast.makeText(this, "Location Shared", Toast.LENGTH_SHORT).show();



        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.SEND_SMS}, PERMISSION_REQUEST);
                return;
            }
            Location tempLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (tempLocation != null) {
                latitude = tempLocation.getLatitude();
                longitude = tempLocation.getLongitude();
                printaddress(latitude, longitude);
            }


        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GPS_REQUEST && resultCode == RESULT_OK) {

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.SEND_SMS}, PERMISSION_REQUEST);
                return;
            }
            Location tempLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(tempLocation!=null){
                latitude = tempLocation.getLatitude();
                longitude = tempLocation.getLongitude();
                printaddress(latitude, longitude);
            }


        }
    }
}
