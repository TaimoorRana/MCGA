package com.concordia.mcga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.concordia.mcga.activities.R;
import com.concordia.mcga.models.StudentSpot;
import com.google.android.gms.maps.model.LatLng;

import java.text.DecimalFormat;
import java.util.List;

public class StudentSpotAdapter extends ArrayAdapter<StudentSpot> {
    private final Context context;
    private final List<StudentSpot> spots;
    private final DecimalFormat df = new DecimalFormat(".##");
    private LatLng currentCoordinates;

    public StudentSpotAdapter(Context context, List<StudentSpot> spots) {
        super(context, R.layout.student_spot_row, spots);
        this.context = context;
        this.spots = spots;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View spotRow = inflater.inflate(R.layout.student_spot_row, parent, false);

        TextView name = (TextView) spotRow.findViewById(R.id.spotTitle);
        TextView address = (TextView) spotRow.findViewById(R.id.spotAddress);
        TextView distance = (TextView) spotRow.findViewById(R.id.spotDistance);
        TextView description = (TextView) spotRow.findViewById(R.id.spotDescription);
        ImageView image = (ImageView) spotRow.findViewById(R.id.spotImage);
        RatingBar rating = (RatingBar) spotRow.findViewById(R.id.spotRating);

        name.setText(getItem(position).getName());
        address.setText(getItem(position).getAddress());
        distance.setText(getFormattedDistance(currentCoordinates,
                getItem(position).getMapCoordinates()));
        description.setText(getItem(position).getDescription());
        image.setImageResource(getItem(position).getResId());
        rating.setRating(getItem(position).getRating().getRatingVal());

        return spotRow;
    }

    private String getFormattedDistance(LatLng start, LatLng end) {
        final int radius = 6371;

        double lat  = Math.toRadians(end.latitude - start.latitude);
        double lon = Math.toRadians(end.longitude - end.longitude);

        double a = Math.sin(lat / 2) * Math.sin(lat / 2)
                + Math.cos(Math.toRadians(start.latitude)) * Math.cos(Math.toRadians(end.latitude))
                * Math.sin(lon / 2) * Math.sin(lon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = radius * c * 1000;

        return df.format(distance) + " m";
    }
}
