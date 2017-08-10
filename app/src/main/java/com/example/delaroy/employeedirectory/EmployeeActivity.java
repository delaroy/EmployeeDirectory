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


import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
    EmployeeDbHelper employeeDbHelper;

    /** Identifier for the employee data loader */
    private static final int EMPLOYEE_LOADER = 0;

    /** Adapter for the ListView */
    EmployeeCursorAdapter mCursorAdapter;


    @BindView(R.id.fab) FloatingActionButton button;

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

        // Setup an Adapter to create a list item for each row of pet data in the Cursor.
        // There is no pet data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new EmployeeCursorAdapter(this, null);
        employeeListView.setAdapter(mCursorAdapter);


        employeeDbHelper = new EmployeeDbHelper(this);

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
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Delete all entries" menu option

        }
        return super.onOptionsItemSelected(item);
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
        // Update {@link EmployeeCursorAdapter} with this new cursor containing updated pet data
        mCursorAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);

    }
}
