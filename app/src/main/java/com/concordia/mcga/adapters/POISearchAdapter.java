package com.concordia.mcga.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.concordia.mcga.activities.R;
import com.concordia.mcga.models.Building;
import com.concordia.mcga.models.Campus;
import com.concordia.mcga.models.POI;

import java.util.ArrayList;

public class POISearchAdapter extends BaseExpandableListAdapter {
    private Context context;

    public static final int SGW_INDEX = 0;
    public static final int LOYOLA_INDEX = 1;

    private ArrayList<Building> sgwFilteredList;
    private ArrayList<Building> loyolaFilteredList;

    public POISearchAdapter(Context context) {
        this.context = context;

        this.sgwFilteredList = new ArrayList<>();
        this.loyolaFilteredList = new ArrayList<>();
    }

    @Override
    public int getGroupCount() {
        return 2;
    }

    @Override
    public int getChildrenCount(int i) {
        if (i == SGW_INDEX) {
            return sgwFilteredList.size();
        } else if (i == LOYOLA_INDEX) {
            return loyolaFilteredList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int i) {
        if (i == SGW_INDEX) {
            return Campus.SGW;
        } else if (i == LOYOLA_INDEX) {
            return Campus.LOY;
        } else {
            return null;
        }
    }

    @Override
    public Object getChild(int i, int i1) {
        if (i == SGW_INDEX) {
            return sgwFilteredList.get(i1);
        } else if (i == LOYOLA_INDEX) {
            return loyolaFilteredList.get(i1);
        } else {
            return null;
        }
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    public boolean getGroupIsEmpty(int index) {
        if (index == SGW_INDEX) {
            return sgwFilteredList.isEmpty();
        } else if (index == LOYOLA_INDEX) {
            return loyolaFilteredList.isEmpty();
        } else {
            return false;
        }
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        Campus campus = (Campus)getGroup(i);

        if (campus == null) {
            return null;
        }

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.poi_search_group_row, null);
        }

        TextView campusRow = (TextView) view.findViewById(R.id.campusHeading);
        campusRow.setText(campus.getName());

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        Building building = (Building)getChild(i, i1);

        if (building == null) {
            return null;
        }

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.poi_search_child_row, null);
        }

        TextView buildingRow = (TextView) view.findViewById(R.id.buildingRow);
        buildingRow.setText(building.getName());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public void filterData(String query, boolean clear) {
        query = query.toLowerCase();
        sgwFilteredList.clear();
        loyolaFilteredList.clear();

        if (clear == false) {
            if (query.isEmpty()) {
                //sgwFilteredList.addAll(Campus.SGW.getBuildings());
                //loyolaFilteredList.addAll(Campus.LOY.getBuildings());
            } else {
                // Temporary buffers. Alternative would be to use synchronize
                ArrayList<Building> sgwList = new ArrayList<>();
                ArrayList<Building> loyList = new ArrayList<>();

                for (Building building : Campus.SGW.getBuildings()) {
                    if (building.getName().toLowerCase().contains(query) ||
                            building.getShortName().toLowerCase().contains(query)) {
                        sgwList.add(building);
                    }
                }

                for (Building building : Campus.LOY.getBuildings()) {
                    if (building.getName().toLowerCase().contains(query) ||
                            building.getShortName().toLowerCase().contains(query)) {
                        loyList.add(building);
                    }
                }

                // We only should check once whether the campus name is a match. Added at end for consistency
                if (Campus.SGW.getName().toLowerCase().contains(query) ||
                        Campus.SGW.getShortName().toLowerCase().contains(query)) {
                    sgwFilteredList.addAll(Campus.SGW.getBuildings());
                } else {
                    sgwFilteredList.addAll(sgwList);
                }

                if (Campus.LOY.getName().toLowerCase().contains(query) ||
                        Campus.LOY.getShortName().toLowerCase().contains(query)) {
                    loyolaFilteredList.addAll(Campus.LOY.getBuildings());
                } else {
                    loyolaFilteredList.addAll(loyList);
                }
            }
        }

        notifyDataSetChanged();
    }
}

