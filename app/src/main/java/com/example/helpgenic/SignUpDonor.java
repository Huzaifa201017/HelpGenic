/*------------------------------------------------Activity-----------------------------------------------*/
package com.example.helpgenic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.helpgenic.Doctor.DocPage;
import com.example.helpgenic.Doctor.SignUpDoc;

public class SignUpDonor extends AppCompatActivity {

    // Button submitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_donor);

        // No page exists for now .
//        submitButton = findViewById(R.id.submitButtonDonor);
//
//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(SignUpDonor.this, DocPage.class));
//            }
//        });
    }
}