package com.example.helpgenic.Patient;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.helpgenic.Classes.Appointment;
import com.example.helpgenic.Classes.BookingManager;
import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.Doctor;
import com.example.helpgenic.Classes.Patient;
import com.example.helpgenic.Classes.Slot;
import com.example.helpgenic.Classes.VirtualAppointmentSchedule;
import com.example.helpgenic.PatientAdapters.ListViewDsiplayingSlotsAdapter;
import com.example.helpgenic.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DisplayingSlots extends AppCompatActivity {


    ListView lst;
    Doctor d;
    VirtualAppointmentSchedule vs;
    Patient p;
    final Calendar myCalendar= Calendar.getInstance();
    Button dateBtn;
    ArrayList<Slot> slots;
    Date dateSelected;
    BookingManager bm = new BookingManager();
    int selectedPosition = -1;
    Slot selectedSlot = null;
    Button bookApp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaying_slots);

        lst = findViewById(R.id.slotsList);
        bookApp = findViewById(R.id.bookApp);


        // getting doctor , patient , and corresponding clicked vSchedule row's information.
        d = (Doctor) getIntent().getSerializableExtra("doc");
        p = Patient.getInstance();
        int vs_index =  getIntent().getIntExtra("vs_index", 0);
        vs = d.getvSchedule().get(vs_index);

        slots = bm.makeSlots(vs.getsTime(),vs.geteTime(),vs.getDay());  // all total possibilities


        // ====================== selecting date ==================================

        dateBtn = findViewById(R.id.date);

        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH,month);
            myCalendar.set(Calendar.DAY_OF_MONTH,day);

            selectedPosition = -1;
            selectedSlot = null;
            updateLabel();


            long millis = System.currentTimeMillis();
            java.sql.Date date1 = new java.sql.Date(millis);

            String dayWeekText = new SimpleDateFormat("EEEE").format(dateSelected);

            if( (Objects.equals(vs.getDay() , dayWeekText)) && (dateSelected.compareTo(date1) > 0 || Objects.equals(dateSelected.toString() , date1.toString() )) ){

                DbHandler db = new DbHandler();
                db.connectToDb(DisplayingSlots.this);
                bm.setDb(db);

                bm.getAvailableSlots(d.getId() , dateSelected, vs.getDay() , slots, DisplayingSlots.this ).addOnCompleteListener(
                        task -> {

                            if (task.isSuccessful()){
                                ArrayList<Slot> availableSlots = task.getResult();

                                ListViewDsiplayingSlotsAdapter adapter = new ListViewDsiplayingSlotsAdapter(DisplayingSlots.this,0,availableSlots);
                                lst.setAdapter(adapter);

                                lst.setOnItemClickListener((adapterView, view1, i, l) -> {


                                    if(selectedPosition != -1){
                                        lst.getChildAt(selectedPosition).setBackgroundColor(Color.TRANSPARENT);
                                    }

                                    try{
                                        selectedSlot = (Slot)adapterView.getItemAtPosition(i);
                                        lst.getChildAt(i).setBackgroundColor(Color.rgb(165,184,166));
                                        selectedPosition = i;
                                    }catch (Exception e){
                                        Toast.makeText(DisplayingSlots.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                });

                            }
                        }
                );


            }
            else{
                Toast.makeText(DisplayingSlots.this, "Invalid Date Selected", Toast.LENGTH_SHORT).show();
            }




        };


        dateBtn.setOnClickListener(view -> new DatePickerDialog(
                DisplayingSlots.this,
                date,
                myCalendar.get(Calendar.YEAR)
                ,myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show());

        // ===========================================================================



        // listen for book appointment button

        bookApp.setOnClickListener(view -> {

            if (selectedSlot == null){
                Toast.makeText(DisplayingSlots.this, "Please select a slot !", Toast.LENGTH_SHORT).show();
            }else {


                Doctor temp1 = new Doctor(d.getId(), d.getName(), d.getSpecialization(), vs);
                Patient temp2 = new Patient(p.getId(), p.getName());
                Appointment newApt = new Appointment(dateSelected, temp1, temp2, selectedSlot.sTime, selectedSlot.eTime);

                bm.confirmAppointment(newApt, DisplayingSlots.this).addOnCompleteListener(

                        task -> {

                            if (task.isSuccessful() && task.getResult()) {

//                            AlarmHandler ah = new AlarmHandler();
//                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//                            ah.setAlarm(appId , dateSelected , selectedSlot.sTime , DisplayingSlots.this , alarmManager);

                                finish();
                            }
                        }
                );

            }


        });

    }


    @SuppressLint("SimpleDateFormat")
    private void updateLabel(){

        String myFormat="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);

        Date t = myCalendar.getTime();

        String date = dateFormat.format(myCalendar.getTime());
        dateBtn.setText(date);


        dateSelected = new java.sql.Date(t.getTime());




    }



}