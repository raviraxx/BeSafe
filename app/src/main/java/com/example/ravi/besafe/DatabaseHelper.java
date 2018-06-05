package com.example.ravi.besafe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="DB_CONTACTS.db";
    public static final String TABLE_NAME="CONTACTS";
    public static final String ID="ID";
    public static final String CONTACT_NO="CONTACT_NO";


    Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context=context;

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME+"( "+ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"+CONTACT_NO+" TEXT )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertContact(String number){

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(CONTACT_NO,number);
        long result=sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        if(result==-1)
            return false;

        return true;

    }

    public ArrayList<String> readContacts(){
        ArrayList<String> contacts=new ArrayList<>();

        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
       Cursor cursor= sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME,null);
       while (cursor.moveToNext()){

           contacts.add(cursor.getString(1));
       }

        return contacts;

    }

    public boolean deleteContact(String number){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();

        int result= sqLiteDatabase.delete(TABLE_NAME,CONTACT_NO+" = ?",new String[]{number} );

        if(result>0){
            return true;
        }

        return false;
    }
}
