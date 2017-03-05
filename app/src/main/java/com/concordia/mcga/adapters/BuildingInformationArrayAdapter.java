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
    private final Context context;
    private ArrayList<String> information = new ArrayList<String>();
    private int index = 0;
    private ImageView imageView[] = new ImageView[4];
    private String[] imageString = new String[4];

    public BuildingInformationArrayAdapter(Context context, ArrayList<String> information) {
        super(context, R.layout.list_text, information);
        this.context = context;
        this.information = information;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate Layout + get view
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.building_info_list, parent, false);

        // Get Ids of layout
        imageView[0] = (ImageView) rowView.findViewById(R.id.buildingImage1);
        imageView[1] = (ImageView) rowView.findViewById(R.id.buildingImage2);
        imageView[2] = (ImageView) rowView.findViewById(R.id.buildingImage3);
        imageView[3] = (ImageView) rowView.findViewById(R.id.buildingImage4);

        // Set images
        imageString = information.get(position).split(", ");

        try {
            for (int i = 0 ; i > 4; i ++) {
                switch (imageString[i]) {
                    case "up":
                        imageView[i].setImageResource(R.drawable.ic_arrow_upward_black_24dp);
                        break;

                    case "down":
                        imageView[i].setImageResource(R.drawable.ic_arrow_downward_black_24dp);
                        break;

                    case "right":
                        imageView[i].setImageResource(R.drawable.ic_arrow_forward_black_24dp);
                        break;

                    case "left":
                        imageView[i].setImageResource(R.drawable.ic_arrow_back_black_24dp);
                        break;

                    case "destination":
                        imageView[i].setImageResource(R.drawable.ic_add_location_black_24dp);

                    case "None":
                        break;

                    default:
                        break;
                }
            }
        }
        catch (Exception e){

        }

        return rowView;
    }
}
