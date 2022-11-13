package com.example.helpgenic.Patient;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.helpgenic.PatientAdapters.Appointment;
import com.example.helpgenic.PatientAdapters.ListViewPatientHistoryAdapter;
import com.example.helpgenic.R;

import java.util.ArrayList;

public class UpcomingAppointments extends Fragment {

    private final ArrayList<Appointment> appointments = new ArrayList<>();


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpcomingAppointments() {
        // Required empty public constructor
    }




    private void setUpData() {
        appointments.add(new Appointment("July 22" , "8:30-9:00 AM" , "Tuesday" , "Dr, Guljeet Tillo"));

        appointments.add(new Appointment("Sept 22" , "9:30-10:00 AM" , "Wednesday" , "Dr, Aslam Pardes"));

        appointments.add(new Appointment("Jan 22" , "11:30-12:00 AM" , "Saturday" , "Dr, Mahira Khan"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming_appointments, container, false);

        // populate the data
        setUpData();
        // set adapter to list view
        ListView appointmentListRef = view.findViewById(R.id.upcomingSchedule);
        ListViewPatientHistoryAdapter adapter = new ListViewPatientHistoryAdapter(getContext() , R.layout.list_cell_custom_design_for_patient_schedule , appointments);
        appointmentListRef.setAdapter(adapter);

        return view;
    }
}