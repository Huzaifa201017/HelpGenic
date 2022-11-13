package com.example.helpgenic.PatientAdapters;

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



public class customListViewAdapter extends ArrayAdapter<Doctor> {


    public customListViewAdapter(Context context, int resource, @NonNull List<Doctor> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Doctor doc = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_cell_custom_design, parent, false);
        }
        TextView docName = convertView.findViewById(R.id.DocName);
        TextView qualificationPlusProfession = convertView.findViewById(R.id.info);
        TextView docRating = convertView.findViewById(R.id.rating);

        docName.setText(doc.name);

        String info;
        info = "";
        for (String q: doc.qualification ){
            info = info + q +", ";
        }
        info = info.substring(0,info.length()-2);

        info += "(" + doc.profession + ")";

        qualificationPlusProfession.setText(info);

        docRating.setText(String.valueOf(doc.rating));

        return convertView;
    }


}
