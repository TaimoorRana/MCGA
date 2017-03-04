package com.concordia.mcga.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.concordia.mcga.activities.R;
import com.concordia.mcga.adapters.MySimpleArrayAdapter;

import java.util.ArrayList;

/**
 * Created by Charmander on 2/25/2017.
 */

public class BottomSheetFragment extends Fragment implements View.OnClickListener{
    TextView bottomSheetTextView;
    private ListView list;
    private MySimpleArrayAdapter simpleAdapter;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList =  new ArrayList<String>();
    private ArrayList<String> completeDirectionsList = new ArrayList<String>();
    private int currentDirection = 0;
    private Button nextButton, previousButton;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // View Inflater
        View view = inflater.inflate(R.layout.bottom_sheet_content, container, false);

        // UI Elements
        bottomSheetTextView = (TextView) view.findViewById(R.id.bottom_sheet_title);
        bottomSheetTextView.setText("Directions");
        list = (ListView) view.findViewById(R.id.list1);
        nextButton = (Button) view.findViewById(R.id.nextButton);
        previousButton = (Button) view.findViewById(R.id.previousButton);

        // Adapter: You need three parameters 'the context, id of the layout (it will be where the data is shown),
        // and the array that contains the data
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile", "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2" };
        simpleAdapter = new MySimpleArrayAdapter(getActivity().getApplicationContext(), values);
        //adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.list_text, R.id.customListView, arrayList);
        //list.setAdapter(adapter);
        list.setAdapter(simpleAdapter);

        // Set listeners
        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);


        return view;
    }

    ///////////////////////////////////////////////////////
    // Button clicks for following or previous directions
    ///////////////////////////////////////////////////////


    // Overloaded method for button clicks
    public void onClick(View clickedView) {
        String direction;
        switch (clickedView.getId()) {
            case R.id.nextButton:
                nextDirection();
                break;

            case R.id.previousButton:
                previousDirection();
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
    public void addDirection(String direction){
        completeDirectionsList.add(direction);
    }

    public void removeDirection(int index){
        completeDirectionsList.remove(index);
    }

    // Append a list of directions to the current ones
    public void addDirectionsList(ArrayList<String> directions){
        for (int i =0 ; i < directions.size(); i ++){
            completeDirectionsList.add(directions.get(i));
        }
    }

    /////////////////
    // Clearing list
    /////////////////

    // Reset list and puts current index back to 0
    public void clearDirections(){
        completeDirectionsList.clear();
        currentDirection = 0;
        updateDirections();
    }


    ///////////////////////////////////////////////////////////////////
    // Updating current directions. Can be the previous or next one
    ///////////////////////////////////////////////////////////////////


    private void nextDirection(){
        if (arrayList.size() > 0) {
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
        arrayList.clear();
        for (int i = currentDirection + 1; i < completeDirectionsList.size(); i++){
            arrayList.add(completeDirectionsList.get(i));
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
