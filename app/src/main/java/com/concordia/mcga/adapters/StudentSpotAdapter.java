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
    private final DecimalFormat df = new DecimalFormat(".##");
    private final static int RADIUS_EARTH = 6371;

    /**
     * Constructor which takes the list of student spots as well as a coordinate to calculate
     * distance.
     * @param context Application context
     * @param spots List of student spots to be displayed and sorted
     * @param currentCoordinates Coordinates to be compared to
     */
    public StudentSpotAdapter(Context context, List<StudentSpot> spots, LatLng currentCoordinates) {
        super(context, R.layout.student_spot_row, spots);
        this.context = context;

        // Set distance
        for (StudentSpot i: spots) {
            i.setLastKnownDistance(getDistance(currentCoordinates, i.getMapCoordinates()));
        }
    }

    /**
     * Gets the android view to display a particular student spot. Inflates from the xml then
     * populates with attributes from a given StudentSpot object
     * @param position Position in the listadapter
     * @param convertView View to convert, provided by interface
     * @param parent Parent view provided by interface
     * @return View displaying a student spot
     */
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

        StudentSpot item = getItem(position);

        name.setText(item.getName());
        address.setText(item.getAddress());
        distance.setText(df.format(item.getLastKnownDistance()) + " m");
        description.setText(item.getDescription());
        image.setImageResource(item.getResId());
        rating.setRating(item.getRating());

        return spotRow;
    }

    /**
     * Generates a readable string indicating the distance between any two coordinates
     * @param start First coordinate to compare, as a LatLng
     * @param end Second coordinate to compare, as a LatLng
     * @return Formatted string indicating distance
     */
    private double getDistance(LatLng start, LatLng end) {
        double lat  = Math.toRadians(end.latitude - start.latitude);
        double lon = Math.toRadians(end.longitude - start.longitude);

        double a = Math.sin(lat / 2) * Math.sin(lat / 2)
                + Math.cos(Math.toRadians(start.latitude)) * Math.cos(Math.toRadians(end.latitude))
                * Math.sin(lon / 2) * Math.sin(lon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RADIUS_EARTH * c * 1000;
    }
}
