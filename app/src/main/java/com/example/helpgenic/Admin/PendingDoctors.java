package com.example.helpgenic.Admin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.helpgenic.AdminAdapters.CustomAdapterVerifyDoc;
import com.example.helpgenic.Classes.Admin;
import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.Doctor;
import com.example.helpgenic.DisplayImage;
import com.example.helpgenic.R;
import com.example.helpgenic.login;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class PendingDoctors extends Fragment {

    Button logOutBtn;

    ArrayList<Doctor> docList =new ArrayList<>();

    ListView doctorsList;
    DbHandler dbHandler = new DbHandler();

    // Helper Functions
    private void setUpData(String name,String specialization,int id){
        docList.add(new Doctor(name,specialization,id));
    }
    private Admin admin;
    public PendingDoctors(Admin admin) {
        // Required empty public constructor
        this.admin = admin;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_doctors, container, false);;

        dbHandler.connectToDb(getContext());
        int id=0;
        String name,specialization;

        logOutBtn = view.findViewById(R.id.logOutBtn);
        logOutBtn = view.findViewById(R.id.logOutBtn);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh = getContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sh.edit();
                myEdit.clear();

                myEdit.apply();
                Intent intent = new Intent(getContext() ,login.class);
                startActivity(intent);

                getActivity().finish();
            }
        });



        //============================== Setting up Connection WIth DB ===========================//



        ResultSet rs = dbHandler.getUnVerifiedDocs(getContext()); // get all unverified doctors

        try {
            while (rs.next()) {
                id=rs.getInt("uid");
                name=rs.getString("name");
                specialization=rs.getString("specialization");
                setUpData(name,specialization,id);
            }
        }
        catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        doctorsList =view.findViewById(R.id.verifyList);
        CustomAdapterVerifyDoc adapter1 = new CustomAdapterVerifyDoc(getContext() , R.layout.list_cell_custom_verify_doctors_design , docList, dbHandler);
        doctorsList.setAdapter(adapter1);

        doctorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Doctor d = (Doctor) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getContext() , DisplayImage.class);
                intent.putExtra("docId" , d.getId());
                startActivity(intent);
            }
        });

        //=================================== Verify/Reject Doctor ClickListeners =======================================//


        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            dbHandler.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}