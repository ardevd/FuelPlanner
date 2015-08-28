package com.connectutb.xfuel;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.connectutb.xfuel.providers.AircraftContract;


public class MainFragment extends Fragment implements AircraftContract{

    private Spinner aircraftSpinner;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainFragment newInstance(int sectionNumber) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        aircraftSpinner = (Spinner) rootView.findViewById(R.id.spinnerAircraft);
        populateAircraftSpinner();
        return rootView;
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private void populateAircraftSpinner(){
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.aircraft_spinner_row_layout, null,
                new String[] { AIRCRAFT_NAME },
                new int[] { R.id.textViewSpinnerRowAircraftName }, SimpleCursorAdapter.NO_SELECTION);

        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                    return false;
            }
        });

        //Associate adapter with listView
        aircraftSpinner.setAdapter(adapter);

        LoaderManager loaderManager = getLoaderManager();

        /*
		 * Callback (third argument) is important.
		 *
		 */
        loaderManager.initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {

            @Override
            public Loader<Cursor> onCreateLoader(int id, Bundle args) {
                CursorLoader loader = new CursorLoader(getActivity(), QUERY_AIRCRAFT_ITEM, null, null, null, null);
                return loader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                //Loaded our data.
                ((SimpleCursorAdapter) aircraftSpinner.getAdapter()).swapCursor(data);

            }

            @Override
            public void onLoaderReset(Loader<Cursor> arg0) {
                //We got nothing there, so swap it back to null
                ((SimpleCursorAdapter) aircraftSpinner.getAdapter()).swapCursor(null);
            }

        });
        }
}