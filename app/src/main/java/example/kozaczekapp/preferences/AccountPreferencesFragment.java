package example.kozaczekapp.preferences;

import android.os.Bundle;
import android.preference.Preference;

import example.kozaczekapp.R;

public class AccountPreferencesFragment extends android.preference.PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.account_preferences);
        Preference loginPreference = findPreference("@string/preference_login_key");
    }

}
