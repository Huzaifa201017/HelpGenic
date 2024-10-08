/*------------------------------------------------Activity-----------------------------------------------*/
package com.example.helpgenic.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.helpgenic.Classes.Admin;
import com.example.helpgenic.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AdminPage extends AppCompatActivity {

    private BottomNavigationView bnView;
    private Admin admin;


    private void loadFrag(Fragment fragment, boolean flag) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (!flag) {
            ft.add(R.id.container, fragment);
        } else {
            ft.replace(R.id.container, fragment);
        }
        ft.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin);

        bnView = findViewById(R.id.nav_view);
        admin = (Admin) getIntent().getSerializableExtra("admin");


        bnView.setSelectedItemId(R.id.navigation_home);
        loadFrag(new PendingDoctors(admin), false);

        bnView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    loadFrag(new PendingDoctors(admin), true);
                } else if (id == R.id.navigation_ratingsGraph) {
                    loadFrag(new GraphWithRating(admin), true);
                } else {
                    loadFrag(new GraphWithPatientsNum(admin), true);
                }
                return true;
            }
        });

    }
}