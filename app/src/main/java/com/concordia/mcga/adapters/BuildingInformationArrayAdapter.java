package com.concordia.mcga.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


import com.concordia.mcga.activities.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;


public class BuildingInformationArrayAdapter extends ArrayAdapter<String[]> {

    ////////////////////////////////////////////////////////////
    // INSTANCE VARIABLES
    ////////////////////////////////////////////////////////////

    private final int IMAGES_PER_ROW = 4;

    private final Context context;
    private List<String[]> rowImages = new ArrayList<String[]>();

    // 4 images per per row
    private String[] image = new String[IMAGES_PER_ROW];
    private ImageView[] imageView = new ImageView[IMAGES_PER_ROW];



    ////////////////////////////////////////////////////////////
    // CLASS METHODS
    ////////////////////////////////////////////////////////////

    /**
     * Constructor
     * @param context application context
     * @param rowImages Contains the list of images to display
     */
    public BuildingInformationArrayAdapter(Context context, List<String[]> rowImages) {
        super(context, R.layout.building_info_list, rowImages);
        this.context = context;
        this.rowImages = rowImages;

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
            rowView = inflater.inflate(R.layout.building_info_list, parent, false);

            // Get Ids of layout
            imageView[0] = (ImageView) rowView.findViewById(R.id.buildingImage1);
            imageView[1] = (ImageView) rowView.findViewById(R.id.buildingImage2);
            imageView[2] = (ImageView) rowView.findViewById(R.id.buildingImage3);
            imageView[3] = (ImageView) rowView.findViewById(R.id.buildingImage4);

            // associate the string of an image to the corresponding picture
            // Will take the row at index 'position' in the arraylist and assign
            // the correct string to the image array
            for (int i = 0; i < IMAGES_PER_ROW; i++) {
                image[i] = rowImages.get(position)[i];
            }

            // This will display a maximum of 4 images per row
            try {
                for (int i = 0; i < IMAGES_PER_ROW; i++) {
                    switch (image[i]) {

                        // This case is used for testing.

                        case "up":
                            imageView[i].setImageResource(R.drawable.ic_arrow_upward_black_24dp);
                            break;



                                                //////////
                                                // HALL //
                                                //////////



                        case "asfa":
                            imageView[i].setImageResource(R.mipmap.ic_asfa);
                            break;

                        case "csu":
                            imageView[i].setImageResource(R.mipmap.ic_csu);
                            break;

                        case "hive":
                            imageView[i].setImageResource(R.mipmap.ic_hive);
                            break;

                        case "lifting":
                            imageView[i].setImageResource(R.mipmap.ic_lifting);
                            break;

                        case "scs":
                            imageView[i].setImageResource(R.mipmap.ic_scs);
                            break;

                        case "space":
                            imageView[i].setImageResource(R.mipmap.ic_space);
                            break;

                        case "sasu":
                            imageView[i].setImageResource(R.mipmap.ic_sasu);
                            break;

                        case "cssu":
                           imageView[i].setImageResource(R.mipmap.ic_ccsu);
                            break;

                        case "stinger":
                            imageView[i].setImageResource(R.mipmap.ic_stringer);
                            break;



                                            ////////
                                            // MB //
                                            ////////


                        case "jmac":
                            imageView[i].setImageResource(R.mipmap.ic_jmac);
                            break;

                        case "jmiba":
                            imageView[i].setImageResource(R.mipmap.ic_jmiba);
                            break;

                        case "jmsb_case_comp":
                            imageView[i].setImageResource(R.mipmap.ic_jmsb_case_comp);
                            break;

                        case "jmma":
                            imageView[i].setImageResource(R.mipmap.ic_jmma);
                            break;

                        case "jsec":
                            imageView[i].setImageResource(R.mipmap.ic_jsec);
                            break;

                        case "jmas":
                            imageView[i].setImageResource(R.mipmap.ic_jmas);
                            break;

                        case "fisa":
                            imageView[i].setImageResource(R.mipmap.ic_fisa);
                            break;

                        case "jmucc":
                            imageView[i].setImageResource(R.mipmap.ic_jmucc);
                            break;


                                         ///////////////
                                         // None case //
                                         ///////////////


                        case "none":
                            break;
                        default:
                            break;
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + Log.getStackTraceString(e));
            }
        }
        catch(NullPointerException e){
            Log.e(TAG, "Exception: " + Log.getStackTraceString(e));
        }
        return rowView;
    }



    /////////////////////////////////////////
    // GETTERS
    /////////////////////////////////////////

    /**
     *
     * @return ArrayList images
     */
    public List<String[]> getRowImages(){
        return rowImages;
    }
}
