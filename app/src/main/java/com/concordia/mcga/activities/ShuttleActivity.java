package com.concordia.mcga.activities;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.concordia.mcga.exceptions.MCGADatabaseException;
import com.concordia.mcga.helperClasses.DatabaseConnector;
import com.concordia.mcga.models.Campus;

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


    private void setupBackButton() {
        AppCompatImageButton button = (AppCompatImageButton) findViewById(R.id.shuttleBackButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    private void goBack() {
        this.finish();
    }

    private void shuttleColumnToggleVisibility(boolean showSGW) {
        tableLayout.setColumnCollapsed(SGW_TO_LOY_MOTH, !showSGW);
        tableLayout.setColumnCollapsed(SGW_TO_LOY_FRI, !showSGW);
        tableLayout.setColumnCollapsed(LOY_TO_SGW_MOTH, showSGW);
        tableLayout.setColumnCollapsed(LOY_TO_SGW_FRI, showSGW);
        tableLayout.refreshDrawableState();
    }

    private void instantiateSGWtoLoyolaButton() {
        sgwToLoyola = (Button) findViewById(R.id.fromSgw);
        sgwToLoyola.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuttleColumnToggleVisibility(true);
                Log.d("Button","from SGW");
            }
        });
    }

    private void instantiateLoyolatoSGWButton() {
        loyolaToSgw = (Button) findViewById(R.id.fromLoy);
        loyolaToSgw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuttleColumnToggleVisibility(false);
                Log.d("Button","from Loyola");
            }
        });
    }

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

    private void populateShuttleTable() {
        tableLayout = (TableLayout) findViewById(R.id.shuttleTimeTable);

        for (int rowIndex = 0; rowIndex < shuttleSchedule.length; rowIndex++) {
            TableRow tr = new TableRow(ShuttleActivity.this);

            if (rowIndex % 2 == 0) {
                tr.setBackgroundColor(Color.LTGRAY);
                Log.d("COLOR","gray");
            } else {
                tr.setBackgroundColor(Color.WHITE);
                Log.d("COLOR","white");
            }

            for (int columnIndex = 0; columnIndex < shuttleSchedule[rowIndex].length; columnIndex++) {
                TextView textview = new TextView(ShuttleActivity.this);
                textview.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

                textview.setText(shuttleSchedule[rowIndex][columnIndex]);
                textview.setTextColor(Color.BLACK);
                textview.setTextSize(16f);

                tr.addView(textview);
                Log.d("Shuttle",shuttleSchedule[rowIndex][columnIndex]);
            }
            tableLayout.addView(tr);
        }

        tableLayout.setVisibility(View.VISIBLE);
        tableLayout.refreshDrawableState();
        shuttleColumnToggleVisibility(true);
    }


}
