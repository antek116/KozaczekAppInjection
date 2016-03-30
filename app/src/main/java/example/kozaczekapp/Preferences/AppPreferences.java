package example.kozaczekapp.Preferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;

import example.kozaczekapp.R;

public class AppPreferences extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        ListPreference listPreference = (ListPreference) findPreference(getString(R.string.downloadType));
        listPreference.setSummary(getString(R.string.selected_download_preference) + listPreference.getValue());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        if (pref instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) pref;
            pref.setSummary(getString(R.string.selected_download_preference) + listPreference.getValue());
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
