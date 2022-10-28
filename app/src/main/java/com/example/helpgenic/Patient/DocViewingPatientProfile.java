package com.example.helpgenic.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.helpgenic.R;

public class DocViewingPatientProfile extends AppCompatActivity {

    String[] date = {"01-02-2012" , "05-12-2023" , "12-09-2015","13-10-2020","01-02-2012" , "05-12-2023" , "12-09-2015","13-10-2020"}; // sample dates
    ListView appointmentsListRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_viewing_patient_profile);

        appointmentsListRef = findViewById(R.id.appointments);
        ListViewAppointmentsAdapter adapter = new ListViewAppointmentsAdapter(this , R.layout.appointments_list_custom_design , date);
        appointmentsListRef.setAdapter(adapter);
    }



}