
package com.example.helpgenic.CommonAdapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.helpgenic.R;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final Map<String,List<String>> physicalSchedule;
    private final List<String> groupList;
    public ExpandableListViewAdapter(Context context, List<String> grouplist, Map<String,List<String>> physicalSchedule)
    {
        this.context=context;
        this.physicalSchedule=physicalSchedule;
        this.groupList=grouplist;
    }
    @Override
    public int getGroupCount() {
        return physicalSchedule.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return Objects.requireNonNull(physicalSchedule.get(groupList.get(i))).size();
    }

    @Override
    public Object getGroup(int i) {
        return groupList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return Objects.requireNonNull(physicalSchedule.get(groupList.get(i))).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String clinic=getGroup(i).toString();
        if(view==null)
        {
            LayoutInflater inflater = (LayoutInflater)  context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.expandable_listview_group_item_patient_views_doc_profile,null);
        }
        TextView item = view.findViewById(R.id.group_item);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(clinic);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String Details = getChild(i,i1).toString();
        if(view==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.expandable_listview_child_item_patient_views_doc_prof,null);
        }
        TextView item = view.findViewById(R.id.child_item);
        item.setText(Details);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true ;
    }
}
