/*------------------------------------------------Activity-----------------------------------------------*/
package com.example.helpgenic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.helpgenic.Doctor.DocPage;
import com.example.helpgenic.Doctor.SignUpDoc;
import com.example.helpgenic.Patient.PatientPage;
import com.example.helpgenic.Patient.SignUpPatient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // temporary functions
    public void goToSignUpDocPage(View view){
        Intent intent = new Intent(this , SignUpDoc.class);
        startActivity(intent);
    }

    public void goToLoginPage(View view){
        Intent intent = new Intent(this , login.class);
        startActivity(intent);
    }

    public void goToSignUpPatientPage(View view){
        Intent intent = new Intent(this , SignUpPatient.class);
        startActivity(intent);
    }

    public void goToPatientPage(View view){
        Intent intent = new Intent(this , PatientPage.class);
        startActivity(intent);
    }

    public void goToSignUpDonorPage(View view){
        Intent intent = new Intent(this , SignUpDonor.class);
        startActivity(intent);
    }

//    public void goToAdminDashboard(View view){
//        Intent intent = new Intent(this , Admin.class);
//        startActivity(intent);
//    }




    public void goToDocPage(View view){
        Intent intent = new Intent(this , DocPage.class);
        startActivity(intent);
    }

    public void goToGetStartedPage(View view){
        Intent intent = new Intent(this , GetStarted.class);
        startActivity(intent);
    }
}