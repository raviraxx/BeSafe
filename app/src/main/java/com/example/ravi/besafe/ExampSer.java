package com.example.ravi.besafe;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by ravi on 07-06-2018.
 */

public class ExampSer extends Service {
    String number;
    boolean runService;
    DatabaseHelper databaseHelper;
//db se lena
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        databaseHelper=new DatabaseHelper(ExampSer.this);


            number=databaseHelper.readContacts().get(0);



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
        {
            // ActivityCompat.requestPermissions((Activity) getApplicationContext(),new String[]{Manifest.permission.CALL_PHONE},Request_Call);
        }
        else{

          //  Toast.makeText(this, "Calling Emergency Number", Toast.LENGTH_SHORT).show();
            String dial = "tel:"+number;
            Log.d("ExampleService","Service is running");
            Intent i=new Intent(Intent.ACTION_CALL, Uri.parse(dial));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);


            }


        return START_STICKY;
    }






}
