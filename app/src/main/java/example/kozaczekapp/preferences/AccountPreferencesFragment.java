package example.kozaczekapp.preferences;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.preference.Preference;

import example.kozaczekapp.R;

public class AccountPreferencesFragment extends android.preference.PreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.account_preferences);
        Preference loginPreference = findPreference(getResources().getString(R.string.account_login_key));
        AccountManager accountManager = AccountManager.get(getActivity().getBaseContext());
        Account[] accounts = accountManager.getAccountsByType(getResources().getString(R.string.account_type));
        if (accounts.length > 0) {
            loginPreference.setTitle("Email: " + accounts[0].name);
        }
    }
}
