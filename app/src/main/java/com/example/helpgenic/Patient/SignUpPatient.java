/*------------------------------------------------Activity-----------------------------------------------*/

package com.example.helpgenic.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.helpgenic.Doctor.DocPage;
import com.example.helpgenic.Doctor.SignUpDoc;
import com.example.helpgenic.R;

public class SignUpPatient extends AppCompatActivity {

    Button submitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_patient);

        submitBtn = findViewById(R.id.submitButtonPatient);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpPatient.this, PatientPage.class));
            }
        });
    }
}