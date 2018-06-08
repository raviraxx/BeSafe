package com.example.ravi.besafe;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;


/**
 * Created by ravi on 07-06-2018.
 */

public class ExampSer extends Service {
    String number;
//db se lena
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        number=new DatabaseHelper(ExampSer.this).readContacts().get(0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
        {
            // ActivityCompat.requestPermissions((Activity) getApplicationContext(),new String[]{Manifest.permission.CALL_PHONE},Request_Call);
        }
        else{
            String dial = "tel:"+number;
            Intent i=new Intent(Intent.ACTION_CALL, Uri.parse(dial));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        if (intent.getStringExtra("data") != null)
        {
            String str=intent.getStringExtra("data");//get data here sended from BroadcastReceiver
            Toast.makeText(this, ""+str, Toast.LENGTH_SHORT).show();
        }


        return START_STICKY;
    }
}
