package com.example.helpgenic.DoctorAdapters;

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


public class ListViewPatientsAttendedAdapter extends ArrayAdapter<PatientAppointment> {

    public ListViewPatientsAttendedAdapter(Context context, int resource, @NonNull List<PatientAppointment> objects) {
        super(context, resource, objects);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        PatientAppointment appointment = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_cell_custom_design_patients_attended_and_remaining, parent, false);
        }

        // all views defined in list cell design
        TextView patientName = convertView.findViewById(R.id.patientNaam);
        TextView id = convertView.findViewById(R.id.id);
        TextView appointmentDate = convertView.findViewById(R.id.date);
        TextView appointmentTime = convertView.findViewById(R.id.time);


        // setting their data
        patientName.setText(appointment.patientName);

        id.setText(String.valueOf(appointment.id));  // random data
        appointmentDate.setText(new StringBuilder().append(appointment.date).append(" ").append(appointment.week).toString());
        appointmentTime.setText(appointment.time);



        return convertView;
    }

}
