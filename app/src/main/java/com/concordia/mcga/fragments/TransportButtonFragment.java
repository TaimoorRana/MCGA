package com.concordia.mcga.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.concordia.mcga.activities.MainActivity;
import com.concordia.mcga.activities.R;
import com.concordia.mcga.models.Campus;

import java.util.Date;

public class TransportButtonFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton walkFAB;
    private FloatingActionButton bikeFAB;
    private FloatingActionButton carFAB;
    private FloatingActionButton publicTransportFAB;
    private FloatingActionButton shuttleFAB;
    private TextView walkTextView, bikeTextView, carTextView, publicTransportTextView, shuttleTextView;
    private Animation transport_fab_open, transport_fab_close, transport_textview_open, transport_textview_close;
    private Boolean fabExpanded = false;
    private MainActivity activity;

    private boolean shuttleAvailable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transport_button_fragment, container, false);

        shuttleAvailable = shuttleAvailable();

        //Initialize FABs
        FloatingActionButton transportExpandFAB = (FloatingActionButton) view
                .findViewById(R.id.transportExpandFAB);
        walkFAB = (FloatingActionButton) view.findViewById(R.id.walkFAB);
        bikeFAB = (FloatingActionButton) view.findViewById(R.id.bikeFAB);
        carFAB = (FloatingActionButton) view.findViewById(R.id.carFAB);
        publicTransportFAB = (FloatingActionButton) view.findViewById(R.id.publicTransportFAB);
        if(shuttleAvailable){shuttleFAB = (FloatingActionButton) view.findViewById(R.id.shuttleFAB);}

        //Initialize Text Views
        walkTextView = (TextView) view.findViewById(R.id.textViewWalk);
        bikeTextView = (TextView) view.findViewById(R.id.textViewBike);
        carTextView = (TextView) view.findViewById(R.id.textViewCar);
        publicTransportTextView = (TextView) view.findViewById(R.id.textViewPublicTransport);
        if(shuttleAvailable){shuttleTextView = (TextView) view.findViewById(R.id.textViewShuttle);}

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
        if(shuttleAvailable){shuttleFAB.setOnClickListener(this);}

        //Some test values
        setWalkTime(30);
        setBikeTime(10);
        setCarTime(30);
        setPublicTransportTime(45);
        if(shuttleAvailable){setShuttleTime(getNumberOfMinutesToNextShuttleFromCurrentTime(activity.getCurrentCampus()));}
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
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

        if(shuttleAvailable){setShuttleTime(getNumberOfMinutesToNextShuttleFromCurrentTime(activity.getCurrentCampus()));}
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

            if(shuttleAvailable){
                shuttleFAB.startAnimation(transport_fab_close);
                shuttleTextView.startAnimation(transport_textview_close);
                shuttleFAB.setClickable(false);
                }
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

            if(shuttleAvailable) {
                shuttleFAB.startAnimation(transport_fab_open);
                shuttleTextView.startAnimation(transport_textview_open);
                shuttleFAB.setClickable(true);
            }
        }
    }

    private void swapIcons(int iconId) {
        FloatingActionButton transportExpandFAB = (FloatingActionButton) getView().findViewById(R.id.transportExpandFAB);
        transportExpandFAB.setImageResource(iconId);
        transportExpandFAB.clearColorFilter();
    }

    /* Helpers */
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

    @TargetApi(24)
    private static boolean shuttleAvailable(){
        int dayOfWeek =Integer.parseInt((DateFormat.getPatternInstance("ee")).format(new Date()));
        dayOfWeek=1;
        if (dayOfWeek == 1 || dayOfWeek == 7)
            return false;
        else
            return true;
    }

    @TargetApi(24)
    public static int getNumberOfMinutesToNextShuttleFromCurrentTime(Campus c)
    {
        /* Fancy pseudo-code */
        //read present time from Android app
        //read appropriate time from SQL database
        //return the number of minutes

        int dayOfWeek = Integer.parseInt((DateFormat.getPatternInstance("ee")).format(new Date()));
        Log.d("Current day",String.valueOf(dayOfWeek));
        Log.d("Campus is", c.getName());

        dayOfWeek = 1;

        int timeToShuttle;
        if(c == Campus.LOY)
            timeToShuttle = 7;
        else
            timeToShuttle = 5;

        switch (dayOfWeek){
            case 1:
            case 7:
                //There's no shuttle on the weekends
                return -1;
            case 2:
            case 3:
            case 4:
            case 5:
                //Shuttle Monday-Thursday
                return 3*timeToShuttle;
            case 6:
                //Shuttle Friday
                return 11*timeToShuttle;
        }

    return -2; //Should not be able to reach here.
    }

    /**
     * Private method to convert from minutes to hours, minutes
     * @param minutes
     * @return
     */
    private int[] convertMinutesToHoursMinutes(int minutes){
        int[] hoursMinutes = new int[2];

        if(minutes <= 0)
            return hoursMinutes;

        if (minutes < 60) {
            hoursMinutes[1] = minutes;
        }
        else {
            hoursMinutes[0] = minutes / 60;
            hoursMinutes[1] = minutes-(hoursMinutes[0] * 60);
        }
            return hoursMinutes;
    }

    public void setWalkTime(int minutes) {
        int[] hoursMinutes = convertMinutesToHoursMinutes(minutes);
        walkTextView.setText(formatTime(hoursMinutes[0], hoursMinutes[1]));
    }

    public void setBikeTime(int minutes) {
        int[] hoursMinutes = convertMinutesToHoursMinutes(minutes);
        bikeTextView.setText(formatTime(hoursMinutes[0], hoursMinutes[1]));
    }

    public void setCarTime(int minutes) {
        int[] hoursMinutes = convertMinutesToHoursMinutes(minutes);
        carTextView.setText(formatTime(hoursMinutes[0], hoursMinutes[1]));
    }

    public void setPublicTransportTime(int minutes) {
        int[] hoursMinutes = convertMinutesToHoursMinutes(minutes);
        publicTransportTextView.setText(formatTime(hoursMinutes[0], hoursMinutes[1]));
    }

    public void setShuttleTime(int minutes) {
        Log.d("Time to shuttle", String.valueOf(minutes));
        if ( minutes < 0)
        {
            shuttleTextView.setText("Shuttle not available");
//            shuttleFAB.startAnimation(transport_fab_close);
//            shuttleTextView.startAnimation(transport_textview_close);
            shuttleFAB.setClickable(false);
            return;
        }
        int[] hoursMinutes = convertMinutesToHoursMinutes(minutes);
        shuttleTextView.setText(formatTime(hoursMinutes[0], hoursMinutes[1]));
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
