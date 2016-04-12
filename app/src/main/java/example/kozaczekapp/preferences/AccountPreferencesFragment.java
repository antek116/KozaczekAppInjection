package example.kozaczekapp.preferences;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.os.Bundle;
import android.preference.Preference;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import example.kozaczekapp.R;
import example.kozaczekapp.authenticator.AccountKeyConstants;

public class AccountPreferencesFragment extends android.preference.PreferenceFragment {

    Preference deleteAccountPreference;
    Account account;
    AccountManager accountManager;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity().getApplicationContext(), "deleteAccountButton.setOnClickListener", Toast.LENGTH_SHORT).show();
            accountManager.removeAccount(account, null, null);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.account_preferences);

        accountManager = AccountManager.get(getActivity().getBaseContext());
        String accountType = getResources().getString(R.string.account_type);
        Account[] accounts = accountManager.getAccountsByType(accountType);
        deleteAccountPreference = findPreference("DELETE_BUTTON_KEY");
        View v = deleteAccountPreference.getView(null, null);
        Button deleteAccountButton = (Button) v.findViewById(R.id.idDeleteAccountButton);
        deleteAccountButton.setOnClickListener(onClickListener);

        if (accounts.length > 0) {
            account = accounts[0];
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
