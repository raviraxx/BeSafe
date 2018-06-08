package com.example.ravi.besafe;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

import java.util.ArrayList;
import java.util.List;

public class ContactsSettings extends AppCompatActivity {

    Button btn_add,btn_delete;

    DatabaseHelper databaseHelper;
    ListView lv_contacts;
    ArrayList<String> contactsList;
    ContactsListAdapter listAdapter;
    public static final int CONTACT_PICKER_REQUEST=500;
    public static final int SEND_SMS_REQUEST=510;
    SharedPreferences contactsPref;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_settings);
        contactsPref=getSharedPreferences("contactsPref",0);
        prefEditor=contactsPref.edit();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        databaseHelper=new DatabaseHelper(ContactsSettings.this);
        btn_add=findViewById(R.id.btn_addContact);

        lv_contacts=findViewById(R.id.list1);
        if(databaseHelper.readContacts().size()>0){
            contactsList=databaseHelper.readContacts();
            listAdapter=new ContactsListAdapter(ContactsSettings.this,contactsList);
            lv_contacts.setAdapter(listAdapter);
        }else{
            contactsList=new ArrayList<>();
        }

        SharedPreferences sharedPreferences=getSharedPreferences("prefs",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("firstTime",true);
        editor.apply();


        btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {



                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

                    new MultiContactPicker.Builder(ContactsSettings.this) //Activity/fragment context

                            .hideScrollbar(false) //Optional - default: false
                            .showTrack(true) //Optional - default: true
                            .searchIconColor(Color.WHITE) //Option - default: White
                            .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) //Optional - default: CHOICE_MODE_MULTIPLE
                            .handleColor(ContextCompat.getColor(ContactsSettings.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                            .bubbleColor(ContextCompat.getColor(ContactsSettings.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                            .bubbleTextColor(Color.WHITE) //Optional - default: White
                            .showPickerForResult(CONTACT_PICKER_REQUEST);


                }
                else{

                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

                            requestPermissions(new String[]{Manifest.permission.SEND_SMS},SEND_SMS_REQUEST);
                        }

                    }


                }




            }
        });



    }

    @Override
    public void onBackPressed() {

        if(databaseHelper.readContacts().size()>0){

            super.onBackPressed();

        }else{
            Toast.makeText(this, "Insert atleast one contact", Toast.LENGTH_SHORT).show();
        }
    }

    public void gotoNext(View view) {

        if(databaseHelper.readContacts().size()>0){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }else{
            Toast.makeText(this, "Insert atleast one contact", Toast.LENGTH_SHORT).show();
        }


    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CONTACT_PICKER_REQUEST){
            if(resultCode == RESULT_OK) {

                ArrayList<String> contactsList=new ArrayList<>();
                ArrayList<String> dbContacts=databaseHelper.readContacts();

                List<ContactResult> results = MultiContactPicker.obtainResult(data);
                // Log.d("MyTag", results.get(0).getDisplayName());

                for(ContactResult contactResult:results){

                    List<String> phonenos= contactResult.getPhoneNumbers();
                    String name=contactResult.getDisplayName();

                    if(dbContacts.contains(phonenos.get(0))){
                        Toast.makeText(this, "Contact "+name+" already exists", Toast.LENGTH_SHORT).show();
                        continue;
                    }
                    else{


                        if(databaseHelper.insertContact(phonenos.get(0))){
                            prefEditor.putString(phonenos.get(0),name).apply();
                            Toast.makeText(this, "Contact "+name+" added", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(this, "Contact "+name+" insert error", Toast.LENGTH_SHORT).show();
                        }



                    }



                }


                contactsList=databaseHelper.readContacts();
                listAdapter=new ContactsListAdapter(ContactsSettings.this,contactsList);
                lv_contacts.setAdapter(listAdapter);


            } else if(resultCode == RESULT_CANCELED){
                System.out.println("User closed the picker without selecting items.");
            }
        }

    }


}
