package com.example.helpgenic.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.helpgenic.AdminAdapters.CustomAdapterVerifyDoc;
import com.example.helpgenic.Classes.DbHandler;
import com.example.helpgenic.Classes.Doctor;
import com.example.helpgenic.DisplayImage;
import com.example.helpgenic.R;
import com.example.helpgenic.login;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class PendingDoctors extends Fragment {

    Button logOutBtn;
    ArrayList<Doctor> docList = null;
    ListView doctorsList;
    DbHandler dbHandler = new DbHandler();
    CustomAdapterVerifyDoc adapter1 = null;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pending_doctors, container, false);;



        logOutBtn = view.findViewById(R.id.logOutBtn);

        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);

                requireActivity().finish();

            }
        });


        doctorsList = view.findViewById(R.id.verifyList);

        dbHandler.getUnVerifiedDocs().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                docList = task.getResult();
                adapter1 = new CustomAdapterVerifyDoc(getContext() , R.layout.list_cell_custom_verify_doctors_design , docList);
                doctorsList.setAdapter(adapter1);
                Log.d("Pending Doctors: ", "Successfully fetched doctors list");

            } else {
                Log.d("Pending Doctors: ", "Failed to fetch doctors list");
            }
        });



        doctorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Doctor d = (Doctor) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getContext() , DisplayImage.class);
                intent.putExtra("docId" , d.getId());
                startActivity(intent);
            }
        });


        return view;
    }


}