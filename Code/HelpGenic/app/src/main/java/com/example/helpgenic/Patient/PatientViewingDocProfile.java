package com.example.helpgenic.Patient;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.helpgenic.Classes.Appointment;
import com.example.helpgenic.Classes.BookingManager;
import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.Doctor;
import com.example.helpgenic.Classes.Patient;
import com.example.helpgenic.Classes.PhysicalAppointmentSchedule;
import com.example.helpgenic.Classes.ReportsHandler;
import com.example.helpgenic.Classes.VirtualAppointmentSchedule;
import com.example.helpgenic.CommonAdapters.ListViewVirtualScheduleDisplayAdapter;
import com.example.helpgenic.DoctorAdapters.MyExpandableListAdapter;
import com.example.helpgenic.R;

import org.checkerframework.checker.units.qual.A;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PatientViewingDocProfile extends AppCompatActivity {


    ListView appointments;
    ListView virtualapps;

    TextView fee , docName , docEmail ,docSpeciality;
    Doctor d;
    Patient p;

    List<String> groupList,childList;
    Map<String, List<String>> map;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    DbHandler db = new DbHandler();
    ArrayList<String[]> clinicInfo =new ArrayList<>();
    ArrayList<String> allClinicNames= new ArrayList<>();
    ReportsHandler rh = new ReportsHandler();


    private void setUpData(){

        d = (Doctor) getIntent().getSerializableExtra("doctor");
        p = Patient.getInstance();


        docName = findViewById(R.id.docName);
        docName.setText(String.format("Dr. %s", d.getName()));

        docEmail = findViewById(R.id.docEmail);
        docEmail.setText(d.getMail());

        docSpeciality = findViewById(R.id.docSpeciality);
        docSpeciality.setText(d.getSpecialization());

        fee = findViewById(R.id.fee);
        fee.setText(String.format(Locale.getDefault(), "FEE: %d /-", d.getFee()));



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_viewing_doc_profile);

        appointments=findViewById(R.id.prior_appointments);
        expandableListView= findViewById(R.id.psExpandable);


        setUpData();

        db.connectToDb(this);


        db.setDoctorAptScheduleDetails(d).addOnCompleteListener(
                task -> {
                    if (!task.isSuccessful() || (!task.getResult())) {
                        Log.d("PatientViewingDocProfile", "Fetch Document Task Failed");
                        d.setVSch(new ArrayList<>());
                        d.setPSchedule(new ArrayList<>());
                    }

                    addClinicNames();
                    setUpDataForEachClinic();

                    // setting up the list
                    virtualapps=findViewById(R.id.virtual_app_list);
                    ListViewVirtualScheduleDisplayAdapter adapter2 = new ListViewVirtualScheduleDisplayAdapter(this,R.layout.list_cell_custom_design_patient_views_doc_prof,d.getvSchedule());
                    virtualapps.setAdapter(adapter2);

                    // listen to click on virtual schedule row
                    virtualapps.setOnItemClickListener((adapterView, view, i, l) -> {

                        Intent intent = new Intent(PatientViewingDocProfile.this, DisplayingSlots.class);

                        intent.putExtra("vs_index" ,  i);
                        intent.putExtra("doc" , d);

                        startActivity(intent);
                    });







                    // populating pSchedule List
                    expandableListAdapter = new MyExpandableListAdapter(PatientViewingDocProfile.this,groupList,map, d.getPSchedule());
                    expandableListView.setAdapter(expandableListAdapter);
                    expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                        int lastExpandedPosition=-1;
                        @Override
                        public void onGroupExpand(int i) {
                            if(lastExpandedPosition != -1 && i!= lastExpandedPosition){
                                expandableListView.collapseGroup(lastExpandedPosition);
                            }
                            lastExpandedPosition=i;

                        }
                    });

                }
        );
        Toast.makeText(this, d.getName(), Toast.LENGTH_SHORT).show();





////        rh.setDb(db);
//        rh.displayPreviousAppointments(0 , 0 , PatientViewingDocProfile.this, appointments);
//
//        appointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//
//                Appointment apt = (Appointment)adapterView.getItemAtPosition(i);
//                Intent intent = new Intent(PatientViewingDocProfile.this, AppointmentDocsViewedByPatient.class);
//                intent.putExtra("aptId" , apt.getAppId());
//                startActivity(intent);
//            }
//        });




            // ===================================================================================================

    }

    //function to set data for physical schedule
    public void setUpDataForEachClinic(){

        try {
            for(PhysicalAppointmentSchedule p: d.getPSchedule()){

                String day = p.getDay();
                Time sTime = p.getsTime();
                Time eTime = p.geteTime();
                String AssPhone = p.getAssistantPhNum();
                String fullSchedule = "  Time :      " + sTime.toString() + "  to  " + eTime.toString() + "  -  " + day;
                String[] allinOne={fullSchedule, "  Assistant Contact :      "+AssPhone};
                clinicInfo.add(allinOne);
            }

            map = new HashMap<>();

            int i =0;

            for(String group: groupList){
                if(group.equals(allClinicNames.get(i)))
                    loadChild(clinicInfo.get(i));
                map.put(group,childList);
                i++;
            }

        }
        catch (Exception e){
            Toast.makeText(PatientViewingDocProfile.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void loadChild(String[] info) {
        childList = new ArrayList<>();
        Collections.addAll(childList, info);
    }


    public void addClinicNames(){

        try {

            groupList=new ArrayList<>();

            for (PhysicalAppointmentSchedule p: d.getPSchedule()){
                groupList.add(p.getClinicName());
                allClinicNames.add(p.getClinicName());
            }

        }catch (Exception e){
            Toast.makeText(PatientViewingDocProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


}