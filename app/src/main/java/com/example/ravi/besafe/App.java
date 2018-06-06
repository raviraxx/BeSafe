package com.example.ravi.besafe;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

/**
 * Created by ravi on 07-06-2018.
 */

public class App extends Application {
    public static final String Channel_id="exampleServiceChannel";


    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannel();
    }

    private void createNotificationChannel() {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel serviceChannel = new NotificationChannel(
                    Channel_id,"Example Service Channel", NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager=getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
