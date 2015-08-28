package com.connectutb.xfuel;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.connectutb.xfuel.providers.AircraftContract;
import com.connectutb.xfuel.tools.AircraftManager;

public class MainFragment extends Fragment implements AircraftContract{
    // Views
    private Spinner aircraftSpinner;
    private RadioButton radioMetric;
    private RadioButton radioImperial;
    private RadioGroup radioGroupUnits;


    SharedPreferences settings;
    SharedPreferences.Editor editor;


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

        settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
        editor = settings.edit();

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        configureViews(rootView);

        populateAircraftSpinner();
        return rootView;
    }

    private void configureViews(View rootView){

        // Initialization of Views
        aircraftSpinner = (Spinner) rootView.findViewById(R.id.spinnerAircraft);
        radioMetric = (RadioButton) rootView.findViewById(R.id.radioButtonMetric);
        radioImperial = (RadioButton) rootView.findViewById(R.id.radioButtonImperial);
        radioGroupUnits = (RadioGroup) rootView.findViewById(R.id.radioGroupUnits);

        if (settings.getBoolean("want_metric", true)) {
            radioMetric.setChecked(true);
            radioImperial.setChecked(false);
        } else {
            radioMetric.setChecked(false);
            radioImperial.setChecked(true);
        }

        radioMetric.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    editor.putBoolean("want_metric", true).commit();
                } else {
                    editor.putBoolean("want_metric", false).commit();
                }
            }
        });
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    private void populateAircraftSpinner(){

        AircraftManager am = new AircraftManager(getActivity());
        am.updateAircraftList();

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