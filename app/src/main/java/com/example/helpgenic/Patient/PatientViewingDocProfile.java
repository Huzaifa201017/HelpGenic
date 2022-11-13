package com.example.helpgenic.Patient;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.helpgenic.CommonAdapters.ExpandableListViewAdapter;
import com.example.helpgenic.CommonAdapters.ListViewAppointmentsAdapter;
import com.example.helpgenic.CommonAdapters.ListViewVirtualScheduleDisplayAdapter;
import com.example.helpgenic.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientViewingDocProfile extends AppCompatActivity {

    String[] arr={"Wed, 29/10/2022","Wed, 29/10/2022","Wed, 29/10/2022","Wed, 29/10/2022",
            "WebOS","Wed, 29/10/2022","Wed, 29/10/2022","Wed, 29/10/2022","Wed, 29/10/2022","Wed, 29/10/2022","Wed, 29/10/2022","Wed, 29/10/2022",
            "WebOS","Ubuntu","Windows7","Max OS X"};

    String[] arr2={"Mon: 9am - 10am","Tue: 8pm - 10pm","Wed: 8pm - 10pm","Thurs: 8pm - 10pm","Fri: 8pm - 10pm",};

    ListView appointments;
    ListView virtualapps;
    List<String> grouplist;
    List<String> childlist;
    Map<String,List<String>> physicalSchedule;
    ExpandableListView expandablelistview;
    ExpandableListAdapter expandableListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_viewing_doc_profile);
        appointments=findViewById(R.id.prior_appointments);
        ListViewAppointmentsAdapter adapter = new ListViewAppointmentsAdapter(this,R.layout.appointments_list_custom_design,arr);
        appointments.setAdapter(adapter);


        // Listening to click on appointments row
        appointments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getApplicationContext(), AppointmentDocsViewedByPatient.class));
            }
        });


        virtualapps=findViewById(R.id.virtual_app_list);
        ListViewVirtualScheduleDisplayAdapter adapter2 = new ListViewVirtualScheduleDisplayAdapter(this,R.layout.list_cell_custom_design_patient_views_doc_prof,arr2);
        virtualapps.setAdapter(adapter2);
        grouplist=new ArrayList<>();
        createGroupList();
        createCollection();
        //-----------------------------//
        expandablelistview=findViewById(R.id.psExpandable);
        expandableListAdapter= new ExpandableListViewAdapter(this,grouplist,physicalSchedule);
        expandablelistview.setAdapter(expandableListAdapter);
        expandablelistview.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int lastExpandedPosition=-1;
            @Override
            public void onGroupExpand(int i) {
                if(lastExpandedPosition!=-1 && i!=lastExpandedPosition)
                {
                    expandablelistview.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition=i;
            }
        });
        expandablelistview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                String selected =expandableListAdapter.getChild(i,i1).toString();
                Toast.makeText(getApplicationContext(),"Selected: " + selected,Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    private void createCollection() {
        String[] clinic1 ={"10am to 12pm", "Shakeel Zafar", "0900 7860112"};
        String[] clinic2 ={"7am to 12pm", "Qari Rayan Reach", "0900 7860112"};
        String[] clinic3 ={"11am to 12pm", "Akbar Don", "0900 1860112"};
        physicalSchedule = new HashMap<>();
        for(String group:grouplist)
        {
            if(group.equals("Bachay Barhao Clinic")){
                loadChild(clinic1);
            }
            else if(group.equals("Mardana Kamzori Clinic")){
                loadChild(clinic2);
            }
            else {
                loadChild(clinic3);
            }
            physicalSchedule.put(group,childlist);
        }
    }

    private void loadChild(String[] clinicname) {
        childlist =new ArrayList<>();
        childlist.addAll(Arrays.asList(clinicname));

    }

    private void createGroupList(){
        grouplist=new ArrayList<>();
        grouplist.add("Bachay Barhao Clinic");
        grouplist.add("Mardana Kamzori Clinic");
        grouplist.add("Huzaifa Tharki Clinic");

    }
}