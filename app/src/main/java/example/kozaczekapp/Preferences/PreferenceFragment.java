package example.kozaczekapp.preferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;

import example.kozaczekapp.R;

public class PreferenceFragment extends android.preference.PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    /**
     * Method can be called while the fragment's activity is still in the process of being created.
     * As such, you can not rely on things like the activity's content view hierarchy being initialized at this point.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        ListPreference listPreference = (ListPreference) findPreference(getString(R.string.downloadType));
        listPreference.setSummary(getString(R.string.selected_download_preference) + listPreference.getValue());
    }

    /**
     * Called when a shared preference is changed, added, or removed. This may be called even if a preference is set to its existing value.
     * This callback will be run on your main thread.
     *
     * @param sharedPreferences The SharedPreferences that received the change.
     * @param key               The key of the preference that was changed, added, or removed.
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        if (pref instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) pref;
            pref.setSummary(getString(R.string.selected_download_preference) + listPreference.getValue());
        }
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * We registers there shared preference change listener
     */
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    /**
     * Called when the Fragment is no longer resumed.
     * We unregisters there on shared preference change listener.
     */
    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
