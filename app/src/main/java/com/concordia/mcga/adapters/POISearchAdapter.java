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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * This adapter searches through both campuses to find POIs (for now, buildings) that match a query.
 */
public class POISearchAdapter extends BaseExpandableListAdapter {
    private Context context;

    public static final int SGW_INDEX = 0;
    public static final int LOY_INDEX = 1;

    private List<Building> sgwFilteredList;
    private List<Building> loyolaFilteredList;

    /**
     * The constructor initializes the empty lists that store the currently queried POIs
     * @param context View context to search
     */
    public POISearchAdapter(Context context) {
        this.context = context;

        this.sgwFilteredList = new ArrayList<>();
        this.loyolaFilteredList = new ArrayList<>();
    }

    /**
     * Evaluates the number of campuses active based on the current queried POIs
     * @return The number of active campuses
     */
    @Override
    public int getGroupCount() {
        int count = 0;
        if (!getGroupIsEmpty(SGW_INDEX)) {
            count++;
        }
        if (!getGroupIsEmpty(LOY_INDEX)) {
            count++;
        }
        return count;
    }

    /**
     * This overrides the BaseExpandableList function to get the number of children (POI) entries
     * @param childPosition Campus index
     * @return Number of POIs specific to the passed campus
     */
    @Override
    public int getChildrenCount(int childPosition) {
        Campus campus = (Campus)getGroup(childPosition);
        if (campus == Campus.SGW) {
            return sgwFilteredList.size();
        } else if (campus == Campus.LOY) {
            return loyolaFilteredList.size();
        } else {
            return 0;
        }
    }

    /**
     * This overrides the BaseExpandableList function to get the campus specified by an index.
     * This function is written specifically to address the quirks in the way BaseExpandableList
     * handles groups and children - it is used internally to get the "group" (campus) based on the
     * number of visible groups, so we have to handle different cases depending on whether the
     * POIs filtered by the query come from a single campus or both of them.
     * @param groupPosition Active campus index
     * @return The campus specified by the index
     */
    @Override
    public Object getGroup(int groupPosition) {
        boolean sgwClear = getGroupIsEmpty(SGW_INDEX);
        boolean loyClear = getGroupIsEmpty(LOY_INDEX);

        if (sgwClear && loyClear) {
            return null;
        } else if (sgwClear) {
            if (groupPosition == 0) {
                return Campus.LOY;
            } else {
                return null;
            }
        } else if (loyClear) {
            if (groupPosition == 0) {
                return Campus.SGW;
            } else {
                return null;
            }
        } else {
            if (groupPosition == SGW_INDEX) {
                return Campus.SGW;
            } else if (groupPosition == LOY_INDEX) {
                return Campus.LOY;
            } else {
                return null;
            }
        }
    }

    /**
     * This overrides the BaseExpandableList function to get an actual menu item - in this case, POI
     * @param groupPosition The active campus index (see getGroup)
     * @param childPosition The POI index within the specified campus
     * @return The POI that exists according to the filtered query
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Campus campus = (Campus)getGroup(groupPosition);
        if (campus == Campus.SGW) {
            return sgwFilteredList.get(childPosition);
        } else if (campus == Campus.LOY) {
            return loyolaFilteredList.get(childPosition);
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

    /**
     * Helper function to determine whether a given filtered POI list (from the query) is empty
     * @param index The campus to query
     * @return True if the query-filtered campus list is empty, false if it has items
     */
    public boolean getGroupIsEmpty(int index) {
        if (index == SGW_INDEX) {
            return sgwFilteredList.isEmpty();
        } else if (index == LOY_INDEX) {
            return loyolaFilteredList.isEmpty();
        } else {
            return false;
        }
    }

    /**
     * Inflates and returns the view associated with the index-specified campus or POI group
     * @param groupPosition Campus/group index
     * @param b Whether the group is collapsed or expanded
     * @param view The old view to reuse, if possible
     * @param viewGroup The parent that this view will be attached to
     * @return The inflated POI group view
     */
    @Override
    public View getGroupView(int groupPosition, boolean b, View view, ViewGroup viewGroup) {
        Campus campus = (Campus)getGroup(groupPosition);

        if (campus == null) {
            return null;
        }

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.poi_search_group_row, null);
        }

        CircleImageView campusImage = (CircleImageView) view.findViewById(R.id.groupImage);
        Campus camp = (Campus)getGroup(groupPosition);
        if (camp == Campus.SGW) {
            campusImage.setImageResource(R.mipmap.ic_sgw_campus);
        } else {
            campusImage.setImageResource(R.mipmap.ic_loy_campus);
        }

        TextView campusRow = (TextView) view.findViewById(R.id.groupText);
        campusRow.setText(campus.getName());

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
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        Building building = (Building)getChild(groupPosition, childPosition);

        if (building == null) {
            return null;
        }

        if (view == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.poi_search_child_row, null);
        }

        TextView buildingRow = (TextView) view.findViewById(R.id.childText);
        buildingRow.setText(building.getName());

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

        if (query.isEmpty()) {
            //sgwFilteredList.addAll(Campus.SGW.getBuildings());
            //loyolaFilteredList.addAll(Campus.LOY.getBuildings());
        } else {
            // Temporary buffers. Alternative would be to use synchronize
            List<Building> sgwList = new ArrayList<>();
            List<Building> loyList = new ArrayList<>();

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

        notifyDataSetChanged();
    }
}

