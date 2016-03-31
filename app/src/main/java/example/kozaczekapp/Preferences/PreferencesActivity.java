package example.kozaczekapp.Preferences;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import java.util.List;

import example.kozaczekapp.R;

public class PreferencesActivity extends PreferenceActivity
{
    @Override
    public void onBuildHeaders(List<PreferenceActivity.Header> target)
    {
        loadHeadersFromResource(R.xml.preferences_header, target);
    }

    @Override
    protected boolean isValidFragment(String fragmentName)
    {
        return PreferenceFragment.class.getName().equals(fragmentName);
    }
}
