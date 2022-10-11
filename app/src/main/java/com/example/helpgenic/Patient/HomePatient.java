/*------------------------------------------------Fragment-----------------------------------------------*/

package com.example.helpgenic.Patient;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.helpgenic.R;
import com.example.helpgenic.SignUpDonor;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomePatient #newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomePatient extends Fragment  {

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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Button  findDocBtn;  // FindDoctor button reference

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home_patient, container, false);
        findDocBtn = view.findViewById(R.id.findDoc);

        // on button click go to FindDoctor class Page (activity)
        findDocBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity() , FindDoctor.class);
                startActivity(intent);

            }
        });


        return view; // return view
    }
}