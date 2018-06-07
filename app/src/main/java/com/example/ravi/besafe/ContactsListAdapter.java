package com.example.ravi.besafe;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ContactsListAdapter extends ArrayAdapter<String> {

    Activity context;
    List<String> contactsList;
    SharedPreferences sharedPreferences;

    public ContactsListAdapter(@NonNull Activity context, @NonNull List<String> objects) {
        super(context, R.layout.lv_contact_item, objects);
        this.context=context;
        contactsList=objects;
        sharedPreferences=context.getSharedPreferences("contactsPref",0);

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view=context.getLayoutInflater().inflate(R.layout.lv_contact_item,parent,false);
        TextView tv_contact;
        ImageView iv_delete;
        TextView tv_name;


        tv_name=view.findViewById(R.id.tv_display_contact);
        tv_contact=view.findViewById(R.id.tv_contact);
        iv_delete=view.findViewById(R.id.iv_delete);

        tv_contact.setText(contactsList.get(position));
        tv_name.setText(sharedPreferences.getString(contactsList.get(position),"No Name"));

        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseHelper databaseHelper=new DatabaseHelper(context);
                boolean result=databaseHelper.deleteContact(contactsList.get(position));
                if(result){
                    notifyDataSetChanged();
                    contactsList.remove(position);
                    Toast.makeText(context, context.getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, context.getString(R.string.delete_failure), Toast.LENGTH_SHORT).show();
                }

            }
        });


        return view;
    }
}
