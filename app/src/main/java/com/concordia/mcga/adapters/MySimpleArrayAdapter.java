package com.concordia.mcga.adapters;

/**
 * Created by Charmander on 3/4/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.concordia.mcga.activities.R;

import java.util.ArrayList;

public class MySimpleArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private ArrayList<String> values = new ArrayList<String>();

    public MySimpleArrayAdapter(Context context, ArrayList<String> values) {
        super(context, R.layout.list_text, values);
        this.context = context;
        this.values = values;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_text, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.customListView);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);
        textView.setText(values.get(position));
        return rowView;
    }
}