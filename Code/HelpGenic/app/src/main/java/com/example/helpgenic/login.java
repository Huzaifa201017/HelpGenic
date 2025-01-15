/*------------------------------------------------Activity-----------------------------------------------*/
package com.example.helpgenic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.helpgenic.Admin.AdminPage;
import com.example.helpgenic.Classes.AuthenticateUser;
import com.example.helpgenic.Classes.GuestUser;
import com.example.helpgenic.Doctor.DocPage;
import com.example.helpgenic.Patient.PatientPage;

public class login extends AppCompatActivity {

    private Button signUp, logIn;
    private final GuestUser usr = new GuestUser();
    EditText emailField, passwordField;
    private void moveToNextScreen(GuestUser obj){

        Intent intent;

        if (obj.getType() == 'P') {

            // if patient then take him to the patient page
            intent = new Intent(login.this, PatientPage.class);
            intent.putExtra("patient", obj);

        } else if (obj.getType() == 'D') {

            // if doctor then take him to the doctor page
            intent = new Intent(login.this, DocPage.class);
            intent.putExtra("doctor", obj);

        }else{
            // if admin then take him to the admin page
            intent = new Intent(login.this, AdminPage.class);
            intent.putExtra("admin", obj);
        }

        startActivity(intent);
        finish();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.loginEmail);
        passwordField = findViewById(R.id.loginPassword);
        signUp = findViewById(R.id.getStarted);
        logIn = findViewById(R.id.loginBtn);


        // When submit button clicked, Log the user in (now we know that he was not already logged in)
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AuthenticateUser au = new AuthenticateUser();

                usr.setAu(au);
                // Make the user login , if get verified
                usr.logIn(emailField, passwordField, login.this).addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        // if user verified
                        GuestUser obj = task.getResult();

                        if (obj != null) {

                            Log.d("Login", "User verified: " + obj.getType());
                            moveToNextScreen(obj);

                        }
                    } else {
                        Log.d("Login", "User not verified");
                    }
                });

            }
        });


        // if sign up  button pressed then take him to the sign up page
        signUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(login.this, GetStarted.class));
            }
        });



    }

}