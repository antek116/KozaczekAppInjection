package example.kozaczekapp.Authenticator;

import android.accounts.AccountAuthenticatorActivity;
import android.content.Intent;

/**
 * Created by Patryk Gwiazdowski on 06.04.2016.
 * // Good Job Patryk
 */
public class AuthenticatorActivity extends AccountAuthenticatorActivity {
     /**
     * Called when submitting form
     * It gets field values from form, adds them to intent and calls finishLogin(Intent)
     */
    public void submit() {
    }

    /**
     * Creates or updates Account with data specified in intent
     *
     * @param intent created in submit class
     */
    private void finishLogin(Intent intent) {
    }
}
