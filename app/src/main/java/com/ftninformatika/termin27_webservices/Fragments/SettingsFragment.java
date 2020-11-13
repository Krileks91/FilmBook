package com.ftninformatika.termin27_webservices.Fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.ftninformatika.termin27_webservices.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}