package com.example.youss.hango.activities;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.example.youss.hango.R;
import com.example.youss.hango.infrastructure.Utilities;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new SortPreferencesFragment()).commit();
    }

    public static class SortPreferencesFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferencegeneral);
            bindPreferenceSummaryToVal(findPreference(getString(R.string.sort_hango_by_name)));
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            setPreferenceSummary(preference,newValue);
            SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString(Utilities.MyPreferences,newValue.toString()).apply();
            return true;
        }


        private void bindPreferenceSummaryToVal(Preference preference)
        {
            preference.setOnPreferenceChangeListener(this);
            setPreferenceSummary(preference, PreferenceManager.getDefaultSharedPreferences(preference.getContext())
            .getString(preference.getKey(),""));

        }

        private void setPreferenceSummary(Preference preference,Object val)
        {
                //We pass our preference and value,  find the index where the string value
                //Bind the summary to the preference
                String value=val.toString();
                if(preference instanceof ListPreference)
                {
                     ListPreference listPreference= (ListPreference) preference;
                    int index=listPreference.findIndexOfValue(value);
                    if(index>=0)
                    {
                        preference.setSummary(listPreference.getEntries()[index]) ;
                    }
                }
        }
    }
}
