package com.concordia.mcga.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.concordia.mcga.activities.R;
import com.concordia.mcga.lib.BottomSheetBuildingInfo;

/**
 * Created by Charmander on 3/5/2017.
 */

public class BottomSheetBuildingInfoFragment extends Fragment {


    // Bottomsheet
    BottomSheetBuildingInfo behavior;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // View Inflater
        View view = inflater.inflate(R.layout.building_information_fragment, container, false);


        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorlayout);
        View bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBuildingInfo.from(bottomSheet);


        // Set bottom sheet to collapsed
        //behavior.setState(BottomSheetBuildingInfo.STATE_HIDDEN);
        behavior.setState(BottomSheetBuildingInfo.STATE_COLLAPSED);

        return view;
    }

    public void collapse(){
        behavior.setState(BottomSheetBuildingInfo.STATE_COLLAPSED);
    }

    public void expand(){
        behavior.setState(BottomSheetBuildingInfo.STATE_EXPANDED);
    }

}
