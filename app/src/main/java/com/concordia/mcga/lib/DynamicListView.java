package com.concordia.mcga.lib;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.concordia.mcga.activities.R;

import java.util.ArrayList;

/**
 * Created by Charmander on 2/25/2017.
 */

public class DynamicListView extends ListActivity {
    //LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems = new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;

    //RECORDING HOW MANY TIMES THE BUTTON HAS BEEN CLICKED
    int clickCounter = 0;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.bottom_sheet_content);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        setListAdapter(adapter);
    }
}
