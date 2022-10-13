/*------------------------------------------------Fragment-----------------------------------------------*/
package com.example.helpgenic.Doctor;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.helpgenic.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class HomeDoc extends Fragment {

    TabLayout tab;
    ViewPager2 viewPager;

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    public HomeDoc() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_doc, container, false);
        tab = view.findViewById(R.id.tab);
        viewPager = view.findViewById(R.id.viewPager);

        ViewPagerDocPageAdapter adapter = new ViewPagerDocPageAdapter(getActivity().getSupportFragmentManager(), getLifecycle() );
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tab, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0){
                    tab.setText("Appointments");
                }else{
                    tab.setText("Patients");
                }
            }
        }).attach();
        return view;
    }
}