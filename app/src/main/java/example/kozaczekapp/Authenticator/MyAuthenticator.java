package example.kozaczekapp.Authenticator;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Patryk Gwiazdowski on 06.04.2016.
 * // Good Job Patryk
 */
public class MyAuthenticator extends AbstractAccountAuthenticator {

    public MyAuthenticator(Context context) {
        super(context);
    }

    /**
     * Returns a Bundle that contains the Intent of the activity that can be used to edit the
     * properties. In order to indicate success the activity should call response.setResult()
     * with a non-null Bundle.
     */
    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    /**
     *
     * Adds an account of the specified accountType.
     * It is called when user click Add Account in device settings
     * @return Bundle woth intent to start AuthenticatorActivity
     * TODO probably it is important
     */
    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        return null;
    }

    /**
     * Checks that the user knows the credentials of an account.
     */
    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    /**
     * Gets an authtoken for an account from previous succesful login on device.
     * If ther eis no account user will be prompted to log-in
     * TODO probably it is important
     */
    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    /**
     * Ask the authenticator for a localized label for the given authTokenType.
     */
    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    /**
     * Update the locally stored credentials for an account.
     */
    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    /**
     * Checks if the account supports all the specified authenticator specific features.
     */
    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }
}
