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

import static com.example.ravi.besafe.App.Channel_id;

/**
 * Created by ravi on 07-06-2018.
 */

public class ExampSer extends Service {
    String number="7775999217";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
        {
            // ActivityCompat.requestPermissions((Activity) getApplicationContext(),new String[]{Manifest.permission.CALL_PHONE},Request_Call);
        }
        else{
            String dial = "tel:"+number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        if (intent.getStringExtra("data") != null)
        {
            String str=intent.getStringExtra("data");//get data here sended from BroadcastReceiver
            Toast.makeText(this, ""+str, Toast.LENGTH_SHORT).show();
        }

        Intent activityIntent=new Intent(this,MainActivity.class);
        PendingIntent contentIntent=PendingIntent.getActivity(this,0,activityIntent,0);


//        Intent stopSer=new Intent(this,MainActivity.class);
//        PendingIntent sservice=PendingIntent.getService()

        Notification notification=new NotificationCompat.Builder(this,Channel_id).setContentTitle("BeSafe Service Running")
                //.setContentText("Example")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentIntent(contentIntent)
//                .addAction(R.mipmap.ic_launcher,"Stop Service",sservice)
                .build();
        startForeground(1,notification);

        return START_STICKY;
    }
}
