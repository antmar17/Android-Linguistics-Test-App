package com.example.synchronizationtestapp;

import androidx.appcompat.app.AppCompatActivity; import android.content.Intent; import android.os.Bundle; import android.view.View; import android.widget.Button; import android.widget.EditText; import android.widget.TextView; import android.widget.Toast; import java.lang.reflect.Array; import java.util.ArrayList; import java.util.Arrays;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button search_button = findViewById(R.id.search_b);
        search_button.setOnClickListener(v->{
            Intent intent= new Intent(this,DisplayActivity.class);
            startActivity(intent);
        });
    }

    public void on_register_click(View view) {
        //all the edit text boxes
        EditText fn_et = findViewById(R.id.first_name_et);
        EditText ln_et = findViewById(R.id.last_name_et);
        EditText age_et = findViewById(R.id.age_et);
        EditText pnum_et = findViewById(R.id.phone_number_et);
        EditText email_et = findViewById(R.id.email_et);
        EditText[] et_list = {fn_et, ln_et, age_et, pnum_et, email_et};
        String[] warnings = {"First Name", "Last Name", "Age", "Phone Number", "Email"};

        //check that all fields are filled out
        for (int i = 0; i < 5; i++) {
            EditText et = et_list[i];
            String warning = warnings[i];
            String input = et.getText().toString();
            if (input.matches("")) {
                Toast.makeText(MainActivity.this, "You did not enter in a " + warning, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //get text from text boxes
        String f_n=fn_et.getText().toString();
        String l_n=ln_et.getText().toString();
        int age=Integer.parseInt(age_et.getText().toString());
        String pnum=pnum_et.getText().toString();
        String email=email_et.getText().toString();

        Person person;
        //try to make a new person object
        try{
            person=new Person(f_n,l_n,age,pnum,email);
            Toast.makeText(MainActivity.this,person.toString(),Toast.LENGTH_SHORT).show();
        }
        catch(Exception e){
            Toast.makeText(MainActivity.this,"Error registering User",Toast.LENGTH_SHORT).show();
            person= new Person("Error","Error",-1,"-1","Error");
        }

        //put in data base
        DataBaseHelper dataBaseHelper = new DataBaseHelper(MainActivity.this);
        boolean success=dataBaseHelper.add_row(person);
        Toast.makeText(MainActivity.this,"Success = "+success,Toast.LENGTH_SHORT).show();



    }


}