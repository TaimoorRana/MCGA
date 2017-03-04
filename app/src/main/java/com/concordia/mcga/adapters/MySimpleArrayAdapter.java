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

public class MySimpleArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public MySimpleArrayAdapter(Context context, String[] values) {
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
        textView.setText(values[position]);
        String s = values[position];
        if (s.startsWith("Windows7") || s.startsWith("iPhone")
                || s.startsWith("Solaris")) {
            imageView.setImageResource(R.drawable.quantum_ic_bigtop_updates_white_24);
        } else {
            imageView.setImageResource(R.drawable.ic_close_dark);
        }
        // Change the icon for Windows and iPhone
        return rowView;
    }
}