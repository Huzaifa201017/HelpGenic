/*------------------------------------------------Fragment-----------------------------------------------*/
package com.example.helpgenic.Doctor;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.helpgenic.DoctorAdapters.ListViewPatientsAttendedAdapter;
import com.example.helpgenic.DoctorAdapters.PatientAppointment;
import com.example.helpgenic.R;

import java.util.ArrayList;


public class PatientsRemaining extends Fragment {

    private ArrayList<PatientAppointment> appointments = new ArrayList<>();
    public PatientsRemaining() {
        // Required empty public constructor
    }

    private void setUpData() {

        appointments.add(new PatientAppointment("July 22" , "8:30-9:00 AM" , "Tuesday" , "Iqbal Butt" , 12));

        appointments.add(new PatientAppointment("Sept 22" , "9:30-10:00 AM" , "Wednesday" , "Shabnam Pardes", 21));

        appointments.add(new PatientAppointment("Jan 22" , "11:30-12:00 AM" , "Saturday" , "Haleema Bukhari" , 32));

        appointments.add(new PatientAppointment("Jan 22" , "11:30-12:00 AM" , "Saturday" , "Saleem Chishti" , 32));

        appointments.add(new PatientAppointment("Jan 22" , "11:30-12:00 AM" , "Saturday" , "Razandu Akru" , 32));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patients_remaining, container, false);

        // populate the data
        setUpData();
        // set adapter to list view
        ListView appointmentListRef = view.findViewById(R.id.patientsRem);
        ListViewPatientsAttendedAdapter adapter = new ListViewPatientsAttendedAdapter(getContext() , R.layout.list_cell_custom_design_patients_attended_and_remaining , appointments);
        appointmentListRef.setAdapter(adapter);



        appointmentListRef.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PatientAppointment p = (PatientAppointment) adapterView.getItemAtPosition(i);
                Toast.makeText(getContext(), "Item: "+ p.patientName, Toast.LENGTH_SHORT).show();


                startActivity(new Intent(getContext(), DocViewingPatientProfile.class));
            }
        });


        return view;
    }
}