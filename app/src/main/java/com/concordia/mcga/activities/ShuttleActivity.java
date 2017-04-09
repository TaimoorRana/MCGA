package com.concordia.mcga.activities;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.concordia.mcga.exceptions.MCGADatabaseException;
import com.concordia.mcga.helperClasses.DatabaseConnector;

public class ShuttleActivity extends AppCompatActivity {
    final static int SGW_TO_LOY_MOTH = 0;
    final static int SGW_TO_LOY_FRI = 1;
    final static int LOY_TO_SGW_MOTH = 2;
    final static int LOY_TO_SGW_FRI = 3;
    final static int SHUTTLE_TABLE_COLUMNS = 4;

    private String[][] shuttleSchedule;
    private TableLayout tableLayout;
    private Button sgwToLoyola;
    private Button loyolaToSgw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuttle);

        setupBackButton();
        instantiateSGWtoLoyolaButton();
        instantiateLoyolatoSGWButton();
        createShuttleSchedule();
        populateShuttleTable();
    }

    /**
     * This is the button that quits the Shuttle Activity to give back
     * control to the main activity.
     */
    private void setupBackButton() {
        AppCompatImageButton button = (AppCompatImageButton) findViewById(R.id.shuttleBackButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    /**
     * Quits the current activity to return control to the Main Activity
     */
    private void goBack() {
        this.finish();
    }

    /*
    Helper method for hiding and showing columns depending on the
    source campus selected.
     */
    private void switchColumnsForCampusShuttle(boolean showSGW) {
        tableLayout.setColumnCollapsed(SGW_TO_LOY_MOTH, !showSGW);
        tableLayout.setColumnCollapsed(SGW_TO_LOY_FRI, !showSGW);
        tableLayout.setColumnCollapsed(LOY_TO_SGW_MOTH, showSGW);
        tableLayout.setColumnCollapsed(LOY_TO_SGW_FRI, showSGW);
        tableLayout.refreshDrawableState();
    }

    /*
    Helper method to get the colours for the campus direction to toggle
     */
    private void switchColorOnCampusButtons(boolean showSGW) {
        if (showSGW) {
            sgwToLoyola.setTextColor(Color.WHITE);
            loyolaToSgw.setTextColor(Color.LTGRAY);
        } else {
            sgwToLoyola.setTextColor(Color.LTGRAY);
            loyolaToSgw.setTextColor(Color.WHITE);
        }
    }

    private void instantiateSGWtoLoyolaButton() {
        sgwToLoyola = (Button) findViewById(R.id.fromSgw);
        sgwToLoyola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchColumnsForCampusShuttle(true);
                switchColorOnCampusButtons(true);
            }
        });
    }

    private void instantiateLoyolatoSGWButton() {
        loyolaToSgw = (Button) findViewById(R.id.fromLoy);
        loyolaToSgw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchColumnsForCampusShuttle(false);
                switchColorOnCampusButtons(false);
            }
        });
    }

    /**
     * Queries the database for the shuttle schedules
     * Creates a nx4 array of String to store the schedule
     */
    private void createShuttleSchedule() {
        Cursor shuttleCursor;
        String shuttleQuery = "select * from shuttle;";
        try {
            shuttleCursor = DatabaseConnector.getInstance().getDb().rawQuery(shuttleQuery, null);
        } catch (MCGADatabaseException e) {
            throw new Error("Could not connect to database");
        }

        shuttleSchedule = new String[shuttleCursor.getCount()][SHUTTLE_TABLE_COLUMNS];
        for (int rowIndex = 0; shuttleCursor.moveToNext(); rowIndex++) {
            for (int columnIndex = 0; columnIndex < SHUTTLE_TABLE_COLUMNS; columnIndex++) {
                shuttleSchedule[rowIndex][columnIndex] = shuttleCursor.getString(columnIndex);
            }
        }

        shuttleCursor.close();
    }


    /**
     * Method that creates a table and fills it programmatically
     * with the information contained in the shuttle schedule array
     * of String
     */
    private void populateShuttleTable() {
        tableLayout = (TableLayout) findViewById(R.id.shuttleTimeTable);

        for (int rowIndex = 0; rowIndex < shuttleSchedule.length; rowIndex++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
            tableRow.setWeightSum(2f);

            if (rowIndex % 2 == 0) {
                tableRow.setBackgroundColor(Color.LTGRAY);
            } else {
                tableRow.setBackgroundColor(Color.WHITE);
            }

            for (int columnIndex = 0; columnIndex < shuttleSchedule[rowIndex].length; columnIndex++) {
                TextView textView = new TextView(this);
                textView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                textView.setText(shuttleSchedule[rowIndex][columnIndex]);
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(16f);
                textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                textView.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.text_view_height);

                tableRow.addView(textView);
            }
            tableLayout.addView(tableRow);
        }

        switchColumnsForCampusShuttle(true);
        switchColorOnCampusButtons(true);
    }

}
