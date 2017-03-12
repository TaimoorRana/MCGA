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
import com.concordia.mcga.lib.BuildingBottomSheetInfo;

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

    // View
    View view;
    View bottomSheet;
    CoordinatorLayout coordinatorLayout;

    // Bottomsheet
    BuildingBottomSheetInfo behavior;

    // simple buttons
    private ImageButton nextButton, previousButton, expandButton;


    /**
     * On create view override
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return layout inflater view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // View Inflater
        view = inflater.inflate(R.layout.bottom_sheet_content, container, false);
        setupUiElements();
        setupBottomSheetView();
        setupBottomSheetBehavior();
        overrideBottomSheetCallBack();
        setupAdapter();
        setupButtonListeners();

        return view;
    }

    /**
     * Instantiate click listeners
     */
    private void setupButtonListeners(){
        // Set listeners
        expandButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        expandButton.setImageResource(R.drawable.ic_expand_less_black_24dp);
    }

    /**
     * Instantiate bottomsheet behaviors
     */
    private void setupBottomSheetBehavior(){
        behavior = BuildingBottomSheetInfo.from(bottomSheet);
        behavior.setmType("building_navigation");
        behavior.setState(BuildingBottomSheetInfo.STATE_COLLAPSED);
    }

    /**
     * Instantiate the Bottom Sheet Views
     */
    private void setupBottomSheetView(){
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorlayout);
        bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
    }

    /**
     * Instantiate ArrayList Adapter
     */
    private void setupAdapter(){
        adapter = new DirectionsArrayAdapter(getActivity().getApplicationContext(), displayedDirectionsList, displayedDirectionsImage);
        adapter.notifyDataSetChanged();
        list.setAdapter(adapter);
    }

    /**
     * Instantiate UI elements
     */
    private void setupUiElements(){
        // UI Elements
        bottomSheetTextView = (TextView) view.findViewById(R.id.bottom_sheet_title);
        bottomSheetTextView.setText("Directions");
        list = (ListView) view.findViewById(R.id.list1);
        nextButton = (ImageButton) view.findViewById(R.id.nextButton);
        previousButton = (ImageButton) view.findViewById(R.id.previousButton);
        expandButton = (ImageButton) view.findViewById(R.id.expandButton);
    }

    /**
     * Override bottomsheetcallback
     * added different behaviors on state changes
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


    ///////////////////////////////////////////////////////
    // Button clicks for following or previous directions
    ///////////////////////////////////////////////////////


    /**
     * Override on click method
     * @param clickedView
     */
    @Override
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
                if (behavior.getState() == BuildingBottomSheetInfo.STATE_COLLAPSED){
                    behavior.setState(BuildingBottomSheetInfo.STATE_EXPANDED);
                }else{
                    behavior.setState(BuildingBottomSheetInfo.STATE_COLLAPSED);
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


    /**
     * Add Directions to the list Dynamically
     * @param direction
     * @param image
     */
    public void addDirection(String direction, String image){
        completeDirectionsList.add(direction);
        completeDirectionsImage.add(image);
    }

    /**
     * Remove directions from list
     * @param index
     */
    public void removeDirection(int index){
        completeDirectionsList.remove(index);
        completeDirectionsImage.remove(index);
    }

    /**
     * Append directions to the list
     * @param directions
     * @param image
     */
    public void addDirectionsList(ArrayList<String> directions, ArrayList<String> image){
        for (int i =0 ; i < directions.size(); i ++){
            completeDirectionsList.add(directions.get(i));
            completeDirectionsImage.add(image.get(i));
        }
    }

    /////////////////
    // Clearing list
    /////////////////

    /**
     * Clear List of directions and resets index back to 0
     */
    public void clearDirections(){
        completeDirectionsList.clear();
        completeDirectionsList.clear();
        currentDirection = 0;
        updateDirections();
    }


    ///////////////////////////////////////////////////////////////////
    // Updating current directions. Can be the previous or next one
    ///////////////////////////////////////////////////////////////////

    /**
     * Display the next direction
     */
    private void nextDirection(){
        if (displayedDirectionsList.size() > 0) {
            currentDirection++;
        }
        updateDirections();
    }

    /**
     * display the previous direction
     */
    private void previousDirection(){
        if (currentDirection > 0) {
            currentDirection--;
        }
        updateDirections();
    }


    /**
     * Updates the list view through the adapter pattern
     */
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


    /**
     * Set current direction that user may view
     * @param direction
     */
    public void setTextDirections(String direction){
        bottomSheetTextView.setText(direction);
    }

    
    /**
     * @return TextView
     */
    public TextView getTextDirections(){
        return bottomSheetTextView;
    }


    ////////////////////////////////////
    // AUTO-GENERATE GETTERS
    ////////////////////////////////////

    public TextView getBottomSheetTextView() {
        return bottomSheetTextView;
    }

    public ListView getList() {
        return list;
    }

    public DirectionsArrayAdapter getAdapter() {
        return adapter;
    }

    public ArrayList<String> getDisplayedDirectionsList() {
        return displayedDirectionsList;
    }

    public ArrayList<String> getDisplayedDirectionsImage() {
        return displayedDirectionsImage;
    }

    public ArrayList<String> getCompleteDirectionsImage() {
        return completeDirectionsImage;
    }

    public ArrayList<String> getCompleteDirectionsList() {
        return completeDirectionsList;
    }

    public int getCurrentDirection() {
        return currentDirection;
    }

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    public View getBottomSheet() {
        return bottomSheet;
    }

    public CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }

    public BuildingBottomSheetInfo getBehavior() {
        return behavior;
    }

    public ImageButton getNextButton() {
        return nextButton;
    }

    public ImageButton getPreviousButton() {
        return previousButton;
    }

    public ImageButton getExpandButton() {
        return expandButton;
    }

}
