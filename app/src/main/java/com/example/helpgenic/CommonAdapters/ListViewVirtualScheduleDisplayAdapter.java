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


public class ListViewVirtualScheduleDisplayAdapter extends ArrayAdapter<String> {
    private final int resource;
    public ListViewVirtualScheduleDisplayAdapter(@NonNull Context context, int resource, @NonNull String[] objects) {

        super(context, resource, objects);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        String item  = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resource, parent, false);
        }


        TextView fileName = convertView.findViewById(R.id.textView2);
        fileName.setText(item);



        return convertView;
    }
}
