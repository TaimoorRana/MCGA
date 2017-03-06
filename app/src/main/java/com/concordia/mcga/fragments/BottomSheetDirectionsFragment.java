package com.concordia.mcga.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.concordia.mcga.activities.R;
import com.concordia.mcga.adapters.DirectionsArrayAdapter;
import com.concordia.mcga.lib.BottomSheetBuildingInfo;

import java.util.ArrayList;

/**
 * Created by Charmander on 2/25/2017.
 */

public class BottomSheetDirectionsFragment extends Fragment implements View.OnClickListener{

    // Layout elements
    TextView bottomSheetTextView;
    private ListView list;

    // Custom ArrayList adapter
    private DirectionsArrayAdapter adapter;

    // Arrays store directions and image information
    private ArrayList<String> displayedDirectionsList =  new ArrayList<String>();
    private ArrayList<String> displayedDirectionsImage = new ArrayList<String>();

    // Arrays store directions and images that the user may view
    private ArrayList<String> completeDirectionsImage = new ArrayList<String>();
    private ArrayList<String> completeDirectionsList = new ArrayList<String>();

    // Counter keeps track of the index of the current direction
    private int currentDirection = 0;


    // Bottomsheet
    BottomSheetBuildingInfo behavior;

    // simple buttons
    private ImageButton nextButton, previousButton, expandButton;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // View Inflater
        View view = inflater.inflate(R.layout.bottom_sheet_content, container, false);


        // UI Elements
        bottomSheetTextView = (TextView) view.findViewById(R.id.bottom_sheet_title);
        bottomSheetTextView.setText("Directions");

        list = (ListView) view.findViewById(R.id.list1);
        nextButton = (ImageButton) view.findViewById(R.id.nextButton);
        previousButton = (ImageButton) view.findViewById(R.id.previousButton);
        expandButton = (ImageButton) view.findViewById(R.id.expandButton);

        // Adapter: You need three parameters 'the context, id of the layout (it will be where the data is shown),
        // and the array that contains the data

        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorlayout);
        View bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBuildingInfo.from(bottomSheet);

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

        adapter = new DirectionsArrayAdapter(getActivity().getApplicationContext(), displayedDirectionsList, displayedDirectionsImage);
        adapter.notifyDataSetChanged();


        // ONLY FOR DEMO
        addDirection("Go Straight For 400m", "up");
        addDirection("down", "down");
        addDirection("up", "up");
        addDirection("Turn Right in 50m", "right");
        addDirection("left", "left");
        addDirection("Go Straight For 400m", "up");
        addDirection("down", "down");
        addDirection("up", "up");
        addDirection("Turn Right in 50m", "right");
        addDirection("left", "left");
        addDirection("Go Straight For 400m", "up");
        addDirection("down", "down");
        addDirection("up", "up");
        addDirection("Turn Right in 50m", "right");
        addDirection("left", "left");
        addDirection("You Have Arrived ", "destination");

        ///


        list.setAdapter(adapter);

        updateDirections();

        // Set listeners
        expandButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);


        expandButton.setImageResource(R.drawable.ic_expand_less_black_24dp);
        behavior.setState(BottomSheetBuildingInfo.STATE_COLLAPSED);

        return view;
    }


    ///////////////////////////////////////////////////////
    // Button clicks for following or previous directions
    ///////////////////////////////////////////////////////


    // Overloaded method for button clicks
    public void onClick(View clickedView) {
        switch (clickedView.getId()) {
            case R.id.nextButton:
                adapter.notifyDataSetChanged();
                nextDirection();
                break;

            case R.id.previousButton:
                previousDirection();
                break;

            case R.id.expandButton:
                if (behavior.getState() == BottomSheetBuildingInfo.STATE_COLLAPSED){
                    behavior.setState(BottomSheetBuildingInfo.STATE_EXPANDED);
                }else{
                    behavior.setState(BottomSheetBuildingInfo.STATE_COLLAPSED);
                }
                break;
        }
    }

    ///////////////////////////////////////////////////////////////
    // Adding, clearing, modifying directions from the list
    // After a change occurs, we must use the adapter to notify
    // of a modification
    ///////////////////////////////////////////////////////////////


    ///////////////////////////////////////
    // This List stores all the directions
    ///////////////////////////////////////


    // Add Directions to the list Dynamically
    public void addDirection(String direction, String image){
        completeDirectionsList.add(direction);
        completeDirectionsImage.add(image);
    }

    public void removeDirection(int index){
        completeDirectionsList.remove(index);
        completeDirectionsImage.remove(index);
    }

    // Append a list of directions to the current ones
    public void addDirectionsList(ArrayList<String> directions, ArrayList<String> image){
        for (int i =0 ; i < directions.size(); i ++){
            completeDirectionsList.add(directions.get(i));
            completeDirectionsImage.add(image.get(i));
        }
    }

    /////////////////
    // Clearing list
    /////////////////

    // Reset list and puts current index back to 0
    public void clearDirections(){
        completeDirectionsList.clear();
        completeDirectionsList.clear();
        currentDirection = 0;
        updateDirections();
    }


    ///////////////////////////////////////////////////////////////////
    // Updating current directions. Can be the previous or next one
    ///////////////////////////////////////////////////////////////////


    private void nextDirection(){
        if (displayedDirectionsList.size() > 0) {
            currentDirection++;
        }
        updateDirections();
    }

    private void previousDirection(){
        if (currentDirection > 0) {
            currentDirection--;
        }
        updateDirections();
    }

    // Updates the current list that the user views
    public void updateDirections(){
        displayedDirectionsList.clear();
        displayedDirectionsImage.clear();

        for (int i = currentDirection + 1; i < completeDirectionsList.size(); i++){
            displayedDirectionsList.add(completeDirectionsList.get(i));
            displayedDirectionsImage.add(completeDirectionsImage.get(i));
        }
        adapter.notifyDataSetChanged();

        if (completeDirectionsList.size() > 0) {
            // Set the main direction
            setTextDirections(completeDirectionsList.get(currentDirection));
        }
        else{
            setTextDirections("Directions");
        }
    }


    ////////////////////////////////////
    // Main Direction View
    ////////////////////////////////////


    // Set current direction that user may view
    public void setTextDirections(String direction){
        bottomSheetTextView.setText(direction);
    }

    public TextView getTextDirections(){
        return bottomSheetTextView;
    }


}
