package com.concordia.mcga.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.concordia.mcga.activities.R;
import com.concordia.mcga.activities.ShuttleActivity;
import com.concordia.mcga.helperClasses.MCGATransportMode;
import com.concordia.mcga.helperClasses.OutdoorDirections;
import com.concordia.mcga.models.Campus;
import com.concordia.mcga.models.Transportation;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TransportButtonFragment extends Fragment implements View.OnClickListener {

    //Floating Action Buttons
    private FloatingActionButton transportExpandFAB;
    private FloatingActionButton walkFAB;
    private FloatingActionButton bikeFAB;
    private FloatingActionButton carFAB;
    private FloatingActionButton publicTransportFAB;
    private FloatingActionButton shuttleFAB;
    //Text Views
    private TextView walkTextView, bikeTextView, carTextView, publicTransportTextView, shuttleTextView;
    //Animations
    private Animation transport_fab_open, transport_fab_close, transport_textview_open, transport_textview_close;
    //State
    private boolean fabExpanded = false;
    private boolean shuttleVisible = true;
    private String transportType;
    private boolean carVisible = true;
    private boolean publicTransportVisible = true;
    //Outdoor directions
    private OutdoorDirections outdoorDirections;
    private Campus currentCampus = Campus.SGW;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transport_button_fragment, container, false);
        //Initialize FABs
        transportExpandFAB = (FloatingActionButton) view.findViewById(R.id.transportExpandFAB);
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

        //Set initial transport type and icon
        this.transportType = MCGATransportMode.TRANSIT;
        swapIcons(Transportation.TRANSIT.getIconID());

        return view;
    }

    @Override
    public void onClick(View clickedView) {
        toggle();
        switch (clickedView.getId()) {
            case R.id.transportExpandFAB:
                if (!isExpanded()) {
                    restorePreviousIcon();
                }
                displayAllTransportTimes();
                break;
            case R.id.walkFAB:
                this.transportType = MCGATransportMode.WALKING;
                swapIcons(Transportation.WALKING.getIconID());
                break;
            case R.id.bikeFAB:
                this.transportType = MCGATransportMode.BICYCLING;
                swapIcons(Transportation.BICYCLING.getIconID());
                break;
            case R.id.carFAB:
                this.transportType = MCGATransportMode.DRIVING;
                swapIcons(Transportation.DRIVING.getIconID());
                break;
            case R.id.publicTransportFAB:
                this.transportType = MCGATransportMode.TRANSIT;
                swapIcons(Transportation.TRANSIT.getIconID());
                break;
            case R.id.shuttleFAB:
                this.transportType = MCGATransportMode.SHUTTLE;
                swapIcons(Transportation.SHUTTLE.getIconID());
                break;
        }
        if (clickedView.getId() != R.id.transportExpandFAB) {
            outdoorDirections.setSelectedTransportMode(transportType);
            outdoorDirections.drawPathForSelectedTransportMode();
        }
    }

    /**
     * Displays time for each transportation option
     */
    protected void displayAllTransportTimes() {
        setTimeForTextView(walkTextView, MCGATransportMode.WALKING);
        setTimeForTextView(bikeTextView, MCGATransportMode.BICYCLING);
        setTimeForTextView(carTextView, MCGATransportMode.DRIVING);
        setTimeForTextView(publicTransportTextView, MCGATransportMode.TRANSIT);
        setTimeForTextView(shuttleTextView, MCGATransportMode.SHUTTLE);
    }

    private void setTimeForTextView(TextView textView, String transportType) {
        String time;
        if (transportType == MCGATransportMode.SHUTTLE) {
            int totaltime =0;
            totaltime += getMinutesToNextShuttleDeparture();
            totaltime += outdoorDirections.getHoursForTransportType(transportType)*60;
            totaltime += outdoorDirections.getMinutesForTransportType(transportType);

            time = formatTime(totaltime);
        } else {
            time = formatTime(outdoorDirections.getHoursForTransportType(transportType),
                    outdoorDirections.getMinutesForTransportType(transportType));
        }
        textView.setText(time);
    }


    /**
     * Expands and retracts the transport option buttons, performed automatically when the expand button is clicked. Use this to expand
     * and retract the transport options programatically.
     */
    public void toggle() {
        if (fabExpanded) {
            fabExpanded = false;
            walkFAB.startAnimation(transport_fab_close);
            walkTextView.startAnimation(transport_textview_close);
            walkFAB.setClickable(false);

            bikeFAB.startAnimation(transport_fab_close);
            bikeTextView.startAnimation(transport_textview_close);
            bikeFAB.setClickable(false);

            carFAB.startAnimation(transport_fab_close);
            if (carVisible) {
                carTextView.startAnimation(transport_textview_close);
                carFAB.setClickable(false);
            }

            publicTransportFAB.startAnimation(transport_fab_close);
            if (publicTransportVisible) {
                publicTransportTextView.startAnimation(transport_textview_close);
                publicTransportFAB.setClickable(false);
            }

            shuttleFAB.startAnimation(transport_fab_close);
            if (shuttleVisible) {
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
            if (carVisible) {
                carTextView.startAnimation(transport_textview_open);
                carFAB.setClickable(true);
            }

            publicTransportFAB.startAnimation(transport_fab_open);
            if (publicTransportVisible) {
                publicTransportTextView.startAnimation(transport_textview_open);
                publicTransportFAB.setClickable(true);
            }

            shuttleFAB.startAnimation(transport_fab_open);
            if (shuttleVisible) {
                shuttleTextView.startAnimation(transport_textview_open);
                shuttleFAB.setClickable(true);
            }

            swapIcons(R.drawable.ic_close_black_24dp);
        }
    }

    private void swapIcons(int iconId) {
        transportExpandFAB.setImageResource(iconId);

        if (iconId != R.drawable.ic_close_black_24dp)
            transportExpandFAB.setTag(iconId);

        transportExpandFAB.clearColorFilter();
    }

    private void restorePreviousIcon() {
        transportExpandFAB.setImageResource((Integer) transportExpandFAB.getTag());
        transportExpandFAB.clearColorFilter();
    }

    private String formatTime(int minutes){
        int hours = minutes / 60;
        minutes %= 60;
        return formatTime(hours,minutes);
    }

    private String formatTime(int hours, int minutes) {
        String time = null;
        if (hours > 0 && minutes > 0) {
            time = hours + "h " + minutes + "m";
        } else if (hours == 0) {
            time = minutes + "m";
        } else if (minutes == 0) {
            time = hours + "h";
        }
        return time;
    }

    /**
     * Disables the shuttle transport option by greying it out and disable any click activity
     *
     * @param isDisabled
     */
    public void disableShuttle(boolean isDisabled) {
        if (isDisabled) {
            this.shuttleFAB.setClickable(false);
            this.shuttleFAB.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.grey)));
            this.shuttleVisible = false;
        } else {
            this.shuttleFAB.setClickable(true);
            this.shuttleFAB.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorPrimary)));
            this.shuttleVisible = true;
        }
    }

    /**
     * Disables the car option by greying it out and disable any click activity
     *
     * @param isDisabled
     */
    public void disableCar(boolean isDisabled) {
        if (isDisabled) {
            this.carFAB.setClickable(false);
            this.carFAB.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.grey)));
            this.carVisible = false;
        } else {
            this.carFAB.setClickable(true);
            this.carFAB.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorPrimary)));
            this.carVisible = true;
        }
    }

    /**
     * Disables the public transport option by greying it out and disable any click activity
     *
     * @param isDisabled
     */
    public void disablePublicTransport(boolean isDisabled) {
        if (isDisabled) {
            this.publicTransportFAB.setClickable(false);
            this.publicTransportFAB.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.grey)));
            this.publicTransportVisible = false;
        } else {
            this.publicTransportFAB.setClickable(true);
            this.publicTransportFAB.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.colorPrimary)));
            this.publicTransportVisible = true;
        }
    }

    /**
     * Sets the value of the walk time textbox
     *
     * @param hours
     * @param minutes
     */
    public void setWalkTime(int hours, int minutes) {
        walkTextView.setText(formatTime(hours, minutes));
    }

    /**
     * Sets the value of the bike time textbox
     *
     * @param hours
     * @param minutes
     */
    public void setBikeTime(int hours, int minutes) {
        bikeTextView.setText(formatTime(hours, minutes));
    }

    /**
     * Sets the value of the car time textbox
     *
     * @param hours
     * @param minutes
     */
    public void setCarTime(int hours, int minutes) {
        carTextView.setText(formatTime(hours, minutes));
    }

    /**
     * Sets the value of the public transport time textbox
     *
     * @param hours
     * @param minutes
     */
    public void setPublicTransportTime(int hours, int minutes) {
        publicTransportTextView.setText(formatTime(hours, minutes));
    }

    /**
     * Sets the value of the concordia shuttle time textbox
     *
     * @param hours
     * @param minutes
     */
    public void setShuttleTime(int hours, int minutes) {
        shuttleTextView.setText(formatTime(hours, minutes));
    }

    /* Getters */

    /**
     * @return True if the option buttons are expanded, false otherwise
     */
    public boolean isExpanded() {
        return fabExpanded;
    }

    /**
     * @return True if the shuttle icon is active
     */
    public boolean isShuttleVisible() {
        return shuttleVisible;
    }

    /**
     * @return True if the shuttle icon is active
     */
    public boolean isCarVisible() {
        return carVisible;
    }

    /**
     * @return True if the public transport icon is active
     */
    public boolean isPublicTransportVisible() {
        return publicTransportVisible;
    }


    public FloatingActionButton getTransportExpandFAB() {
        return transportExpandFAB;
    }

    public FloatingActionButton getWalkFAB() {
        return walkFAB;
    }

    public FloatingActionButton getBikeFAB() {
        return bikeFAB;
    }

    public FloatingActionButton getCarFAB() {
        return carFAB;
    }

    public FloatingActionButton getPublicTransportFAB() {
        return publicTransportFAB;
    }

    public FloatingActionButton getShuttleFAB() {
        return shuttleFAB;
    }

    public TextView getWalkTextView() {
        return walkTextView;
    }

    public TextView getBikeTextView() {
        return bikeTextView;
    }

    public TextView getCarTextView() {
        return carTextView;
    }

    public TextView getPublicTransportTextView() {
        return publicTransportTextView;
    }

    public TextView getShuttleTextView() {
        return shuttleTextView;
    }

    public Animation getTransport_fab_open() {
        return transport_fab_open;
    }

    public Animation getTransport_fab_close() {
        return transport_fab_close;
    }

    public Animation getTransport_textview_open() {
        return transport_textview_open;
    }

    public Animation getTransport_textview_close() {
        return transport_textview_close;
    }

    public String getTransportType() {
        return transportType;
    }

    public void setOutdoorDirections(OutdoorDirections outdoorDirections) {
        this.outdoorDirections = outdoorDirections;
    }

    public int getMinutesToNextShuttleDeparture() {
        String[][] shuttleSchedule = ShuttleActivity.getShuttleSchedule();

        int currentDay = Integer.valueOf(new SimpleDateFormat("u").format(new Date()));
        int currentTime = getMinutesFromTimeString(new SimpleDateFormat("HH:mm").format(new Date()));

        int shuttleColumnIndex = getShuttleColumnIndex(currentDay);

        int timeToNextShuttle = -1;

        if (isShuttleAvailable(currentDay)) {
            timeToNextShuttle = calculateTimeToNextShuttle(shuttleSchedule,shuttleColumnIndex, currentTime);
        }

        if (timeToNextShuttle < 0 || !isShuttleAvailable(currentDay)) {
            final int TOTAL_MINUTES_IN_A_DAY = 60 * 24;
            int daysToNextShuttleService = calculeNumberOfWholeDaysToNextShuttle(currentDay);
            shuttleColumnIndex = getNextDayColumnIndex(currentDay, shuttleColumnIndex);

            timeToNextShuttle = (TOTAL_MINUTES_IN_A_DAY - currentTime) + (TOTAL_MINUTES_IN_A_DAY * daysToNextShuttleService) + getMinutesFromTimeString(shuttleSchedule[0][shuttleColumnIndex]);
        }
        Log.d("Adrianna next shuttle", String.valueOf(timeToNextShuttle));
        return timeToNextShuttle;
    }

    private int getNextDayColumnIndex(int currentDay, int currentIndex) {
        int sqlTableColumnIndex = currentIndex;

        //Monday to Thursday shift one to the right
        if (currentDay > 0 && currentDay < 5)
            sqlTableColumnIndex += 1;
        else
            sqlTableColumnIndex -= 1;

        return sqlTableColumnIndex;
    }

    /**
     * Returns the appropriate column index for the shuttle,
     * based on currentDay and the current Campus.
     *
     * @param currentDay
     * @return
     */
    private int getShuttleColumnIndex(int currentDay) {
        int sqlTableColumnIndex = 0;

        if (currentCampus == Campus.LOY)
            sqlTableColumnIndex += 2; //LOYtoSGW are columns 2,3

        if (currentDay > 4)
            sqlTableColumnIndex += 1; //Friday are columns 1,3

        return sqlTableColumnIndex;
    }

    /**
     * Shuttle service is only available from Monday to Friday
     */
    private boolean isShuttleAvailable(int day) {
        return (day > 0 && day < 6);
    }

    /**
     * Input "HH:mm"
     *
     * @param hourminute
     * @return minutes
     */
    private int getMinutesFromTimeString(String hourminute) {
        String[] tokens = hourminute.split(":");
        if (tokens[0].contains("-")) {
            return -1;
        } else {
            return Integer.valueOf(tokens[0]) * 60 + Integer.valueOf(tokens[1]);
        }
    }

    private int calculateTimeToNextShuttle(String[][] scheduleMinutes, int columnIndex, int currentTime) {
        //Scans the list from the beginning, for current day
        for (int rowIndex=0; rowIndex < scheduleMinutes.length; rowIndex++) {
            int scheduleSlotInMinutes = getMinutesFromTimeString(scheduleMinutes[rowIndex][columnIndex]);
            //There are "-" in the schedule for empty slots
            if (scheduleSlotInMinutes == -1)
            {
                continue;
            }

            int timeDifference = scheduleSlotInMinutes - currentTime;
            if (timeDifference > 0) {
                return timeDifference;
            }
        }
        //Current time is after last shuttle for day has departed
        return -1;
    }

    private int calculeNumberOfWholeDaysToNextShuttle(int currentDay) {

        //This should never occur.
        if (currentDay < 0 || currentDay > 7)
            return -1;

        //Friday, have to wait for Saturday, Sunday
        if (currentDay == 5)
            return 2;

        //Saturday, have to wait for Sunday
        if (currentDay == 6)
            return 1;

        //Shuttle service available the following day
        return 0;
    }

    public void setCampus(Campus c) {
        currentCampus = c;
    }
}
