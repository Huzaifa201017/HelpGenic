package com.example.helpgenic.Doctor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.helpgenic.R;
import java.util.List;


// ============================================== List Views =====================================================
class PatientAppointment{

    public String date;
    public String time;
    public String week;
    public String patientName;
    public int id;

    public PatientAppointment(String date, String time, String week, String patientName , int id) {
        this.date = date;
        this.time = time;
        this.week = week;
        this.patientName = patientName;
        this.id = id;
    }
}
class ListViewPatientsAttendedAdapter extends ArrayAdapter<PatientAppointment> {

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

// ===============================================View Pager ====================================================

public class ViewPagerDocPageAdapter extends FragmentStateAdapter {


    public ViewPagerDocPageAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
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


