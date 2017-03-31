package com.concordia.mcga.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.concordia.mcga.activities.R;
import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.Campus;
import com.concordia.mcga.models.POI;
import com.concordia.mcga.models.Room;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * This adapter searches through both campuses to find POIs (for now, buildings) that match a query.
 */
public class POISearchAdapter extends BaseExpandableListAdapter {
    private Context context;

    private Campus sgw;
    private Campus loyola;

    private List<POI> locationList;
    private List<Building> sgwFilteredList;
    private List<Building> loyolaFilteredList;
    private List<Room> roomFilteredList;

    public final POI locationPlaceholder = new POI(new LatLng(0,0), "Placeholder");
    public static final int MY_LOCATION_GROUP_POSITION = 0;

    private List<List> masterList;

    /**
     * The constructor initializes the empty lists that store the currently queried POIs
     * @param context View context to search
     */
    public POISearchAdapter(Context context, Campus sgw, Campus loyola) {
        this.context = context;

        this.sgw = sgw;
        this.loyola = loyola;

        this.sgwFilteredList = new ArrayList<>();
        this.loyolaFilteredList = new ArrayList<>();
        this.roomFilteredList = new ArrayList<>();
        this.locationList = new LinkedList<>();

        locationList.add(locationPlaceholder);

        this.masterList = new ArrayList<>();
    }

    /**
     * Evaluates the number of campuses active based on the current queried POIs
     * @return The number of active campuses
     */
    @Override
    public int getGroupCount() {
        return masterList.size();
    }

    /**
     * This overrides the BaseExpandableList function to get the number of children (POI) entries
     * @param childPosition Campus index
     * @return Number of POIs specific to the passed campus
     */
    @Override
    public int getChildrenCount(int childPosition) {
        return masterList.get(childPosition).size();
    }

    /**
     * This overrides the BaseExpandableList function to get the POI group specified by an index.
     * @param groupPosition Active campus index
     * @return The campus specified by the index
     */
    @Override
    public Object getGroup(int groupPosition) {
        return masterList.get(groupPosition);
    }

    /**
     * This overrides the BaseExpandableList function to get an actual menu item - in this case, POI
     * @param groupPosition The active campus index (see getGroup)
     * @param childPosition The POI index within the specified campus
     * @return The POI that exists according to the filtered query
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return masterList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * Inflates and returns the view associated with the index-specified campus or POI group
     * @param groupPosition POIgroup index
     * @param b Whether the group is collapsed or expanded
     * @param view The old view to reuse, if possible
     * @param viewGroup The parent that this view will be attached to
     * @return The inflated POI group view
     */
    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
        String title;
        int resId;

        List group = (List) getGroup(groupPosition);
        if (group == sgwFilteredList) {
            title = "Sir George Williams";
            resId = R.mipmap.ic_sgw_campus;
        } else if (group == loyolaFilteredList) {
            title = "Loyola";
            resId = R.mipmap.ic_loy_campus;
        } else if (group == roomFilteredList){
            title = "Classrooms";
            resId = R.mipmap.ic_classroom;
        } else { // if (group == locationList) {
            title = "My Location";
            resId = R.drawable.ic_my_location_black_24dp;
        }

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.poi_search_group_row, null);
        }

        CircleImageView campusImage = (CircleImageView) view.findViewById(R.id.groupImage);
        campusImage.setImageResource(resId);

        TextView campusRow = (TextView) view.findViewById(R.id.groupText);
        campusRow.setText(title);

        return view;
    }

    /**
     * Inflates and returns the view associated with the POI list item. For now, a building name
     * @param groupPosition The group/campus index
     * @param childPosition The child/POI index
     * @param b Whether the child is the last one in the group
     * @param view Existing view to reuse if possible
     * @param viewGroup The parent group this view will be attached to
     * @return The POI menu item (building name)
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view,
                             ViewGroup viewGroup) {
        POI poi = (POI)getChild(groupPosition, childPosition);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

        if (poi == null || poi == locationPlaceholder) {
            view = layoutInflater.inflate(R.layout.poi_search_empty_row, null);
        } else {
            if (view == null) {
                view = layoutInflater.inflate(R.layout.poi_search_child_row, null);
            }

            TextView buildingRow = (TextView) view.findViewById(R.id.childText);
            int backgroundColor = (childPosition % 2) == 0 ? Color.WHITE : Color.LTGRAY;
            view.setBackgroundColor(backgroundColor);
            buildingRow.setText(poi.getName());
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    /**
     * This is the actual filter to select the POIs specified by the passed string.
     * The POIs that are determined to meet the query criteria are included in the filtered lists
     * (see constructor.)
     * @param query The string query to match POIs against
     */
    public void filterData(String query) {
        query = query.toLowerCase();
        sgwFilteredList.clear();
        loyolaFilteredList.clear();
        roomFilteredList.clear();
        masterList.clear();

        if (!query.isEmpty()) {
            // Always add a group for the "locate me" list item. Using a dummy list
            masterList.add(locationList);

            // Get matching SGW buildings
            if (sgw.getName().toLowerCase().contains(query) ||
                    sgw.getShortName().toLowerCase().contains(query)) {
                sgwFilteredList.addAll(sgw.getBuildings());
            } else {
                List<Building> sgwList = new ArrayList<>();
                for (Building building : sgw.getBuildings()) {
                    if (building.getName().toLowerCase().contains(query) ||
                            building.getShortName().toLowerCase().contains(query)) {
                        sgwList.add(building);
                    }
                }
                sgwFilteredList.addAll(sgwList);
            }

            // Get matching Loyola buildings
            if (loyola.getName().toLowerCase().contains(query) ||
                    loyola.getShortName().toLowerCase().contains(query)) {
                loyolaFilteredList.addAll(loyola.getBuildings());
            } else {
                List<Building> loyList = new ArrayList<>();
                for (Building building : loyola.getBuildings()) {
                    if (building.getName().toLowerCase().contains(query) ||
                            building.getShortName().toLowerCase().contains(query)) {
                        loyList.add(building);
                    }
                }
                loyolaFilteredList.addAll(loyList);
            }

            // Get matching rooms
            List<Room> roomList = new ArrayList<>();
            for (Building building : loyola.getBuildings()) {
                for (Room room : building.getRooms()) {
                    if (room.getName().toLowerCase().contains(query)) {
                        roomList.add(room);
                    }
                }
            }
            for (Building building : sgw.getBuildings()) {
                for (Room room : building.getRooms()) {
                    if (room.getName().toLowerCase().contains(query)) {
                        roomList.add(room);
                    }
                }
            }
            roomFilteredList.addAll(roomList);
        }

        if (!sgwFilteredList.isEmpty()) {
            masterList.add(sgwFilteredList);
        }
        if (!loyolaFilteredList.isEmpty()) {
            masterList.add(loyolaFilteredList);
        }
        if (!roomFilteredList.isEmpty()) {
            masterList.add(roomFilteredList);
        }
        notifyDataSetChanged();
    }
}

