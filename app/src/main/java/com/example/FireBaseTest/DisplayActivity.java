package com.example.FireBaseTest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class DisplayActivity extends AppCompatActivity {
    private String TAG = "DisplayActivity";
    FireBaseCloudHelper fireBaseCloudHelper;

    Button back_button;
    Button search_button;
    ListView person_lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        //check internet connection
        //check for connection to database

        fireBaseCloudHelper = new FireBaseCloudHelper(DisplayActivity.this, TAG);


        //find buttons by id
        back_button = findViewById(R.id.back_b);
        search_button = findViewById(R.id.search_b);
        person_lv = findViewById(R.id.person_lv);

        //set back button functionality
        back_button.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        //set search button functionality
        search_button.setOnClickListener(v -> {

            //display information in a list view


            DatabaseReference userRef = fireBaseCloudHelper.mDatabase.child("users");
            ValueEventListener userListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    List<Person> everyone = new ArrayList<>();
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        //User Information
                        String firstName = userSnapshot.child("first_name").getValue(String.class);
                        String lastName = userSnapshot.child("last_name").getValue(String.class);
                        int age = userSnapshot.child("age").getValue(int.class);
                        String email = userSnapshot.child("email").getValue(String.class);
                        String phoneNumber = userSnapshot.child("phone_number").getValue(String.class);
                        //create Person object
                        Person person = new Person(firstName, lastName, age, phoneNumber, email);
                        everyone.add(person);
                    }

                    DataBaseHelper dataBaseHelper = new DataBaseHelper(DisplayActivity.this);
                    List<Person> localUsers = dataBaseHelper.getEveryone();   /*this way gets it from the SQL database*/
                    //update database from what is in local sql
                    for (Person person : localUsers) {
                        String name = person.getFirst_name() + " " + person.getLast_name();
                        fireBaseCloudHelper.write_user(person);
                    }
                    if(!fireBaseCloudHelper.isConnected){

                        Toast.makeText(DisplayActivity.this, "Got from local database", Toast.LENGTH_SHORT).show();
                        everyone = localUsers;
                    }


                    ArrayAdapter people_array_adapter = new ArrayAdapter<Person>(DisplayActivity.this, android.R.layout.simple_expandable_list_item_1, everyone);
                    person_lv.setAdapter(people_array_adapter);

                    if (everyone.size() == 0) {
                        Toast.makeText(DisplayActivity.this, "No users Registered yet!", Toast.LENGTH_SHORT).show();
                    }

                }

                //get users from cloud

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w(TAG, "loadPost:onCancelled", error.toException());
                }
            };
            userRef.addListenerForSingleValueEvent(userListener);


        });
    }

    // using normal java
    public boolean isNetworkAvailable(Context con) {
        try {
            ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                Log.d(TAG, "connected");

                Toast.makeText(con, "connected", Toast.LENGTH_SHORT).show();
                return true;
            }
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());

            Toast.makeText(con, "nope", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}