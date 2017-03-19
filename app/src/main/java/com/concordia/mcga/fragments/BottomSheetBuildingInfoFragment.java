package com.concordia.mcga.fragments;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.concordia.mcga.activities.R;
import com.concordia.mcga.adapters.BuildingInformationArrayAdapter;
import com.concordia.mcga.lib.BuildingBottomSheetInfo;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;



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

    // Arraylist containing all images
    private List<String[]> rowImages = new ArrayList<String[]>();

    // 4 images per row
    private final int IMAGES_PER_ROW = 4;

    // Main view
    private View view = null;

    // Bottom sheet view
    private CoordinatorLayout coordinatorLayout = null;
    private View bottomSheet = null;

    private Button viewSwitchButton;

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



    public Button getViewSwitchButton() {
        return viewSwitchButton;
    }

    /**
     * Buttons are assigned their corresponding ID from thhe layout.xml file
     */
    private void setupButtons(){
        expandButton = (ImageButton) view.findViewById(R.id.expandButton);
        expandButton.setImageResource(R.drawable.ic_expand_less_black_24dp);
        expandButton.setOnClickListener(this);
        viewSwitchButton = (Button) view.findViewById(R.id.viewSwitchButton);
    }

    /**
     * Bottom Sheet behavior is assigned
     */
    private void setupBottomSheetBehavior(){
        behavior = BuildingBottomSheetInfo.from(bottomSheet);
        behavior.setmType("building_information");
        behavior.setState(BuildingBottomSheetInfo.STATE_HIDDEN);
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
        adapter = new BuildingInformationArrayAdapter(getActivity().getApplicationContext(), rowImages);
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
     * Obtains the state of the Bottom Sheet
     * 4 is expanded
     * 5 is collapsed
     * Others states should not exist
     * @return
     */
    public int getState(){
        return behavior.getState();
    }

    /**
     * Updates the row of images associate to the building information. Works
     * similarly to a unit of Work
     * The array adapter cannot take a String array as a value
     */
    public void updateImageRow(){
        adapter.notifyDataSetChanged();
    }

    /**
     * Clears array containing all image information
     */
    public void clear(){
        rowImages.clear();
        updateImageRow();
    }

    /**
     * Removes a row from the array list
     * Catches out of bounds exception
     * @param index
     */
    public void remove(int index){
        try {
            rowImages.remove(index);
        }
        catch (IndexOutOfBoundsException e){
            Log.e(TAG, "Exception: "+ Log.getStackTraceString(e));
        }
    }


    /**
     * Adds an image to the array.. If array passed is greater than 4
     * then catched exception
     * @param image
     */
    public void addImages(String[] image){
        try {
            String[] temp = new String[IMAGES_PER_ROW];
            for (int i = 0; i < image.length; i++){
                temp[i] = image[i];
            }
            rowImages.add(temp);
        }
        catch(ArrayIndexOutOfBoundsException e){
            Log.e(TAG, "Exception: "+ Log.getStackTraceString(e));
        }
    }

    /**
     * Display hall buildings POI
     */
    public void displayHBuildingAssociations(){
        String temp[] = new String[IMAGES_PER_ROW];
        temp[0] = "asfa";
        temp[1] = "sasu";
        temp[2] = "space";
        temp[3] = "lifting";
        addImages(temp);
        updateImageRow();

        temp[0] = "hive";
        temp[1] = "csu";
        temp[2] = "ccsu";
        temp[3] = "scs";
        addImages(temp);
        updateImageRow();

    }

    /**
     * Display MB POI
     */
    public void displayMBBuildingAssociations(){
        String temp[] = new String[IMAGES_PER_ROW];
        temp[0] = "jmac";
        temp[1] = "jmiba";
        temp[2] = "jmma";
        temp[3] = "jmas";
        addImages(temp);
        updateImageRow();


        temp[0] = "fisa";
        temp[1] = "jmucc";
        temp[2] = "jsec";
        temp[3] = "jmsb_case_comp";
        addImages(temp);
        updateImageRow();
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


    ////////////////////////////////////
    // GETTERS
    ////////////////////////////////////

    /***
     * @return Bottom Sheet Behavior
     */
    public BuildingBottomSheetInfo getBehavior() {
        return behavior;
    }

    /**
     *
     * @return The View of the layout inflater
     */
    public View getBottomSheet() {
        return bottomSheet;
    }

    /**
     *
     * @return Expand button
     */

    public ImageButton getExpandButton() {
        return expandButton;
    }

    /**
     *
     * @return Bottom Sheet Title
     */
    public TextView getBottom_sheet_title() {
        return bottom_sheet_title;
    }

    /**
     *
     * @return Address
     */
    public TextView getAddress() {
        return address;
    }

    /**
     *
     * @return Closing Time
     */
    public TextView getClosingTime() {
        return closingTime;
    }

    /**
     *
     * @return Opening Time
     */
    public TextView getOpeningTime() {
        return openingTime;
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
    public BuildingInformationArrayAdapter getAdapter() {
        return adapter;
    }


    /**
     *
     * @return ArrayList
     */
    public List<String[]> getRowImages() {
        return rowImages;
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
     * @return Coordinator Layout
     */
    public CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }
}
