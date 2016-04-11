package example.kozaczekapp.preferences;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.os.Bundle;
import android.preference.Preference;

import example.kozaczekapp.R;
import example.kozaczekapp.authenticator.AccountKeyConstants;

public class AccountPreferencesFragment extends android.preference.PreferenceFragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.account_preferences);
        AccountManager accountManager = AccountManager.get(getActivity().getBaseContext());
        String accountType = getResources().getString(R.string.account_type);
        Account[] accounts = accountManager.getAccountsByType(accountType);

        if (accounts.length > 0) {
            Account account = accounts[0];
            final AccountManagerFuture<Bundle> future = accountManager.getAuthToken(account, AccountKeyConstants.AUTHTOKEN_TYPE_FULL_ACCESS, null, getActivity(), null, null);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Bundle tokenBundle = future.getResult();
                        Preference loginPreference = findPreference(getResources().getString(R.string.account_login_key));
                        Preference tokenPreference = findPreference(getResources().getString(R.string.account_token_key));
                        final String login = tokenBundle.get(AccountManager.KEY_ACCOUNT_NAME).toString();
                        final String token = tokenBundle.get(AccountManager.KEY_AUTHTOKEN).toString();
                        loginPreference.setTitle(getResources().getString(R.string.account_login_title) + ": " + login);
                        tokenPreference.setTitle(getResources().getString(R.string.account_token_title) + ": " + token);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
