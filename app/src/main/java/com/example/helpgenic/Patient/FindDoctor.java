package com.example.helpgenic.Patient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.helpgenic.R;

import java.util.ArrayList;

public class FindDoctor extends AppCompatActivity {

    ArrayList<Doctor> docList = new ArrayList<Doctor>();
    String[] items = {"Rating" , "Fee" , "PatientsAttended" };

    ListView lstView;
    SearchView sView;
    AutoCompleteTextView tView;
    ArrayAdapter<String> adapter;

    private void setUpData(){
        String [] arr = {"MBBS" , "FCPS"};
        docList.add(new Doctor("Dr. Javed Butt" , arr, "Pediatrics" , (float) 4.6));

        String [] arr1 = {"MBBS" , "FCPS"};
        docList.add(new Doctor("Dr. Iqbal Changezi" , arr1, "Cardiologist" , (float) 4.5));

        String [] arr2 = {"MBBS"};
        docList.add(new Doctor("Dr. Iqbal Changezi" , arr2, "ENT Specialist" , (float) 4.5));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor);

        sView = findViewById(R.id.searchView);
        tView = findViewById(R.id.searchOptions);
        adapter = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , items);
        tView.setAdapter(adapter);


        sView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sView.setIconified(false);
            }
        });

        tView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(FindDoctor.this, "Item: "+ s, Toast.LENGTH_SHORT).show();
            }
        });


        // populate the data
        setUpData();
        // set adapter to list view
        lstView = findViewById(R.id.DocList);
        customListViewAdapter adapter = new customListViewAdapter(this , R.layout.list_cell_custom_design , docList);
        lstView.setAdapter(adapter);
    }
}