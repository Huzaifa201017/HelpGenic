package com.example.helpgenic;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.VirtualAppointmentSchedule;

import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Locale;

public class AddVirtualSchedule extends AppCompatActivity {
    DbHandler dbHandler = new DbHandler();
    Spinner spinner;
    String[] Days = {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
    String selectedDay;
    Button startTime,endTime,submit;
    int hour,minutes,sec,hour2,minutes2;
    LocalTime t1,t2;
    String start,end;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sec=0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_virtual_schedule);
        spinner = findViewById(R.id.spinner);
        startTime = findViewById(R.id.start);
        endTime= findViewById(R.id.end);
        submit = findViewById(R.id.submit);
        t1=LocalTime.of(0,0);
        t2=LocalTime.of(0,0);

        //---------------------- CLick Listners for Clock ---------------------//

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddVirtualSchedule.this, new TimePickerDialog.OnTimeSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selHour, int selMin) {
                        hour =selHour;
                        minutes=selMin;
                        //initialize  calander
                        Calendar calendar = Calendar.getInstance();
                        //Set selected Time on button
                        startTime.setText(String.format(Locale.getDefault(),"%02d:%02d",hour,minutes));
                        t1=LocalTime.of(hour,minutes);
                    }
                },12,0,true);
                timePickerDialog.updateTime(hour,minutes);
                timePickerDialog.show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddVirtualSchedule.this, new TimePickerDialog.OnTimeSetListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selHour, int selMin) {
                        hour2 =selHour;
                        minutes2=selMin;
                        //initialize calender
                        Calendar calendar = Calendar.getInstance();
                        //Set selected Time on button
                        endTime.setText(String.format(Locale.getDefault(),"%02d:%02d",hour2,minutes2));
                        t2=LocalTime.of(hour2,minutes2);
                    }
                },12,0,true);
                timePickerDialog.updateTime(hour2,minutes2);
                timePickerDialog.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get time from both buttons in string
                start = startTime.getText().toString() + ":00";
                end = endTime.getText().toString() + ":00";

                //compare the two times
                int value=t1.compareTo(t2);
                System.out.println(start);
                System.out.println(end);
                Time time1 = Time.valueOf(start); // startTime
                Time time2 = Time.valueOf(end); // endTime


                //pass the doc id in the function insertVAppSchedule according the object of doc which is created after login
                int docId = getIntent().getIntExtra("docId",0);
                float fee = getIntent().getFloatExtra("fee",0.0f);

                VirtualAppointmentSchedule virtualAppointmentSchedule = new VirtualAppointmentSchedule(selectedDay,time1,time2,fee);

                if(returnToProfile(value)){

                    System.out.println(fee);
                    dbHandler.connectToDb(getApplicationContext());
                    dbHandler.insertVAppSchedule(AddVirtualSchedule.this,virtualAppointmentSchedule,docId);
                    try {
                        dbHandler.closeConnection();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sh.edit();
                    myEdit.putBoolean("vNeedToUpdate", true);
                    myEdit.apply();
                    finish();
                }
            }
        });

        //---------------------------------------------------------------------//
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, Days);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedDay = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getApplicationContext(), selectedDay, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public boolean returnToProfile(int value){
        //int value=t1.compareTo(t2);
        boolean eFound=false;
        if(value==0){
            startTime.setError("Start and End times cannot be Equal");
            eFound=true;
        }
        else if(value>0){
            startTime.setError("Invalid Start Time");
            System.out.println(value);
            eFound=true;
        }
        else{
            startTime.setError(null);
        }

        if(eFound)
            return false;
        else
            return true;
    }
}