/*------------------------------------------------Fragment-----------------------------------------------*/

package com.example.helpgenic.Doctor;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.helpgenic.Classes.BookingManager;
import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.Doctor;
import com.example.helpgenic.Classes.GuestUser;
import com.example.helpgenic.Classes.PhysicalAppointmentSchedule;
import com.example.helpgenic.Classes.VirtualAppointmentSchedule;
import com.example.helpgenic.CommonAdapters.ListViewVirtualScheduleDisplayAdapter;
import com.example.helpgenic.DoctorAdapters.MyExpandableListAdapter;
import com.example.helpgenic.R;
import com.example.helpgenic.login;
import com.google.firebase.auth.FirebaseAuth;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class ProfileDoc extends Fragment {

    Doctor d;
    Button changeFee,addVirtualSchedule,addPhysicalSchedule;
    Dialog dialog;
    DbHandler db =new DbHandler();
    TextView fee , docName , docEmail ,docSpeciality;
    ListView virtualapps;
    ListViewVirtualScheduleDisplayAdapter adapter2;
    ArrayList<VirtualAppointmentSchedule> vSchList;
    ArrayList<PhysicalAppointmentSchedule> pSchList;
    ArrayList<String[]> clinicInfo =new ArrayList<>();
    ArrayList<String> allClinicNames= new ArrayList<>();
    View view;
    float feeAmount = 0.0f;

    List<String> groupList,childList;
    Map<String, List<String>> map;
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;


    private void setUpDialogBox(){
        // initialize dialog
        dialog = new Dialog(getContext());
        // set custom design of dialog
        dialog.setContentView(R.layout.fee_input);
        // set custom height
        dialog.getWindow().setLayout(1000,800);
        // set transparent background
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // show
        dialog.show();
    }

    private void handleDialogBoxFunctionality() {

        EditText feeInput =  dialog.findViewById(R.id.feeInput);
        Button feeSubmit = dialog.findViewById(R.id.feeSubmit);

        feeSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int updatedFee = Integer.parseInt(feeInput.getText().toString());

                dialog.dismiss();

                feeAmount = Float.parseFloat(feeInput.getText().toString());

                if(!db.isConnectionOpen()){
                    db.connectToDb(getContext());
                }
                db.updateFee(d.getId(),Integer.parseInt(feeInput.getText().toString()),getContext()).addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                Log.d("Profile Doc", "Fee Update Operation Success");
                                fee.setText(String.format(Locale.getDefault(), "FEE: %d /-", updatedFee));

                            } else {
                                Log.d("Profile Doc", "Fee Update Operation Failed");
                            }
                        }
                );


            }
        });
    }

    private void setUpData(View view){

        d = Doctor.getInstance();
        vSchList = d.getvSchedule();
        pSchList = d.getPSchedule();
        addClinicNames();
        setUpDataForEachClinic();


        // populating basic Info
        docName = view.findViewById(R.id.name);
        docName.setText(d.getName());

        docEmail = view.findViewById(R.id.email);
        docEmail.setText(d.getMail());

        docSpeciality = view.findViewById(R.id.speciality);
        docSpeciality.setText(d.getSpecialization());

        fee = view.findViewById(R.id.fee);
        fee.setText(String.format(Locale.getDefault(), "FEE: %d /-", d.getFee()));



        // populating vSchedule List
        virtualapps=view.findViewById(R.id.virtual_app_list);
        adapter2 = new ListViewVirtualScheduleDisplayAdapter(getContext(),R.layout.list_cell_custom_design_patient_views_doc_prof,vSchList);
        virtualapps.setAdapter(adapter2);


        // populating pSchedule List
        expandableListAdapter = new MyExpandableListAdapter(getContext(),groupList,map, pSchList);
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Toast.makeText(getContext(), Integer.toString(0), Toast.LENGTH_SHORT).show();

        view = inflater.inflate(R.layout.fragment_profile_doc, container, false);

        addPhysicalSchedule=view.findViewById(R.id.book_physical_appointment);
        addVirtualSchedule =view.findViewById(R.id.book_virtual_appointment);
        expandableListView= view.findViewById(R.id.psExpandable);
        Button logOut = view.findViewById(R.id.dLogOut);
        changeFee = view.findViewById(R.id.changeFee);



        setUpData(view);




        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Doctor.clearInstance();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);

                getActivity().finish();
            }
        });

        changeFee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpDialogBox();
                handleDialogBoxFunctionality();
            }
        });



        //================================== remove service =========================================
        virtualapps.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Get the clicked item
                VirtualAppointmentSchedule item = (VirtualAppointmentSchedule) adapterView.getItemAtPosition(position);
                Log.d("Delete Item", Integer.toString(position));

                // Show confirmation dialog
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this service?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                // Call Firestore to remove the item
                                db.removeVAptSchedule(item.getId(), d.getId(), getContext()).addOnCompleteListener(task -> {
                                    if (task.isSuccessful() && task.getResult()) {
                                        // Remove the item from the list and notify the adapter

                                        vSchList.remove(position);
                                        adapter2.notifyDataSetChanged();
                                    } else {
                                        Log.e("DbHandler", "Failed to delete appointment schedule");
                                    }
                                });
                            }
                        }).setNegativeButton("No", null).show();

                return true;
            }
        });

        //================================== add virtual schedule ================================
        addVirtualSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddVirtualSchedule.class);

                ArrayList<CharSequence> days = getUsedDays();
                intent.putExtra("days" ,days);

                startActivity(intent);
            }
        });

        // ================================== add physical schedule ================================
        // Click listener to button 'addSchedule for physicalSchedule'

        addPhysicalSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), com.example.helpgenic.Doctor.AddPhysicalSchedule.class);
                startActivity(intent);
            }
        });



        return view;
    }

    //function to set data for physical schedule
    public void setUpDataForEachClinic(){

        try {
            for(PhysicalAppointmentSchedule p: pSchList){

                String day = p.getDay();
                Time sTime = p.getsTime();
                Time eTime = p.geteTime();
                String AssPhone = p.getAssistantPhNum();
                String fullSchedule = "        Time :      " + sTime.toString() + "  to  " + eTime.toString() + "  -  " + day;
                String[] allinOne={fullSchedule, "        Assistant Contact :      "+AssPhone};
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
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }

    private void loadChild(String[] info) {
        childList = new ArrayList<>();
        Collections.addAll(childList, info);
    }

    public void addClinicNames(){

        try {

            groupList=new ArrayList<>();

            for (PhysicalAppointmentSchedule p: pSchList){
                groupList.add(p.getClinicName());
                allClinicNames.add(p.getClinicName());
            }

        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResume() {

        super.onResume();

        SharedPreferences sh = getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sh.edit();



        if(sh.getBoolean("pNeedToUpdate", false)){
            pSchList = d.getPSchedule();
            addClinicNames();
            setUpDataForEachClinic();
            ((MyExpandableListAdapter)expandableListAdapter).updateList(groupList,map,pSchList);


        }
        else if (sh.getBoolean("vNeedToUpdate", false)) {

            vSchList = d.getvSchedule();
            adapter2.notifyDataSetChanged();

        }
        myEdit.remove("pNeedToUpdate");
        myEdit.remove("vNeedToUpdate");
        myEdit.apply();


    }

    public ArrayList<CharSequence> getUsedDays(){
        ArrayList<CharSequence> days = new ArrayList<>();

        for(VirtualAppointmentSchedule v: vSchList){
            days.add(v.getDay());
        }
        return days;
    }

}