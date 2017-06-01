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
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<Quake>> {

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;
    /** Adapter for the list of earthquakes */
    private QuakeAdapter mAdapter;

    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query";

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quake_list);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);


        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new QuakeAdapter(this, new ArrayList<Quake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Quake url = mAdapter.getItem(position);

               String indiUrl = url.getUrl();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(indiUrl));
                startActivity(i);
                //Log.v("MainActivity", indiUrl);
            }
        });
// Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

// Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

// Get a reference to the LoaderManager, in order to interact with loaders.
         LoaderManager loaderManager = getLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
          ProgressBar loadingIndicator = (ProgressBar) findViewById(R.id.loading_spinner);
           loadingIndicator.setVisibility(View.GONE);
             // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
//            mEmptyStateTextView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Get a reference to the ConnectivityManager to check state of network connectivity
//                    ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
//
//// Get details on the currently active default data network
//                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//
//                    if (networkInfo != null && networkInfo.isConnected()) {
//
//// Get a reference to the LoaderManager, in order to interact with loaders.
//                        LoaderManager loaderManager = getLoaderManager();
//
//                        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
//                        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
//                        // because this activity implements the LoaderCallbacks interface).
//                        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null,new EarthquakeActivity());
//                    } else {
//                        // Otherwise, display error
//                        // First, hide loading indicator so error message will be visible
//                        ProgressBar loadingIndicator = (ProgressBar) findViewById(R.id.loading_spinner);
//                        loadingIndicator.setVisibility(View.GONE);
//                        // Update empty state with no connection error message
//                        mEmptyStateTextView.setText(R.string.no_internet_connection);
//                    }
//                }
//            });
              }
    }

    @Override
 public Loader<List<Quake>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "20");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(this, uriBuilder.toString());
          }

    @Override
 public void onLoadFinished(Loader<List<Quake>> loader, List<Quake> earthquakes) {

        ProgressBar progress = (ProgressBar) findViewById(R.id.loading_spinner);
        progress.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_earthquakes);

        // Clear the adapter of previous earthquake data
        // Clear the adapter of previous earthquake data
         mAdapter.clear();
          // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
          // data set. This will trigger the ListView to update.
           if (earthquakes != null && !earthquakes.isEmpty()) {
                mAdapter.addAll(earthquakes);
        }
          }

    @Override
 public void onLoaderReset(Loader<List<Quake>> loader) {
        // Loader reset, so we can clear out our existing data.
                mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
