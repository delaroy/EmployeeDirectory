/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.delaroy.employeedirectory;


import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.example.delaroy.employeedirectory.data.EmployeeContract;
import com.example.delaroy.employeedirectory.data.EmployeeDbHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Displays list of employees that were entered and stored in the app.
 */
public class EmployeeActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {


    /** Identifier for the employee data loader */
    private static final int EMPLOYEE_LOADER = 0;

    /** Adapter for the ListView */
    EmployeeCursorAdapter mCursorAdapter;


    @BindView(R.id.fab) FloatingActionButton button;

    public EmployeeActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        // Setup FAB to open EmployeeEditor
        ButterKnife.bind(this);
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmployeeActivity.this, EmployeeEditor.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra(EmployeeEditor.EXTRA_RECT, createRect(button));
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the employee data
        ListView employeeListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        employeeListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row of employee data in the Cursor.
        // There is no employee data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new EmployeeCursorAdapter(this, null);
        employeeListView.setAdapter(mCursorAdapter);

       //TODO
        // Setup the item click listener
        employeeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(EmployeeActivity.this, EmployeeDetails.class);

                // Form the content URI that represents the specific employee that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link EmployeeEntry#CONTENT_URI}.
                // For example, the URI would be "content://com.example.delaroy.employeedirectory/employees/2"
                // if the employee with ID 2 was clicked on.
                Uri currentEmployeeUri = ContentUris.withAppendedId(EmployeeContract.EmployeeEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentEmployeeUri);
                intent.putExtra(EmployeeEditor.EXTRA_RECT, createRect(button));

                startActivity(intent);

            }
        });


        employeeListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id){
                // Create new intent to go to {@link EmployeeEditor}
                Intent intent = new Intent(EmployeeActivity.this, EmployeeEditor.class);

                // Form the content URI that represents the specific employee that was clicked on,
                // by appending the "id" (passed as input to this method) onto the
                // {@link EmployeeEntry#CONTENT_URI}.
                // For example, the URI would be "content://com.example.delaroy.employeedirectory/employees/2"
                // if the employee with ID 2 was clicked on.
                Uri currentEmployeeUri = ContentUris.withAppendedId(EmployeeContract.EmployeeEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentEmployeeUri);
                intent.putExtra(EmployeeEditor.EXTRA_RECT, createRect(button));

                // Launch the {@link EmployeeEditor} to display the data for the current employee.
                startActivity(intent);

                return true;

            }
        });

        // Kick off the loader
        getLoaderManager().initLoader(EMPLOYEE_LOADER, null, this);

    }

    private Rect createRect(View view) {
        Rect rect = new Rect();
        view.getDrawingRect(rect);
        ((ViewGroup) view.getParent()).offsetDescendantRectToMyCoords(view, rect);
        return rect;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                onSearchRequested();
                return true;
            default:
                return false;
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                EmployeeContract.EmployeeEntry._ID,
                EmployeeContract.EmployeeEntry.COLUMN_FIRSTNAME,
                EmployeeContract.EmployeeEntry.COLUMN_LASTNAME,
                EmployeeContract.EmployeeEntry.COLUMN_TITLE,
                EmployeeContract.EmployeeEntry.COLUMN_DEPARTMENT,
                EmployeeContract.EmployeeEntry.COLUMN_CITY,
                EmployeeContract.EmployeeEntry.COLUMN_PHONE,
                EmployeeContract.EmployeeEntry.COLUMN_EMAIL
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                EmployeeContract.EmployeeEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Update {@link EmployeeCursorAdapter} with this new cursor containing updated employee data
        mCursorAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);

    }
}
