/*------------------------------------------------Fragment-----------------------------------------------*/

package com.example.helpgenic.Patient;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    ArrayList<Doctor> docList = new ArrayList<>();
    String[] items = {"Rating" , "Fee" , "PatientsAttended" };
    String[] categories = {"Dermatologist" , "Allergist" , "Neurologist" , "Pathologist" , "Urologist" , "Anesthesiologist" , "Ophthalmologist"};


    ListView doctorsList; // list of Doctors
    EditText sView;
    AutoCompleteTextView tView;
    TextView selectOption;
    Dialog dialog;


    // Helper Functions
    private void setUpData(){
        String [] arr = {"MBBS" , "FCPS"};
        docList.add(new Doctor("Dr. Javed Butt" , arr, "Pediatrics" , (float) 4.6));

        String [] arr1 = {"MBBS" , "FCPS"};
        docList.add(new Doctor("Dr. Iqbal Changezi" , arr1, "Cardiologist" , (float) 4.5));

        String [] arr2 = {"MBBS"};
        docList.add(new Doctor("Dr. Iqbal Changezi" , arr2, "ENT Specialist" , (float) 4.5));

    }
    private void setUpDialogBox(){
        // initialize dialog
        dialog = new Dialog(getContext());
        // set custom design of dialog
        dialog.setContentView(R.layout.searchable_spinner_custom_design);
        // set custom height
        dialog.getWindow().setLayout(1000,1500);
        // set transparent background
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // show
        dialog.show();
    }
    private void handleDialogBoxFunctionality(){

        EditText editText = dialog.findViewById(R.id.editText);
        ListView dialogList = dialog.findViewById(R.id.categoryList);
        ArrayAdapter<String> dialogAdapter = new ArrayAdapter<>(getContext() , android.R.layout.simple_list_item_1 , categories);
        dialogList.setAdapter(dialogAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // leave it
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // filter list
                dialogAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // leave it
            }
        });
        dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // set text view as the selected item
                selectOption.setText(dialogAdapter.getItem(i));
                // close the dialog box
                dialog.dismiss();
            }
        });
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_patient, container, false); // return view

        //================================== Select category functionality =======================
        selectOption = view.findViewById(R.id.selectCategory); // select category option

        // When user presses the select category option
        selectOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpDialogBox();
                handleDialogBoxFunctionality();


            }
        });
        // ==================================================================================



        // ========================================= Handling Search View click =========================================
        sView = view.findViewById(R.id.searchView);       // search view at the top
        // ==================================================================================



        // ======================================== Handling Sort by functionality ==========================================
        tView = view.findViewById(R.id.searchOptions);    // 'sort by' spinner option

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity() , android.R.layout.simple_spinner_dropdown_item , items);
        tView.setAdapter(adapter);

        // when user presses the autocomplete text view 'sort by' option
        tView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String s = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getContext(), "Item: "+ s, Toast.LENGTH_SHORT).show();
            }
        });

        // ==================================================================================



        // =======================================Populating the doctors list ===========================================
        // populate the data
        setUpData();
        // set adapter to list view
        doctorsList = view.findViewById(R.id.DocList);
        customListViewAdapter adapter1 = new customListViewAdapter(getContext() , R.layout.list_cell_custom_design , docList);
        doctorsList.setAdapter(adapter1);




        return view;
    }
}