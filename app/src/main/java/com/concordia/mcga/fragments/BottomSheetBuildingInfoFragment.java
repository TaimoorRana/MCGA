package com.concordia.mcga.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.concordia.mcga.activities.R;
import com.concordia.mcga.adapters.BuildingInformationArrayAdapter;
import com.concordia.mcga.lib.BuildingBottomSheetInfo;

import java.util.ArrayList;

/**
 * Created by Charmander on 3/5/2017.
 */

public class BottomSheetBuildingInfoFragment extends Fragment implements View.OnClickListener{


    ////////////////////////////////////////////////////////////
    // INSTANCE VARIABLES
    ////////////////////////////////////////////////////////////


    // Bottomsheet
    private BuildingBottomSheetInfo behavior = null;

    // UI elements
    private ImageButton expandButton = null;
    private TextView bottom_sheet_title = null, address = null, closingTime = null, openingTime = null;
    private ListView list = null;

    // Array adapter
    private BuildingInformationArrayAdapter adapter;
    private ArrayList<String> images = new ArrayList<String>();
    private ArrayList<String> rowImages = new ArrayList<String>();

    // Main view
    private View view = null;

    // Bottom sheet view
    private CoordinatorLayout coordinatorLayout = null;
    private View bottomSheet = null;



    ////////////////////////////////////////////////////////////
    // CLASS METHODS
    ////////////////////////////////////////////////////////////


    /**
     * Method will inflate View as well as assign the correct ID to all elements
     * present in the UI. The list adapter is initialized.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.building_information_fragment, container, false);
        setupBottomSheetView();
        setupButtons();
        setupListElements();
        setupBottomSheetBehavior();
        overrideBottomSheetCallBack();
        setupListAdapter();
        return view;
    }

    /**
     * Buttons are assigned their corresponding ID from thhe layout.xml file
     */
    private void setupButtons(){
        expandButton = (ImageButton) view.findViewById(R.id.expandButton);
        expandButton.setImageResource(R.drawable.ic_expand_less_black_24dp);
        expandButton.setOnClickListener(this);
    }

    /**
     * Bottom Sheet behavior is assigned
     */
    private void setupBottomSheetBehavior(){
        behavior = BuildingBottomSheetInfo.from(bottomSheet);
        behavior.setmType("building_information");
        behavior.setState(BuildingBottomSheetInfo.STATE_COLLAPSED);
    }

    /**
     * Assign the ID to all elements contained in the list view
     */
    private void setupListElements(){
        bottom_sheet_title =  (TextView) view.findViewById(R.id.bottom_sheet_title);
        address = (TextView) view.findViewById(R.id.address);
        closingTime = (TextView) view.findViewById(R.id.closingTime);
        openingTime = (TextView) view.findViewById(R.id.openingTime);
        list = (ListView) view.findViewById(R.id.list1);
    }

    /**
     * Assign the Id to the bottomsheet
     */
    private void setupBottomSheetView(){
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorlayout);
        bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
    }

    /**
     * Override the bottomsheet call back fonctionality. A drawable image is added
     * to let the user know in which direction the bottomsheet may expand/retract
     */
    private void overrideBottomSheetCallBack(){
        behavior.addBottomSheetCallback(new BuildingBottomSheetInfo.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BuildingBottomSheetInfo.STATE_COLLAPSED:
                        expandButton.setImageResource(R.drawable.ic_expand_less_black_24dp);
                        break;

                    case BuildingBottomSheetInfo.STATE_EXPANDED:
                        expandButton.setImageResource(R.drawable.ic_expand_more_black_24dp);
                        break;

                    default:
                        break;
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    /**
     * Instanciate the arraylist adapter and assign it to the list
     */
    private void setupListAdapter(){
        adapter = new BuildingInformationArrayAdapter(getActivity().getApplicationContext(), images);
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);
    }

    /**
     *  Override on click method to define it's behavior
     * @param clickedView
     */
    // Overloaded method for button clicks
    @Override
    public void onClick(View clickedView) {
        switch (clickedView.getId()) {
            case R.id.expandButton:
                if (behavior.getState() == BuildingBottomSheetInfo.STATE_COLLAPSED){
                    behavior.setState(BuildingBottomSheetInfo.STATE_EXPANDED);
                }else{
                    behavior.setState(BuildingBottomSheetInfo.STATE_COLLAPSED);
                }
                break;
        }
    }

    /**
     * Add textual information into textboxes found in the layout.xml
     * @param bottom_sheet_title
     * @param address
     * @param openingTime
     * @param closingTime
     */
    // Set building info 1 shot easy money
    public void setBuildingInformation(String bottom_sheet_title, String address, String openingTime, String closingTime){
        setBottomSheetTitle(bottom_sheet_title);
        setAddress(address);
        setOpeningTime(openingTime);
        setClosingTime(closingTime);
    }

    /**
     * Forces the Bottom Sheet to collapse
     */
    public void collapse(){
        behavior.setState(BuildingBottomSheetInfo.STATE_COLLAPSED);
    }

    /**
     * Forces the Bottom Sheet to expand
     */
    public void expand(){
        behavior.setState(BuildingBottomSheetInfo.STATE_EXPANDED);
    }


    /**
     * Updates the row of images associate to the building information. Works
     * similarly to a unit of Work
     */
    public void updateImageRow(){
        String img = "";
        for (int i = 0; i < rowImages.size(); i++){
            img += rowImages.get(i);

            if ((i + 1) % 4 ==0){
                images.add(img);
                adapter.notifyDataSetChanged();
                img = "";
            }
            else if(i == rowImages.size() - 1){
                images.add(img);
                adapter.notifyDataSetChanged();
                img = "";
            }
            else{
                img += "-";
            }
        }
    }

    /**
     * Clears array containing all image information
     */
    public void clear(){
        images.clear();
        rowImages.clear();
        updateImageRow();
    }

    /**
     * Clears all images associated to a row
     */
    private void internalClear(){
        rowImages.clear();
    }

    /**
     * Adds an image to the array
     * @param image
     */
    public void addImage(String image){
        rowImages.add(image);
    }


    /**
     * Sets the Bottom Sheet title / building name
     * @param title
     */
    private void setBottomSheetTitle(String title){
        bottom_sheet_title.setText(title);
    }

    /**
     * Sets the building address
     * @param address
     */
    private void setAddress(String address){
        this.address.setText(address);
    }

    /**
     * Sets closing time of building
     * @param time
     */
    private void setClosingTime(String time){
        closingTime.setText("Closing Hours: " + time);
    }

    /**
     * Sets opening time of building
     * @param time
     */
    private void setOpeningTime(String time){
        openingTime.setText("Opening Hours: " + time);
    }
}
