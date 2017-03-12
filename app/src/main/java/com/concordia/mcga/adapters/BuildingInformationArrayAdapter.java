package com.concordia.mcga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.concordia.mcga.activities.R;

import java.util.ArrayList;



/**
 * Created by Charmander on 3/5/2017.
 */

public class BuildingInformationArrayAdapter extends ArrayAdapter<String> {

    ////////////////////////////////////////////////////////////
    // INSTANCE VARIABLES
    ////////////////////////////////////////////////////////////

    private final Context context;
    private ArrayList<String> information = new ArrayList<String>();
    private int index = 0;
    private ImageView imageView[] = new ImageView[4];
    private String[] imageString = new String[4];


    ////////////////////////////////////////////////////////////
    // CLASS METHODS
    ////////////////////////////////////////////////////////////

    /**
     * Constructor
     * @param context
     * @param information
     */
    public BuildingInformationArrayAdapter(Context context, ArrayList<String> information) {
        super(context, R.layout.building_info_list, information);
        this.context = context;
        this.information = information;

    }


    /**
     * Override getView in the array adapter to allow us to add images to a row
     * @param position
     * @param convertView
     * @param parent
     * @return view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate Layout + get view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.building_info_list, parent, false);
        updateRow(position, rowView);
        return rowView;
    }

    private void updateRow(int position, View rowView){
        // Get Ids of layout
        imageView[0] = (ImageView) rowView.findViewById(R.id.buildingImage1);
        imageView[1] = (ImageView) rowView.findViewById(R.id.buildingImage2);
        imageView[2] = (ImageView) rowView.findViewById(R.id.buildingImage3);
        imageView[3] = (ImageView) rowView.findViewById(R.id.buildingImage4);

        String[] image = information.get(position).split("-");

        try {
            for (int i = 0; i < 4; i ++) {
                switch (image[i]) {
                    case "up":
                        imageView[i].setImageResource(R.drawable.ic_arrow_upward_black_24dp);
                        break;

                    case "none":
                        break;
                    default:
                        break;
                }
            }
        }
        catch (Exception e){

        }
    }
}
