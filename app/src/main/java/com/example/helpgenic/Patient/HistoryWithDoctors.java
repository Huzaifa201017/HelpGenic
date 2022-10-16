package com.example.helpgenic.Patient;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.helpgenic.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryWithDoctors extends Fragment {

    public HistoryWithDoctors() {

    }


    List<String> DocDescription;
    int[] Docs = {R.drawable.doc1, R.drawable.doc2, R.drawable.doc3, R.drawable.doc1, R.drawable.doc2, R.drawable.doc3};

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_history_with_doctors,container,false);

        ListView listView = contentView.findViewById(R.id.AppointmentsListView);
        List<String> list = new ArrayList<>();

        for (int i=0; i<6; i++)
        {
            list.add("Doctor Name:          \n\nQualification:           \n\nRating:          \n");
        }
        CustomAdapter customAdapter = new CustomAdapter(list);
        listView.setAdapter(customAdapter);

        return contentView;
    }


    class CustomAdapter extends BaseAdapter{
        public CustomAdapter(List<String> docDescription){
            DocDescription=docDescription;
        }

        @Override
        public int getCount() {
            return DocDescription.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View convertview, ViewGroup parent) {
            @SuppressLint({"ViewHolder", "InflateParams"}) View view = getLayoutInflater().inflate(R.layout.fragment_history_with_doctors,null);
            ImageView mImageView = (ImageView) view.findViewById(R.id.ImageView);
            TextView mTextView = (TextView) view.findViewById(R.id.TextView);

            mImageView.setImageResource(Docs[i]);
            mTextView.setText(DocDescription.get(i));
            return view;
        }
    }


}