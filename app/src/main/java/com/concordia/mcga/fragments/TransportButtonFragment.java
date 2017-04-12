package com.concordia.mcga.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.concordia.mcga.activities.MainActivity;
import com.concordia.mcga.activities.R;
import com.concordia.mcga.activities.ShuttleActivity;
import com.concordia.mcga.helperClasses.MCGADayOfWeek;
import com.concordia.mcga.helperClasses.MCGATransportMode;
import com.concordia.mcga.utilities.pathfinding.OutdoorDirections;
import com.concordia.mcga.models.Campus;
import com.concordia.mcga.models.Transportation;

import java.util.Calendar;


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

    final static int MINUTES_IN_AN_HOUR = 60;

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

        setGroupVisible(false);

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
            MainActivity activity = ((MainActivity) getActivity());

            // Tell the main activity to generate directions
            ((MainActivity) getActivity()).generateDirections(activity.getLocation(), activity.getDestination(), transportType);
            activity.clearPaths();
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

    /**
     * This method sets the time for the appropriate TextView object.
     *
     * @param textView
     * @param transportType
     */
    private void setTimeForTextView(TextView textView, String transportType) {
        String time;
        if (transportType == MCGATransportMode.SHUTTLE) {
            int totaltime = 0;
            totaltime += getMinutesToNextShuttleDeparture();
            totaltime += outdoorDirections.getHoursForTransportType(transportType) * MINUTES_IN_AN_HOUR;
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

    private String formatTime(int minutes) {
        int hours = minutes / MINUTES_IN_AN_HOUR;
        minutes %= MINUTES_IN_AN_HOUR;
        return formatTime(hours, minutes);
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

    /**
     * This method calculates and returns the time in minutes to the next Concordia Shuttle departure.
     * It takes into consideration, the current time, with day of the week, as well as the
     * current campus location. Concordia Shuttle service is only available from Monday-Friday,
     * with the Friday having a different schedule than the rest of the week.
     *
     * @return
     */
    public int getMinutesToNextShuttleDeparture() {
        String[][] shuttleSchedule = ShuttleActivity.getShuttleSchedule();
        int currentDay = Calendar.getInstance().getTime().getDay();
        int currentTime = Calendar.getInstance().getTime().getHours() * MINUTES_IN_AN_HOUR + Calendar.getInstance().getTime().getMinutes();

        int timeToNextShuttle = -1;
        if (isShuttleAvailable(currentDay)) {
            timeToNextShuttle = calculateTimeToNextShuttle(shuttleSchedule, getShuttleColumnIndex(currentDay), currentTime);
        }

        if (timeToNextShuttle < 0 || !isShuttleAvailable(currentDay)) {
            final int TOTAL_MINUTES_IN_A_DAY = MINUTES_IN_AN_HOUR * 24;
            int daysToNextShuttleService = calculateNumberOfWholeDaysToNextShuttle(currentDay);
            timeToNextShuttle = (TOTAL_MINUTES_IN_A_DAY - currentTime) +
                    (TOTAL_MINUTES_IN_A_DAY * daysToNextShuttleService) +
                    getTimeForFirstShuttleService(currentDay, getShuttleColumnIndex(currentDay), shuttleSchedule);
        }
        return timeToNextShuttle;
    }

    /**
     * This will return the time in minutes for the first shuttle service
     * available for the current day.
     *
     * @param currentDay
     * @param currentShuttleColumnIndex
     * @param shuttleSchedule
     * @return
     */
    private int getTimeForFirstShuttleService(int currentDay, int currentShuttleColumnIndex, String[][] shuttleSchedule) {
        int followingDayColumnIndex = getNextDayColumnIndex(currentDay, currentShuttleColumnIndex);
        int nextShuttleTime = -1;
        for (int rowIndex = 0; rowIndex < shuttleSchedule.length && nextShuttleTime < 0; rowIndex++) {
            nextShuttleTime = getMinutesFrom_HHmm_TimeString(shuttleSchedule[rowIndex][followingDayColumnIndex]);
        }

        return nextShuttleTime;
    }

    /**
     * Given a day and an index, returns the correct index out
     * that we would need for the following day.
     *
     * @param currentDay
     * @param currentIndex
     * @return
     */
    private int getNextDayColumnIndex(int currentDay, int currentIndex) {
        int sqlTableColumnIndex = currentIndex;

        //Monday to Thursday shift one to the right
        if (currentDay >= MCGADayOfWeek.MONDAY && currentDay < MCGADayOfWeek.FRIDAY)
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

        if (currentDay > MCGADayOfWeek.THURSDAY || currentDay < MCGADayOfWeek.MONDAY)
            sqlTableColumnIndex += 1; //Friday, Weekends are columns 1,3

        return sqlTableColumnIndex;
    }

    /**
     * Shuttle service is only available from Monday to Friday
     */
    private boolean isShuttleAvailable(int day) {
        return (day >= MCGADayOfWeek.MONDAY && day <= MCGADayOfWeek.FRIDAY);
    }

    /**
     * This is a parsing method to extract time in minutes from a String.
     *
     * @param hoursColonMinutes "HH:mm"
     * @return minutes
     */
    private int getMinutesFrom_HHmm_TimeString(String hoursColonMinutes) {
        String[] tokens = hoursColonMinutes.split(":");
        if (tokens[0].contains("-")) {
            return -1;
        } else {
            return Integer.valueOf(tokens[0]) * MINUTES_IN_AN_HOUR + Integer.valueOf(tokens[1]);
        }
    }

    /**
     * Calculates the time in minutes until the next Concordia Shuttle
     * departure. The method iterates over the list and tries to find a
     * match for the next available departure.
     *
     * @param shuttleSchedule
     * @param columnIndex
     * @param currentTimeInMinutes
     * @return returns the time until the next departure, or -1 if reached end of list
     */
    private int calculateTimeToNextShuttle(String[][] shuttleSchedule, int columnIndex, int currentTimeInMinutes) {
        //Scans the list from the beginning, for current day
        for (int rowIndex = 0; rowIndex < shuttleSchedule.length; rowIndex++) {
            int scheduleSlotInMinutes = getMinutesFrom_HHmm_TimeString(shuttleSchedule[rowIndex][columnIndex]);
            //There are "-" in the schedule for empty slots
            if (scheduleSlotInMinutes == -1) {
                continue;
            }

            int timeDifference = scheduleSlotInMinutes - currentTimeInMinutes;
            if (timeDifference > 0) {
                return timeDifference;
            }
        }
        //Current time is after last shuttle for day has departed
        return -1;
    }

    /**
     * Calculates the number of whole days until the
     * next day that has Concordia Shuttle Service.
     *
     * @param currentDay
     * @return
     */
    private int calculateNumberOfWholeDaysToNextShuttle(int currentDay) {

        //This should never occur.
        if (currentDay < MCGADayOfWeek.SUNDAY || currentDay > MCGADayOfWeek.SATURDAY)
            return -1;

        //Friday, have to wait for Saturday, Sunday
        if (currentDay == MCGADayOfWeek.FRIDAY)
            return 2;

        //Saturday, have to wait for Sunday
        if (currentDay == MCGADayOfWeek.SATURDAY)
            return 1;

        //Shuttle service available the following day
        return 0;
    }

    public void setCampus(Campus c) {
        currentCampus = c;
    }

    public void setGroupVisible(boolean isVisible) {
        int visibility = (isVisible) ? View.VISIBLE : View.GONE;

        if (fabExpanded) {
            toggle();
        }

        transportExpandFAB.setVisibility(visibility);
    }
}
