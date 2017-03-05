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


public class DirectionsArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private ArrayList<String> directionsText = new ArrayList<String>();
    private ArrayList<String> directionsImage = new ArrayList<String>();

    public DirectionsArrayAdapter(Context context, ArrayList<String> directionsText, ArrayList<String> directionsImage) {
        super(context, R.layout.list_text, directionsText);
        this.context = context;
        this.directionsText = directionsText;
        this.directionsImage = directionsImage;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate Layout + get view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_text, parent, false);

        // Get Ids of layout
        TextView textView = (TextView) rowView.findViewById(R.id.customListView);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);

        // Set text and imagesz
        textView.setText(directionsText.get(position));

        try {
            String image = directionsImage.get(position);

            switch (image) {
                case "up":
                    imageView.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
                    break;

                case "down":
                    imageView.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
                    break;

                case "right":
                    imageView.setImageResource(R.drawable.ic_arrow_forward_black_24dp);
                    break;

                case "left":
                    imageView.setImageResource(R.drawable.ic_arrow_back_black_24dp);
                    break;

                case "destination":
                    imageView.setImageResource(R.drawable.ic_add_location_black_24dp);

                case "None":
                    break;

                default:
                    break;
            }
            return rowView;
        }
        catch (Exception e){

        }

        return rowView;
    }
}