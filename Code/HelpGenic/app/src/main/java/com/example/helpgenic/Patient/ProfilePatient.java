/*------------------------------------------------Fragment-----------------------------------------------*/
package com.example.helpgenic.Patient;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.helpgenic.Classes.Patient;
import com.example.helpgenic.R;
import com.example.helpgenic.login;
import com.google.firebase.auth.FirebaseAuth;

public class ProfilePatient extends Fragment {

    Patient p;

    TextView pName,phNum,email , bloodGroup , dob;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_patient, container, false);

        p = Patient.getInstance();

        pName = view.findViewById(R.id.pName);
        phNum = view.findViewById(R.id.phNum);
        email = view.findViewById(R.id.email);
        bloodGroup = view.findViewById(R.id.bloodGroup);
        dob = view.findViewById(R.id.dob);

        pName.setText(p.getName());
        phNum.setText(p.getPhNum());
        email.setText(p.getMail());
        bloodGroup.setText(p.getBloodGroup());
        dob.setText(p.getDob().toString());

        Button logOut = view.findViewById(R.id.logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                Intent intent = new Intent(getActivity(), login.class);
                startActivity(intent);

                requireActivity().finish();
            }
        });
        return view;

    }
}