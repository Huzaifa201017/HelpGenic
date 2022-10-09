/*------------------------------------------------Fragment-----------------------------------------------*/

package com.example.helpgenic.Patient;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helpgenic.R;
import com.example.helpgenic.SignUpDonor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePatient #newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePatient extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomePatient() {
        // Required empty public constructor
    }

    public void goToFindDoctorPage(View view){
//        Intent intent = new Intent(this , SignUpDonor.class);
//        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_patient, container, false);
    }
}