package com.example.helpgenic.CommonAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.helpgenic.R;

import java.util.List;

public class ListViewAppointmentsAdapter extends ArrayAdapter<String> {

    public ListViewAppointmentsAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String item  = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.appointments_list_custom_design, parent, false);
        }


        TextView date = convertView.findViewById(R.id.date);
        date.setText(item);

        TextView appointmentNum = convertView.findViewById(R.id.appointmentNum);
        appointmentNum.setText("Appointment" + (position+1));



        return convertView;
    }

}
