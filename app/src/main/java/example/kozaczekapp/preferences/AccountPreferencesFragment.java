package example.kozaczekapp.preferences;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.os.Bundle;
import android.preference.Preference;
import android.widget.Toast;

import example.kozaczekapp.R;
import example.kozaczekapp.authenticator.AccountKeyConstants;

public class AccountPreferencesFragment extends android.preference.PreferenceFragment {

    Preference deleteAccountPreference;
    Account account;
    AccountManager accountManager;
    Preference loginPreference;
    Preference tokenPreference;

    private Preference.OnPreferenceClickListener onPreferenceClickListener = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            loginPreference.setTitle(getResources().getString(R.string.account_login_title));
            tokenPreference.setTitle(getResources().getString(R.string.account_token_title));
            togglePreferences(false);
            Toast.makeText(getActivity().getApplicationContext(), getString(R.string.account_deleted_msg), Toast.LENGTH_SHORT).show();
            accountManager.removeAccount(account, null, null);
            return true;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.account_preferences);
        accountManager = AccountManager.get(getActivity().getBaseContext());
        account = getAccount();
        if (account == null) {
            return;
        }

        deleteAccountPreference = findPreference("DELETE_BUTTON_KEY");
        loginPreference = findPreference(getResources().getString(R.string.account_login_key));
        tokenPreference = findPreference(getResources().getString(R.string.account_token_key));

        final AccountManagerFuture<Bundle> future = accountManager.getAuthToken(account, AccountKeyConstants.AUTHTOKEN_TYPE_FULL_ACCESS, null, getActivity(), null, null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bundle tokenBundle = future.getResult();
                    final String login = tokenBundle.get(AccountManager.KEY_ACCOUNT_NAME).toString();
                    final String token = tokenBundle.get(AccountManager.KEY_AUTHTOKEN).toString();
                    loginPreference.setTitle(getResources().getString(R.string.account_login_title) + ": " + login);
                    tokenPreference.setTitle(getResources().getString(R.string.account_token_title) + ": " + token);
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }).start();
        togglePreferences(true);
        deleteAccountPreference.setOnPreferenceClickListener(onPreferenceClickListener);
    }

    private Account getAccount() {
        String accountType = getResources().getString(R.string.account_type);
        Account[] accounts = accountManager.getAccountsByType(accountType);
        Account currentAccount = null;
        if(accounts.length > 0) {
            currentAccount = accounts[0];
        }
        return currentAccount;
    }

    private void togglePreferences(boolean state) {
        loginPreference.setEnabled(state);
        tokenPreference.setEnabled(state);
        deleteAccountPreference.setEnabled(state);
        deleteAccountPreference.setSelectable(state);
    }
}
