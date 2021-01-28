package com.example.FireBaseTest;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.lang.Object;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogRecord;

import static java.lang.Thread.sleep;

public class FireBaseCloudHelper extends Application {

    public DatabaseReference mDatabase;
    public DatabaseReference connRef;
    private Context context;
    private String TAG;
    public boolean isConnected;

    public FireBaseCloudHelper(Context context, String TAG) {
        this.context = context;
        this.TAG = TAG;
        this.isConnected = false;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();
        getConnectionStateRef();
    }


    public void getConnectionStateRef() {
        // [START rtdb_listen_connected]
        connRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    isConnected = true;
                    Toast.makeText(context, "connected", Toast.LENGTH_SHORT).show();
                } else {
                    isConnected = false;
                    Toast.makeText(context, "not connected", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Listener was cancelled");
            }
        });
    }

    public void write_user(Person person) {
        String name = person.getFirst_name() + " " + person.getLast_name();
        mDatabase.child("users").child(name).setValue(person);

    }

    public List<Person> getAllUsers() throws InterruptedException {

        DatabaseReference userRef = mDatabase.child("users");
        ArrayList<Person> returnValue = new ArrayList<>();
        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    //User Information
                    String firstName = userSnapshot.child("first_name").getValue(String.class);
                    String lastName = userSnapshot.child("last_name").getValue(String.class);
                    int age = userSnapshot.child("age").getValue(int.class);
                    String email = userSnapshot.child("email").getValue(String.class);
                    String phoneNumber = userSnapshot.child("phone_number").getValue(String.class);
                    //create Person object
                    Person person = new Person(firstName, lastName, age, phoneNumber, email);
                    returnValue.add(person);
                    Log.w(TAG, person.toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        };


        DataBaseHelper dataBaseHelper = new DataBaseHelper(context);
        List<Person> localUsers = dataBaseHelper.getEveryone();   /*this way gets it from the SQL database*/
        //update database from what is in local sql
        for (Person person : localUsers) {
            String name = person.getFirst_name() + " " + person.getLast_name();
            write_user(person);
        }
        if (!isConnected) {

            Toast.makeText(context, "HAD TO GET FROM LOCAL DATABASE!", Toast.LENGTH_SHORT).show();
            return localUsers;
        }

        //creates a handler to wait until results arrive
        Toast.makeText(context, "Was able connect to cloud! SIZE OF LIST IS " + String.valueOf(returnValue.size()), Toast.LENGTH_SHORT).show();
        while (returnValue.isEmpty()) {
            Thread.sleep(1000);
        }

        return returnValue;
    }

}
