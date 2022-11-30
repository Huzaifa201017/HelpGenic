package com.example.helpgenic.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.helpgenic.Classes.AccountHandler;
import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.GuestUser;

import com.example.helpgenic.R;


public class SignUpPatient extends AppCompatActivity {

    Button submitBtn;
    private DbHandler db;
    private GuestUser usr = new GuestUser();
    EditText nameField, emailField,phoneNumField, password1Field , password2Field, dobField, gender, bloodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_patient);

        submitBtn = findViewById(R.id.submitButtonPatient);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = new DbHandler();


                nameField = findViewById(R.id.patientName);
                emailField = findViewById(R.id.patientEmail);
                phoneNumField = findViewById(R.id.patientPhoneNumber);
                password1Field = findViewById(R.id.patientPassword);
                password2Field = findViewById(R.id.patientConfirmPassword);
                dobField = findViewById(R.id.patientDOB);
                bloodGroup = findViewById(R.id.patientBloodGroup);
                gender = findViewById(R.id.patientGender);

                AccountHandler ah =  new AccountHandler();
                usr.setAh(ah);

                GuestUser obj = usr.SignUpPatient(nameField,emailField,phoneNumField, password1Field,password2Field,gender,dobField, bloodGroup,SignUpPatient.this);


                if (obj != null) {

                    Intent intent = new Intent(SignUpPatient.this, PatientPage.class);
                    intent.putExtra("patient", obj);
                    startActivity(intent);
                    finish();


                }


            }


        });
    }
}
