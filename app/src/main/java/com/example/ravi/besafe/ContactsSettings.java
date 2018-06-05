package com.example.ravi.besafe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsSettings extends AppCompatActivity {

    Button btn_add,btn_delete;
    EditText et_contact;
    TextView tv_contacts;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_settings);

        databaseHelper=new DatabaseHelper(ContactsSettings.this);
        btn_add=findViewById(R.id.btn_addContact);
        btn_delete=findViewById(R.id.btn_deleteContact);
        et_contact=findViewById(R.id.et_contact);
        tv_contacts=findViewById(R.id.tv_contacts);
        tv_contacts.setMovementMethod(new ScrollingMovementMethod());






        refreshTextView();
        btn_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String number=et_contact.getText().toString().trim();

                if(TextUtils.isEmpty(number)){
                    et_contact.setError(getString(R.string.empty_number));
                    et_contact.requestFocus();
                    return;

                    }

                    if(number.length()!=10){
                        et_contact.setError(getString(R.string.invalid_number));
                        et_contact.requestFocus();
                        return;
                    }

                    if(databaseHelper.insertContact(number)){
                        refreshTextView();
                        et_contact.setText("");
                        Toast.makeText(ContactsSettings.this, ""+getString(R.string.insert_success), Toast.LENGTH_SHORT).show();

                }
                    else{

                        Toast.makeText(ContactsSettings.this, ""+getString(R.string.insert_failure), Toast.LENGTH_SHORT).show();

                    }


            }
        });


        btn_delete.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View view) {

                String number=et_contact.getText().toString();

                if(TextUtils.isEmpty(number)){
                    et_contact.setError(getString(R.string.empty_number));
                    et_contact.requestFocus();
                    return;

                }

                if(number.length()!=10){
                    et_contact.setError(getString(R.string.invalid_number));
                    et_contact.requestFocus();
                    return;
                }


                if(databaseHelper.deleteContact(number)){
                    refreshTextView();
                    et_contact.setText("");
                    Toast.makeText(ContactsSettings.this, ""+getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                    }
                else{
                    Toast.makeText(ContactsSettings.this, ""+getString(R.string.delete_failure), Toast.LENGTH_SHORT).show();
                    }
            }
        });

    }

    void refreshTextView(){

        tv_contacts.setText("");
        ArrayList<String> contactsList= databaseHelper.readContacts();

        for(String contact:contactsList){
            tv_contacts.append(contact+"\n\n");
        }

    }
}
