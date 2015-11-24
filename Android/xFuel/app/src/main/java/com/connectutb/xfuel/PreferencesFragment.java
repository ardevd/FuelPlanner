package com.connectutb.xfuel;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Zygote on 24.11.2015.
 */
public class PreferencesFragment extends PreferenceFragment {

    public static PreferencesFragment newInstance(int sectionNumber) {
        PreferencesFragment fragment = new PreferencesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}