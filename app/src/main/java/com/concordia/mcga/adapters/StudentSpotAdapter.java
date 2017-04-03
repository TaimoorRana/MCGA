package com.concordia.mcga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.concordia.mcga.activities.R;
import com.concordia.mcga.models.StudentSpot;

public class StudentSpotAdapter extends ArrayAdapter<StudentSpot> {
    private final Context context;

    public StudentSpotAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View spotRow = inflater.inflate(R.layout.student_spot_row, parent, false);



        return spotRow;
    }
}
