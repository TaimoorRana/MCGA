package com.concordia.mcga.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.concordia.mcga.activities.R;

public class TransportButtonFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton walkFAB;
    private FloatingActionButton bikeFAB;
    private FloatingActionButton carFAB;
    private FloatingActionButton publicTransportFAB;
    private FloatingActionButton shuttleFAB;
    private TextView walkTextView, bikeTextView, carTextView, publicTransportTextView, shuttleTextView;
    private Animation transport_fab_open, transport_fab_close, transport_textview_open, transport_textview_close;
    private Boolean fabExpanded = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transport_button_fragment, container, false);
        //Initialize FABs
        FloatingActionButton transportExpandFAB = (FloatingActionButton) view
            .findViewById(R.id.transportExpandFAB);
        walkFAB = (FloatingActionButton) view.findViewById(R.id.walkFAB);
        bikeFAB = (FloatingActionButton) view.findViewById(R.id.bikeFAB);
        carFAB = (FloatingActionButton) view.findViewById(R.id.carFAB);
        publicTransportFAB = (FloatingActionButton) view.findViewById(R.id.publicTransportFAB);
        shuttleFAB = (FloatingActionButton) view.findViewById(R.id.shuttleFAB);

        //Initialize Text Views
        walkTextView = (TextView) view.findViewById(R.id.textViewWalk);
        bikeTextView = (TextView) view.findViewById(R.id.textViewBike);
        carTextView = (TextView) view.findViewById(R.id.textViewCar);
        publicTransportTextView = (TextView) view.findViewById(R.id.textViewPublicTransport);
        shuttleTextView = (TextView) view.findViewById(R.id.textViewShuttle);

        //Initialize Animations
        transport_fab_open = AnimationUtils.loadAnimation(getContext(), R.anim.transport_fab_open);
        transport_fab_close = AnimationUtils.loadAnimation(getContext(), R.anim.transport_fab_close);
        transport_textview_open = AnimationUtils.loadAnimation(getContext(), R.anim.transport_textview_open);
        transport_textview_close = AnimationUtils.loadAnimation(getContext(), R.anim.transport_textview_close);

        //Register Click Listeners
        transportExpandFAB.setOnClickListener(this);
        walkFAB.setOnClickListener(this);
        bikeFAB.setOnClickListener(this);
        carFAB.setOnClickListener(this);
        publicTransportFAB.setOnClickListener(this);
        shuttleFAB.setOnClickListener(this);

        //Some test values
        setWalkTime(1, 30);
        setBikeTime(1, 0);
        setCarTime(0, 30);
        setPublicTransportTime(0, 45);
        setShuttleTime(0, 30);

        return view;
    }

    @Override
    public void onClick(View clickedView) {
        switch (clickedView.getId()) {
            case R.id.transportExpandFAB:
                expandFAB();
                break;
            case R.id.walkFAB:
                swapIcons(R.drawable.ic_directions_walk_black_24dp);
                break;
            case R.id.bikeFAB:
                swapIcons(R.drawable.ic_directions_bike_black_24dp);
                break;
            case R.id.carFAB:
                swapIcons(R.drawable.ic_directions_car_black_24dp);
                break;
            case R.id.publicTransportFAB:
                swapIcons(R.drawable.ic_directions_transit_black_24dp);
                break;
            case R.id.shuttleFAB:
                swapIcons(R.drawable.ic_stingers_icon);
                break;
        }
    }

    public void expandFAB() {
        if (fabExpanded) {
            fabExpanded = false;
            walkFAB.startAnimation(transport_fab_close);
            walkTextView.startAnimation(transport_textview_close);
            walkFAB.setClickable(false);

            bikeFAB.startAnimation(transport_fab_close);
            bikeTextView.startAnimation(transport_textview_close);
            bikeFAB.setClickable(false);

            carFAB.startAnimation(transport_fab_close);
            carTextView.startAnimation(transport_textview_close);
            carFAB.setClickable(false);

            publicTransportFAB.startAnimation(transport_fab_close);
            publicTransportTextView.startAnimation(transport_textview_close);
            publicTransportFAB.setClickable(false);

            shuttleFAB.startAnimation(transport_fab_close);
            shuttleTextView.startAnimation(transport_textview_close);
            shuttleFAB.setClickable(false);
        } else {
            fabExpanded = true;
            walkFAB.startAnimation(transport_fab_open);
            walkTextView.startAnimation(transport_textview_open);
            walkFAB.setClickable(true);

            bikeFAB.startAnimation(transport_fab_open);
            bikeTextView.startAnimation(transport_textview_open);
            bikeFAB.setClickable(true);

            carFAB.startAnimation(transport_fab_open);
            carTextView.startAnimation(transport_textview_open);
            carFAB.setClickable(true);

            publicTransportFAB.startAnimation(transport_fab_open);
            publicTransportTextView.startAnimation(transport_textview_open);
            publicTransportFAB.setClickable(true);

            shuttleFAB.startAnimation(transport_fab_open);
            shuttleTextView.startAnimation(transport_textview_open);
            shuttleFAB.setClickable(true);
        }
    }

    private void swapIcons(int iconId) {
        FloatingActionButton transportExpandFAB = (FloatingActionButton) getView().findViewById(R.id.transportExpandFAB);
        transportExpandFAB.setImageResource(iconId);
        transportExpandFAB.clearColorFilter();
    }

    private String formatTime(int hours, int minutes) {
        String time = null;
        if (hours > 0) {
            time =  hours + "h" + " " + minutes + "min";
        } else if (hours == 0) {
            time = minutes + "min";
        } else if (minutes == 0) {
            time = hours + "h";
        }
        return time;
    }

    public void setWalkTime(int hours, int minutes) {
        walkTextView.setText(formatTime(hours, minutes));
    }

    public void setBikeTime(int hours, int minutes) {
        bikeTextView.setText(formatTime(hours, minutes));
    }

    public void setCarTime(int hours, int minutes) {
        carTextView.setText(formatTime(hours, minutes));
    }

    public void setPublicTransportTime(int hours, int minutes) {
        publicTransportTextView.setText(formatTime(hours, minutes));
    }

    public void setShuttleTime(int hours, int minutes) {
        shuttleTextView.setText(formatTime(hours, minutes));
    }


    /* Getters */

    public TextView getBikeTextView() {
        return bikeTextView;
    }

    public TextView getCarTextView() {
        return carTextView;
    }

    public TextView getShuttleTextView() {
        return shuttleTextView;
    }

    public TextView getWalkTextView() {
        return walkTextView;
    }

    public TextView getPublicTransportTextView() {
        return publicTransportTextView;
    }


    public Boolean isExpanded() {
        return fabExpanded;
    }
}
