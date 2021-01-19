package com.example.synchronizationtestapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class DisplayActivity extends AppCompatActivity {
    Button back_button;
    Button search_button;
    ListView person_lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        back_button = findViewById(R.id.back_b);
        search_button = findViewById(R.id.search_b);
        person_lv = findViewById(R.id.person_lv);

        //set back button functionality
        back_button.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        //set search button functionality
        search_button.setOnClickListener(v->{
            //display information in a list view
            DataBaseHelper dataBaseHelper = new DataBaseHelper(DisplayActivity.this);
            List<Person> everyone = dataBaseHelper.getEveryone();

            ArrayAdapter people_array_adapter = new ArrayAdapter<Person>(DisplayActivity.this,android.R.layout.simple_expandable_list_item_1,everyone);
            person_lv.setAdapter(people_array_adapter);
            if(everyone.size() == 0){
                Toast.makeText(DisplayActivity.this,"No users Registered yet!",Toast.LENGTH_SHORT).show();
            }


        });

    }




}