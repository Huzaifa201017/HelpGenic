package com.example.helpgenic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Doctor.SignUpDoc;
import com.example.helpgenic.Patient.SignUpPatient;

import java.sql.Connection;
import java.sql.DriverManager;

public class GetStarted extends AppCompatActivity {
    Button doc , donor , patient;
    //DbHandler conn;
     private Connection connection = null;
    String url="jdbc:mysql://sda.mysql.database.azure.com:3306/helpgenic?useSSL=true&loginTimeout=30";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        // =============== connect with db ===============
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        try {

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, "azureuser", "Muhammad167");
            //connection = DriverManager.getConnection(url);
            Toast.makeText(this,"Success", Toast.LENGTH_SHORT).show();


        } catch (Exception e) {

            Log.e(String.valueOf(1), "Error occured here 1!");
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        // ==================================================


        doc = findViewById(R.id.signUpDoc);
        donor = findViewById(R.id.signUpDonor);
        patient = findViewById(R.id.signUpPatient);

        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GetStarted.this, SignUpDoc.class));
            }
        });

        donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GetStarted.this, SignUpDonor.class));
            }
        });


        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GetStarted.this, SignUpPatient.class));
            }
        });
    }
}