package com.connectutb.xfuel;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.connectutb.xfuel.providers.HistoryContract;
import com.connectutb.xfuel.tools.FuelPlanGenerator;

import java.util.HashMap;

/**
 * Created by Zygote on 08.10.2015.
 */
public class HistoryFragment extends Fragment implements HistoryContract {

    private SimpleCursorAdapter adapter;
    private ListView listView;
    private SharedPreferences settings;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static HistoryFragment newInstance(int sectionNumber) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public HistoryFragment() {
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.history, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.clear_history:
                // Delete items from history
                deleteHistory();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteHistory(){
        getActivity().getContentResolver().delete(QUERY_HISTORY_ITEM, null, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        listView = (ListView) rootView.findViewById(R.id.listViewHistory);
        setHasOptionsMenu(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // On item select, load fuel sheet.
                Cursor obj = (Cursor) adapter.getItem(position);
                String dep = obj.getString(obj.getColumnIndex("departure"));
                String arr = obj.getString(obj.getColumnIndex("arrival"));
                String aircraft = obj.getString(obj.getColumnIndex("aircraft"));
                int unit = obj.getInt(obj.getColumnIndex("UNIT"));
                HashMap advOptions = new HashMap<>();

                if (obj.getString(obj.getColumnIndex("ttl")) != null){
                    advOptions.put("TTL",obj.getString(obj.getColumnIndex("ttl")));
                }
                if (obj.getString(obj.getColumnIndex("oew")) != null){
                    advOptions.put("OEW",obj.getString(obj.getColumnIndex("oew")));
                }
                if (obj.getString(obj.getColumnIndex("mtank")) != null){
                    advOptions.put("MTANK",obj.getString(obj.getColumnIndex("mtank")));
                }
                if (obj.getString(obj.getColumnIndex("tanker")) != null){
                    advOptions.put("TANKER",obj.getString(obj.getColumnIndex("tanker")));
                }

                boolean wantMetric = false;
                if (unit==1){
                    wantMetric = true;
                }

                FuelPlanGenerator fpg = new FuelPlanGenerator(getActivity());
                fpg.generateFuelPlan(dep, arr, aircraft, advOptions, wantMetric);
            }
        });

            populateHistoryList();
        return rootView;
    }

    private void populateHistoryList(){
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.history_list_row, null,
                new String[] { HISTORY_DEPARTURE, HISTORY_ARRIVAL, HISTORY_AIRCRAFT, HISTORY_TIMESTAMP},
                new int[] { R.id.textViewHistoryListDeparture, R.id.textViewHistoryListArrival, R.id.textViewHistoryListAircraft, R.id.textViewHistoryListTime }, SimpleCursorAdapter.NO_SELECTION);

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                int indexForTime = cursor.getColumnIndex(HISTORY_TIMESTAMP);
                if (columnIndex == indexForTime) {
                    ((TextView) view).setText(DateUtils.getRelativeTimeSpanString(
                            cursor.getLong(indexForTime)
                    ));
                    return true;
                } else
                    return false;
            }
        });

        //Associate adapter with listView
        listView.setAdapter(adapter);

        LoaderManager loaderManager = getLoaderManager();

        /*
		 * Callback (third argument) is important.
		 *
		 */
        loaderManager.initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>()

                {

                    @Override
                    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                        String sort = " DESC";
                        if (!settings.getBoolean("pref_sort_desc", true)){
                            sort = " ASC";
                        }
                        return new CursorLoader(getActivity(), QUERY_HISTORY_ITEM, null, null, null, HISTORY_TIMESTAMP + sort);
                    }

                    @Override
                    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                        //Loaded our data.
                        ((SimpleCursorAdapter) listView.getAdapter()).swapCursor(data);

                    }

                    @Override
                    public void onLoaderReset(Loader<Cursor> arg0) {
                        //We got nothing there, so swap it back to null
                        ((SimpleCursorAdapter) listView.getAdapter()).swapCursor(null);
                    }

                    }

            );
        }
    }
