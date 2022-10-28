package com.example.helpgenic.Patient;

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

// random class
class Appointment{
    public String date;
    public String time;
    public String week;
    public String docName;

    public Appointment(String date, String time, String week, String docName) {
        this.date = date;
        this.time = time;
        this.week = week;
        this.docName = docName;
    }
}

public class ListViewPatientHistoryAdapter extends ArrayAdapter<Appointment> {


    public ListViewPatientHistoryAdapter( Context context, int resource, @NonNull List<Appointment> objects) {
        super(context, resource, objects);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Appointment appointment = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_cell_custom_design_for_patient_schedule, parent, false);
        }

        // all views defined in list cell design
        TextView docName = convertView.findViewById(R.id.DocName);
        TextView qualificationPlusProfession = convertView.findViewById(R.id.info);
        TextView appointmentDate = convertView.findViewById(R.id.date);
        TextView appointmentTime = convertView.findViewById(R.id.time);


        // setting their data
        docName.setText(appointment.docName);

//        String info;
//        info = "";
//        for (String q: doc.qualification ){
//            info = info + q +", ";
//        }
//        info = info.substring(0,info.length()-2);
//
//        info += "(" + doc.profession + ")";

        qualificationPlusProfession.setText("MBBS, FCPS (Pediatric)");  // random data
        appointmentDate.setText(new StringBuilder().append(appointment.date).append(" ").append(appointment.week).toString());
        appointmentTime.setText(appointment.time);



        return convertView;
    }
}
