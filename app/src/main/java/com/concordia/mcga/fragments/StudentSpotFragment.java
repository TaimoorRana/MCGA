package com.concordia.mcga.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.concordia.mcga.activities.R;
import com.concordia.mcga.adapters.StudentSpotAdapter;
import com.concordia.mcga.factories.StudentSpotFactory;
import com.concordia.mcga.models.StudentSpot;

import java.util.List;

public class StudentSpotFragment extends Fragment {
    private List<StudentSpot> spots;
    private StudentSpotAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate layout view
        View view = inflater.inflate(R.layout.student_spot_fragment, container, false);

        // Populate adapter
        Activity activity = getActivity();
        spots = StudentSpotFactory.getInstance().getStudentSpots(activity.getResources());
        adapter = new StudentSpotAdapter(activity, spots);

        ListView list = (ListView) view.findViewById(R.id.spotList);
        list.setAdapter(adapter);

        return view;
    }
}