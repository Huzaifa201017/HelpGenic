package com.example.helpgenic.Patient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.helpgenic.Classes.Appointment;
import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.Patient;
import com.example.helpgenic.PatientAdapters.ListViewPatientHistoryAdapter;
import com.example.helpgenic.R;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UpcomingAppointments extends Fragment {

    private ArrayList<com.example.helpgenic.Classes.Appointment> appointments=null;
    Button joinMeeting;
    DbHandler db = new DbHandler();
    int docId = 0;
    Patient p;
    ListView appointmentListRef;


    private void setUpData() {

        p = Patient.getInstance();
        db.connectToDb(getContext());

        try {

            db.getUpcommingAppointmentsForPatients(p.getId(),  getContext()).addOnCompleteListener(
                task -> {
                    if (task.isSuccessful()) {
                        appointments = task.getResult();

                        // set adapter to list view
                        ListViewPatientHistoryAdapter adapter = new ListViewPatientHistoryAdapter(getContext() , R.layout.list_cell_custom_design_for_patient_schedule , appointments);
                        appointmentListRef.setAdapter(adapter);

                    } else {
                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            );


        }
        catch (Exception e) {

            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming_appointments, container, false);
        appointmentListRef = view.findViewById(R.id.upcomingSchedule);
        joinMeeting = view.findViewById(R.id.joinMeeting);


        // populate the data
        setUpData();



       // on click take them to the next page to update/cancel appt.
        appointmentListRef.setOnItemClickListener( (adapterView, view1, i, l) -> {


            Appointment apt = (Appointment) adapterView.getItemAtPosition(i);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String dateString = apt.getAppDate() + " " + apt.getsTime().toString();


            // formatting the dateString to convert it into a Date
            Date date = null;
            try {
                date = sdf.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();

            // Setting the Calendar date and time to the given date and time
            calendar.setTime(date);
            long time = calendar.getTimeInMillis();
            long currentTime = System.currentTimeMillis();

            if(time > currentTime){
                Intent intent = new Intent(getContext(), PageForUpdateApps.class);
                intent.putExtra("appointment", apt);
                startActivity(intent);
            }else{
                Toast.makeText(getContext() ,"Your appointment has already been started!", Toast.LENGTH_SHORT).show();
            }


        });



        joinMeeting.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {

                Appointment apt = null;
                if(appointments.size() != 0) {
                    apt = appointments.get(0);
                    docId = 0;
                }


                // check whether the patient time has started or not
                if (apt != null && checkTime(apt.getAppDate() , apt.getsTime())) {

                    SharedPreferences shrd =  getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = shrd.edit();
                    // you have to show the leave comment page
                    myEdit.putBoolean("MeetingJoined" , true);
                    // you have to refresh the page as your appointment time might be over after
                    // meet leave , so need to update the list
                    myEdit.putBoolean("isNeedToUpdate" , true);
                    myEdit.apply();

                    // TODO: Get latest meeting id from database as token gets expired(if time permits)
                    startActivity(new Intent(getContext(), JoinMeeting.class));
                }else{
                    Toast.makeText(getContext(), "No appointment at this time !", Toast.LENGTH_SHORT).show();
                }


            }

        });


        return view;
    }

    private boolean checkTime(Date aptDate, Time sTime){


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String timeString = aptDate.toString() + " " + sTime.toString();



        //formatting the dateString to convert it into a Date
        java.util.Date date = null;
        try {
            date = sdf.parse(timeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        //Setting the Calendar date and time to the given date and time
        calendar.setTime(date);

        // setting alarm
        long time1 = calendar.getTimeInMillis();
        long time2 = time1 + (60000 *30);

        if(System.currentTimeMillis() >= time1 && System.currentTimeMillis() <= time2 ){
            return true;
        }

        return false;
    }


    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences shrd =  getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);

        if(shrd.getBoolean("MeetingJoined", false)){

            SharedPreferences.Editor myEdit = shrd.edit();
            myEdit.remove("MeetingJoined");
            myEdit.putInt("dId", docId);
            myEdit.apply();

            Intent intent = new Intent(getContext() , GiveFeedback.class);

            startActivity(intent);

        }else if(shrd.getBoolean("isNeedToUpdate" ,false)){


            SharedPreferences.Editor myEdit = shrd.edit();
            myEdit.remove("isNeedToUpdate");
            myEdit.apply();
            setUpData();
        }
        // Toast.makeText(getContext(), "Yeah Successful !", Toast.LENGTH_SHORT).show();
    }


}