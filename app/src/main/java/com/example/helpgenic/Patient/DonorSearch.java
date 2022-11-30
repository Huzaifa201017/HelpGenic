package com.example.helpgenic.Patient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.helpgenic.Classes.Patient;
import com.example.helpgenic.R;

import java.util.ArrayList;


public class DonorSearch extends Fragment {

    AutoCompleteTextView bloodGroups;
    String[] bloodGrps = {"A+" , "A-" , "B+","B-","AB+","AB-","O+","O-" };
    ListView donorsList;

    Patient p;
    public DonorSearch(Patient p) {
        // Required empty public constructor
        this.p = p;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_donor_search, container, false);
        // ======================================== Handling Sort by functionality ==========================================
        bloodGroups = view.findViewById(R.id.bloodGroups);    // 'sort by' spinner option
        donorsList = view.findViewById(R.id.donorsList);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity() , android.R.layout.simple_spinner_dropdown_item , bloodGrps);
        bloodGroups.setAdapter(adapter);

        // when user presses the autocomplete text view 'sort by' option
        bloodGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getContext(), "Item: "+ s, Toast.LENGTH_SHORT).show();
            }
        });
        return view;

    }


}