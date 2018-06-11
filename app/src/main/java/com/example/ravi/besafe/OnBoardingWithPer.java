package com.example.ravi.besafe;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.app.OnNavigationBlockedListener;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.heinrichreimersoftware.materialintro.slide.Slide;

import java.util.ArrayList;

public class OnBoardingWithPer extends IntroActivity {
    DatabaseHelper databaseHelper;
    public static final String EXTRA_FULLSCREEN = "com.heinrichreimersoftware.materialintro.demo.EXTRA_FULLSCREEN";
    public static final String EXTRA_PERMISSIONS = "com.heinrichreimersoftware.materialintro.demo.EXTRA_PERMISSIONS";
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        boolean fullscreen = intent.getBooleanExtra(EXTRA_FULLSCREEN, false);
        boolean permissions = intent.getBooleanExtra(EXTRA_PERMISSIONS, true);
        databaseHelper=new DatabaseHelper(getApplicationContext());
        setButtonBackVisible(false);
        setButtonNextVisible(false);
        setButtonCtaVisible(false);
        setButtonCtaTintMode(BUTTON_CTA_TINT_MODE_TEXT);
        setFullscreen(fullscreen);
        setButtonCtaTintMode(BUTTON_CTA_TINT_MODE_TEXT);

        //Slide 1
        addSlide(new SimpleSlide.Builder()
                .title(R.string.title1)
                .description(R.string.description_1)
                .image(R.drawable.red_protection_shield)
                .background(R.color.color_dark_material_bold)
                .backgroundDark(R.color.color_primary_dark)
                .scrollable(false)
                .build());

      //Slide 1
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            addSlide(new SimpleSlide.Builder()
                    .title(R.string.title9)
                    .image(R.drawable.v_logo)
                    .description(R.string.description_9)
                    .background(R.color.color_permissions)
                    .backgroundDark(R.color.color_primary_dark)
                    .scrollable(false)
                    .build());
        }


        //Slide 2
        addSlide(new SimpleSlide.Builder()
                .title(R.string.title2)
                .description(R.string.description_2)
                .image(R.drawable.small_youtube)
                .background(R.color.color_permissions)
                .backgroundDark(R.color.color_dark_permissions)
                .scrollable(false)
                .build());


        //sms Slide
        addSlide(new SimpleSlide.Builder()
                .title(R.string.title10)
                .description(R.string.description_10)
                .background(R.color.color_permissions)
                .image(R.drawable.danger_btn)
                .backgroundDark(R.color.color_dark_permissions)
                .scrollable(false)
                .build());

        //Permission Slide
        final Slide permissionsSlide;
        if (permissions) {
            permissionsSlide = new SimpleSlide.Builder()
                    .title(R.string.title3)
                    .description(R.string.description_3)
                    .background(R.color.btnNotPressed)
                    .backgroundDark(R.color.btn)
                    .scrollable(false)
                    .permissions(new String[]{Manifest.permission.CALL_PHONE,
                            Manifest.permission.SEND_SMS,Manifest.permission.READ_CONTACTS,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_NETWORK_STATE})

                    .build();
            addSlide(permissionsSlide);
        } else {
            permissionsSlide = null;
        }

        //Slide 4
        addSlide(new SimpleSlide.Builder()
                .title(R.string.title5)
                .description(R.string.des)
                //.image(R.drawable.besafe_logo_colour)
                .background(R.color.color_dark_material_metaphor)
                .backgroundDark(R.color.colorPrimaryDark)
                .scrollable(false)
                .canGoForward(false)
                .buttonCtaLabel(R.string.go)
                .buttonCtaClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), "All Set to Go", Toast.LENGTH_SHORT).show();


                        ArrayList<String> contactList=databaseHelper.readContacts();
                        if(contactList.size()==0){
                            startActivity(new Intent(getApplicationContext(),ContactsSettings.class));
                            finish();
                        }
                        else{

                            //finish(); this activity would be finished
                            //Toast.makeText(this, "Go To Main Activity", Toast.LENGTH_SHORT).show();

                           startActivity(new Intent(getApplicationContext(),MainActivity.class));
                           finish();
                        }


                    }
                })
                .build());

        addOnNavigationBlockedListener(new OnNavigationBlockedListener() {
            @Override
            public void onNavigationBlocked(int position, int direction) {
                View contentView = findViewById(android.R.id.content);
                if (contentView != null) {
                    Slide slide = getSlide(position);

                    if (slide == permissionsSlide) {
                        Snackbar.make(contentView, R.string.label_grant_permissions, Snackbar.LENGTH_LONG)
                                .show();
                    }
                }
            }
        });
    }
}
