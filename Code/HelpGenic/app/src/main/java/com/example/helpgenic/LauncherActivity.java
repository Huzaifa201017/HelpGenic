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


    private void moveToNextScreen(GuestUser obj){

        Intent intent;

        if (obj.getType() == 'P') {

            // if patient then take him to the patient page
            intent = new Intent(LauncherActivity.this, PatientPage.class);
            intent.putExtra("patient", obj);

        } else if (obj.getType() == 'D') {

            // if doctor then take him to the doctor page
            intent = new Intent(LauncherActivity.this, DocPage.class);
            intent.putExtra("doctor", obj);

        }else{
            // if admin then take him to the admin page
            intent = new Intent(LauncherActivity.this, AdminPage.class);
            intent.putExtra("admin", obj);
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
                GuestUser obj = task.getResult();

                if (obj != null) {

                    Log.d("Login", "User was already logged in: " + obj.getType());
                    moveToNextScreen(obj);

                }else{

                    Intent intent = new Intent(LauncherActivity.this, login.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

}
