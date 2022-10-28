/*------------------------------------------------Activity-----------------------------------------------*/

package com.example.helpgenic.Doctor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.helpgenic.R;

public class SignUpDoc extends AppCompatActivity {
    //    Spinner specialization;
//    String[] specialization_list={"Psychiatrist","Dentist", "Physiotherapist"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_doc);
//        specialization=findViewById(R.id.SpSpinner);
//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(SignUpDoc.this, android.R.layout.simple_spinner_item,specialization_list);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        specialization.setAdapter(adapter);
//        specialization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
    }

}