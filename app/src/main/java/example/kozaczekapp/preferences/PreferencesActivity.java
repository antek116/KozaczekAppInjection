package example.kozaczekapp.preferences;

import android.preference.PreferenceActivity;

import java.util.List;

import example.kozaczekapp.R;

public class PreferencesActivity extends PreferenceActivity {
    /**
     * Called when the activity needs its list of headers build.
     *
     * @param target The list in which the parsed headers should be placed.
     */
    @Override
    public void onBuildHeaders(List<PreferenceActivity.Header> target) {
        loadHeadersFromResource(R.xml.preferences_header, target);
    }

    /**
     * Subclasses should override this method and verify that the given fragment is a valid type to be attached to this activity.
     * @param fragmentName the class name of the Fragment about to be attached to this activity.
     * @return true if the fragment class name is valid for this Activity and false otherwise.
     */
    @Override
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName);
    }
}
