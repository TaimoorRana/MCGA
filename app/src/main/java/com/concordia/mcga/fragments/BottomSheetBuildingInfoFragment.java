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
import com.concordia.mcga.adapters.DirectionsArrayAdapter;
import com.concordia.mcga.lib.BottomSheetBuildingInfo;

import java.util.ArrayList;

/**
 * Created by Charmander on 3/5/2017.
 */

public class BottomSheetBuildingInfoFragment extends Fragment {


    // Bottomsheet
    private BottomSheetBuildingInfo behavior;

    // UI elements
    private ImageButton expandButton;
    private TextView bottom_sheet_title, address, closingTime, openingTime;

    //Array adapter
    private BuildingInformationArrayAdapter adapter;

    private ArrayList<String> images = new ArrayList<String>();
    private ArrayList<String> rowImages = new ArrayList<String>();

    private ListView list;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // View Inflater
        View view = inflater.inflate(R.layout.building_information_fragment, container, false);


        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorlayout);
        View bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);

        behavior = BottomSheetBuildingInfo.from(bottomSheet);
        expandButton = (ImageButton) view.findViewById(R.id.expandButton);
        bottom_sheet_title =  (TextView) view.findViewById(R.id.bottom_sheet_title);
        address = (TextView) view.findViewById(R.id.address);
        closingTime = (TextView) view.findViewById(R.id.closingTime);
        openingTime = (TextView) view.findViewById(R.id.openingTime);
        list = (ListView) view.findViewById(R.id.list1);

        behavior.addBottomSheetCallback(new BottomSheetBuildingInfo.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBuildingInfo.STATE_COLLAPSED:
                        expandButton.setImageResource(R.drawable.ic_expand_less_black_24dp);
                        break;

                    case BottomSheetBuildingInfo.STATE_EXPANDED:
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

        //List adapter
        adapter = new BuildingInformationArrayAdapter(getActivity().getApplicationContext(), images);
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);

        //////////////////////////////////////////////////
        // FOR DEMO
        addImage("up");
        addImage("up");
        addImage("up");
        addImage("up");
        addImage("up");
        addImage("up");
        addImage("up");
        addImage("up");

        //

        updateImageRow();
        //////////////////////////////////////////////////


        //Set bottomsheet to hidden
        //behavior.setState(BottomSheetBuildingInfo.STATE_HIDDEN);
        // Set bottom sheet to collapsed
        expandButton.setImageResource(R.drawable.ic_expand_less_black_24dp);
        behavior.setState(BottomSheetBuildingInfo.STATE_COLLAPSED);

        return view;
    }

    // Set building info 1 shot easy money
    public void setBuildingInformation(String bottom_sheet_title, String address, String openingTime, String closingTime){
        setBottomSheetTitle(bottom_sheet_title);
        setAddress(address);
        setOpeningTime(openingTime);
        setClosingTime(closingTime);
    }

    public void collapse(){
        behavior.setState(BottomSheetBuildingInfo.STATE_COLLAPSED);
    }

    public void expand(){
        behavior.setState(BottomSheetBuildingInfo.STATE_EXPANDED);
    }


    //Updates a row
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

    public void clear(){
        images.clear();
        rowImages.clear();
        updateImageRow();
    }

    private void internalClear(){
        rowImages.clear();
    }

    public void addImage(String image){
        rowImages.add(image);
    }



    private void setBottomSheetTitle(String title){
        bottom_sheet_title.setText(title);
    }

    private void setAddress(String address){
        this.address.setText(address);
    }

    private void setClosingTime(String time){
        closingTime.setText(time);
    }

    private void setOpeningTime(String time){
        openingTime.setText(time);
    }
}
