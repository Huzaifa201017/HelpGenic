package com.example.helpgenic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.helpgenic.Admin.AdminPage;
import com.example.helpgenic.Classes.AuthenticateUser;
import com.example.helpgenic.Classes.GuestUser;
import com.example.helpgenic.Doctor.DocPage;
import com.example.helpgenic.Patient.PatientPage;

public class LauncherActivity extends AppCompatActivity {

    private final AuthenticateUser au = new AuthenticateUser();


    private void moveToNextScreen(Character userType){

        Intent intent;

        if (userType == 'P') {

            // if patient then take him to the patient page
            intent = new Intent(LauncherActivity.this, PatientPage.class);

        } else if (userType == 'D') {

            // if doctor then take him to the doctor page
            intent = new Intent(LauncherActivity.this, DocPage.class);

        }else{
            // if admin then take him to the admin page
            intent = new Intent(LauncherActivity.this, AdminPage.class);
        }

        startActivity(intent);
        finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher_activity);


        // Check if the user is already logged in
        au.checkIfAlreadyLoggedIn(this).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                Character userType = task.getResult();

                if (userType != null) {

                    Log.d("Login", "User was already logged in: " + userType);
                    moveToNextScreen(userType);

                }else{

                    Intent intent = new Intent(LauncherActivity.this, login.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

}
