/*------------------------------------------------Activity-----------------------------------------------*/

package com.example.helpgenic.Patient;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.helpgenic.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;

public class PatientPage extends AppCompatActivity {

    //private ActivityPatientPageBinding binding;
    private BottomNavigationView bnView;

    private void loadFrag(Fragment fragment, boolean flag){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (!flag){
            ft.add(R.id.container , fragment);
        }else{
            ft.replace(R.id.container , fragment);
        }
        ft.commit();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_patient_page);

        bnView = findViewById(R.id.nav_view);


        bnView.setSelectedItemId(R.id.navigation_home);
        loadFrag(new HomePatient(),false);

        bnView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId() ;
                if (id == R.id.navigation_home){
                    loadFrag(new HomePatient(),true);
                }else if (id == R.id.navigation_profile){
                    loadFrag(new ProfilePatient(),true);
                }else{
                    loadFrag(new NotificationPatient(),true);
                }
                return true;
            }
        });



    }



}