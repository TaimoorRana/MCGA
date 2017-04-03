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
import com.concordia.mcga.lib.BottomSheet;

import java.util.ArrayList;
import java.util.List;


public class BottomSheetDirectionsFragment extends Fragment implements View.OnClickListener{

    // Layout elements
    private TextView bottomSheetTextView;
    private ListView list;

    // Custom ArrayList adapter
    private DirectionsArrayAdapter adapter;

    // Arrays store directions and image information
    private List<String> displayedDirectionsList =  new ArrayList<String>();
    private List<String> displayedDirectionsImage = new ArrayList<String>();

    // Arrays store directions and images that the user may view
    private List<String> completeDirectionsImage = new ArrayList<String>();
    private List<String> completeDirectionsList = new ArrayList<String>();

    // Counter keeps track of the index of the current direction
    private int currentDirection = 0;

    // View
    private View view;
    private View bottomSheet;
    private CoordinatorLayout coordinatorLayout;

    // Bottomsheet
    private BottomSheet behavior;

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

        addDirection("direction", "up");
        addDirection("direction", "up");
        addDirection("direction", "up");
        addDirection("direction", "up");
        addDirection("direction", "up");
        addDirection("direction snidal snidal snidal snidal snidal", "up");
        updateDirections();

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
        behavior = BottomSheet.from(bottomSheet);
        behavior.setmType("building_navigation");
        behavior.setState(BottomSheet.STATE_COLLAPSED);
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
        behavior.addBottomSheetCallback(new BottomSheet.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheet.STATE_COLLAPSED:
                        expandButton.setImageResource(R.drawable.ic_expand_less_black_24dp);
                        break;

                    case BottomSheet.STATE_EXPANDED:
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
                if (behavior.getState() == BottomSheet.STATE_COLLAPSED){
                    behavior.setState(BottomSheet.STATE_EXPANDED);
                }else{
                    behavior.setState(BottomSheet.STATE_COLLAPSED);
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

    /////////////////
    // Clearing list
    /////////////////

    /**
     * Clear List of directions and resets index back to 0
     */
    public void clearDirections(){
        completeDirectionsList.clear();
        completeDirectionsImage.clear();
        currentDirection = 0;
        updateDirections();
    }


    /**
     * Forces the Bottom Sheet to collapse
     */
    public void collapse(){
        behavior.setState(BottomSheet.STATE_COLLAPSED);
    }

    /**
     * Forces the Bottom Sheet to expand
     */
    public void expand(){
        behavior.setState(BottomSheet.STATE_EXPANDED);
    }


    /**
     * Obtains the state of the Bottom Sheet
     * 4 is expanded
     * 5 is collapsed
     * Others states should not exist
     * @return
     */
    public int getState(){
        return behavior.getState();
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

    /**
     *
     * @return TextView of Bottomsheet
     */
    public TextView getBottomSheetTextView() {
        return bottomSheetTextView;
    }

    /**
     *
     * @return List
     */
    public ListView getList() {
        return list;
    }

    /**
     *
     * @return ArrayList Adapter
     */
    public DirectionsArrayAdapter getAdapter() {
        return adapter;
    }

    /**
     * @return  ArrayList of displayed directions
     */
    public List<String> getDisplayedDirectionsList() {
        return displayedDirectionsList;
    }

    /**
     *
     * @return ArrayList of images that are displayed
     */
    public List<String> getDisplayedDirectionsImage() {
        return displayedDirectionsImage;
    }

    /**
     *
     * @return ArrayList of complete images
     */
    public List<String> getCompleteDirectionsImage() {
        return completeDirectionsImage;
    }

    /**
     *
     * @return ArrayList of complete directions
     */
    public List<String> getCompleteDirectionsList() {
        return completeDirectionsList;
    }

    /**
     *
     * @return Returns current direction index
     */
    public int getCurrentDirection() {
        return currentDirection;
    }

    /**
     *
     * @return View
     */
    @Nullable
    @Override
    public View getView() {
        return view;
    }

    /**
     *
     * @return BottomSheet view
     */
    public View getBottomSheet() {
        return bottomSheet;
    }

    /**
     *
     * @return Coordinator layout
     */
    public CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }

    /**
     *
     * @return BottomSheet Behavior
     */
    public BottomSheet getBehavior() {
        return behavior;
    }

    /**
     *
     * @return Image Button
     */
    public ImageButton getNextButton() {
        return nextButton;
    }

    /**
     *
     * @return Image Button
     */
    public ImageButton getPreviousButton() {
        return previousButton;
    }

    /**
     *
     * @return Image Button
     */
    public ImageButton getExpandButton() {
        return expandButton;
    }

}
