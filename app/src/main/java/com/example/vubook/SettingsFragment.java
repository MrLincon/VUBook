package com.example.vubook;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;

public class SettingsFragment extends PreferenceFragment {

    //    private static final String PREF_NAME = "pref_name";
    private static final String PREF_ACCOUNT = "pref_account";
    private static final String PREF_DEPT = "pref_dept";
    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//                if (key.equals(PREF_NAME)) {
//                    Preference namePref = findPreference(key);
//                    namePref.setSummary(sharedPreferences.getString(key, ""));
//                }
                if (key.equals(PREF_ACCOUNT)) {
                    Preference accountPref = findPreference(key);
                    accountPref.setSummary(sharedPreferences.getString(key, ""));
                }
                if (key.equals(PREF_DEPT)) {
                    Preference deptPref = findPreference(key);
                    deptPref.setSummary(sharedPreferences.getString(key, ""));
                }
            }

        };

    }

    @Override
    public void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);

//        Preference namePref = findPreference(PREF_NAME);
//        namePref.setSummary(getPreferenceScreen().getSharedPreferences().getString(PREF_NAME, "Enter Your Name"));

        Preference secPref = findPreference(PREF_ACCOUNT);
        secPref.setSummary(getPreferenceScreen().getSharedPreferences().getString(PREF_ACCOUNT, "Select Your account type"));

        Preference departmentPref = findPreference(PREF_DEPT);
        departmentPref.setSummary(getPreferenceScreen().getSharedPreferences().getString(PREF_DEPT, "Select your department"));

    }

    @Override
    public void onPause() {
        super.onPause();

        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(preferenceChangeListener);
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
