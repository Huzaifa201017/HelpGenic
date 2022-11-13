package com.example.helpgenic.Patient;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.helpgenic.CommonAdapters.ListViewAppointmentDocsAdapter;
import com.example.helpgenic.R;

import java.util.ArrayList;

public class AppointmentDocsViewedByPatient extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_docs_viewed_by_patient);
        ListView listview = findViewById(R.id.listView1);

        ArrayList<String> doc = new ArrayList<>();
        doc.add("X-Ray.pdf");
        doc.add("Blood Tests.pdf");
        doc.add("Sugar Tests.pdf");

        ListViewAppointmentDocsAdapter adapter = new ListViewAppointmentDocsAdapter(this, R.layout.list_cell_custom_design_appointment_docs_1, doc);
        listview.setAdapter(adapter);

        ListView listview2 = findViewById(R.id.listView2);
        ArrayList<String> reports = new ArrayList<>();
        reports.add("Prescription1");
        reports.add("Prescription2");
        reports.add("Prescription3");
        ListViewAppointmentDocsAdapter adapter2 = new ListViewAppointmentDocsAdapter(this, R.layout.list_cell_custom_design_appointment_docs_2 , reports);
        listview2.setAdapter(adapter2);


    }
}