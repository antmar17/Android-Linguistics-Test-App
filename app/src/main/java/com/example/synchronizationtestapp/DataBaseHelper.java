package com.example.synchronizationtestapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "TEST.db";

    private static final String TEST_TABLE = "TEST_TABLE";

    private static final String COLUMN_ID = "COLUMN_ID";
    private static final String COLUMN_FIRST_NAME = "FIRST_NAME";
    private static final String COLUMN_LAST_NAME = "LAST_NAME";
    private static final String COLUMN_AGE = "AGE";
    private static final String COLUMN_PHONE_NUMBER = "PHONE_NUMBER";
    private static final String COLUMN_EMAIL = "EMAIL";


    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE IF NOT EXISTS "
                + TEST_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FIRST_NAME + " TEXT, " + COLUMN_LAST_NAME
                + " TEXT, " + COLUMN_AGE + " INTEGER, " + COLUMN_EMAIL + " TEXT, "
                + COLUMN_PHONE_NUMBER + " TEXT" + ")";
        db.execSQL(createTableStatement);

    }

    //called when version number of database changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //ehhhhh

    }

    public boolean add_row(Person person) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_FIRST_NAME, person.getFirst_name());
        cv.put(COLUMN_LAST_NAME, person.getLast_name());
        cv.put(COLUMN_AGE, person.getAge());
        cv.put(COLUMN_EMAIL, person.getEmail());
        cv.put(COLUMN_PHONE_NUMBER, person.getPhone_number());

        long insert = db.insert(TEST_TABLE, null, cv);
        if (insert == -1) {
            return false;
        }
        return true;
    }

    public List<Person> getEveryone() {
        List<Person> return_list = new ArrayList<>();
        String query_string = "SELECT * FROM " + TEST_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query_string,null);
        //if there are results
        if (cursor.moveToFirst()) {
            //loop through results and create a new object for each row
            do {
                //get all info and make new person object
                int id = cursor.getInt(0);
                String f_n = cursor.getString(1);
                String l_n = cursor.getString(2);
                int age = cursor.getInt(3);
                String email = cursor.getString(4);
                String p_n = cursor.getString(5);
                Person new_person = new Person(f_n, l_n, age, p_n,email );
                //add to retlist
                return_list.add(new_person);


            }while (cursor.moveToNext());



        }
        else{
        }
        cursor.close();
        db.close();
        return return_list;

    }
}
