package com.concordia.mcga.adapters;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.concordia.mcga.activities.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class DirectionsArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private List<String> directionsText = new ArrayList<String>();
    private List<String> directionsImage = new ArrayList<String>();

    /**
     * Constructor
     * @param context application context
     * @param directionsText text that will give directions
     * @param directionsImage images associated with directions
     */
    public DirectionsArrayAdapter(Context context, List<String> directionsText, List<String> directionsImage) {
        super(context, R.layout.list_text, directionsText);
        this.context = context;
        this.directionsText = directionsText;
        this.directionsImage = directionsImage;
    }


    /**
     * Override getView in the array adapter to allow us to add images to a row
     * @param position row index that the user views
     * @param convertView application View
     * @param parent Viewgroup of the application View
     * @return view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate Layout + get view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = null;

        try {
            rowView = inflater.inflate(R.layout.list_text, parent, false);


            // Get Ids of layout
            TextView textView = (TextView) rowView.findViewById(R.id.customListView);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);

            // Set text and images
            textView.setText(directionsText.get(position));

            try {
                String image = directionsImage.get(position);

                // Depending on the string, a different image is generated
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
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + Log.getStackTraceString(e));
            }
        }
        catch(NullPointerException e){
            Log.e(TAG, "Exception: " + Log.getStackTraceString(e));
        }
        return rowView;
    }
}