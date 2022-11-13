package com.example.helpgenic.PatientAdapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.helpgenic.Patient.HistoryWithDoctors;
import com.example.helpgenic.Patient.UpcomingAppointments;

public class ViewPagerPatientHistoryAdapter extends FragmentStateAdapter {

    public ViewPagerPatientHistoryAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @Override
    public Fragment createFragment(int position) {
        if (position == 0){
            return new UpcomingAppointments();
        }else{
            return new HistoryWithDoctors();
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
