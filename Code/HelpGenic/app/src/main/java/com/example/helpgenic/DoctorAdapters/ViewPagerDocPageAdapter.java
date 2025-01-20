package com.example.helpgenic.DoctorAdapters;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.helpgenic.Classes.Doctor;
import com.example.helpgenic.Doctor.PatientsAttended;
import com.example.helpgenic.Doctor.PatientsRemaining;

public class ViewPagerDocPageAdapter extends FragmentStateAdapter {


    public ViewPagerDocPageAdapter( FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @Override
    public Fragment createFragment(int position) {
        if (position == 0){
            return new PatientsRemaining();
        }else{
            return new PatientsAttended();
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }


}
