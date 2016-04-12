package example.kozaczekapp.authenticator;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import example.kozaczekapp.R;
import example.kozaczekapp.timeZoneApi.TimeZone;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {
    private EditText editTextPassword;
    private EditText editTextEmail;
    private Button btnLogin;
    private AccountManager accountManager;
    private Toast toast;
    private Account[] accounts;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.login_form_layout);
        accountManager = AccountManager.get(getBaseContext());
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmail = (EditText) findViewById(R.id.editTextEnterEmail);
        btnLogin = (Button) findViewById(R.id.buttonConfirmLogin);
        accounts = accountManager.getAccountsByType(AccountKeyConstants.ACCOUNT_TYPE);
        if (accounts.length > 0) {
            editTextEmail.setText(accounts[0].name);
            editTextEmail.setEnabled(false);
            btnLogin.setText(R.string.login);
        }
    }

    /**
     * Called when the activity has detected the user's press of the back
     * key.  The default implementation simply finishes the current activity,
     * but you can override this to do whatever you want.
     */
    @Override
    public void onBackPressed() {
        showToast(R.string.loginBeforeUse);
        //FIXME: why u not let me out?
    }

    /**
     * Called when submitting form
     * It gets field values from form, adds them to intent and calls finishLogin(Intent)
     */
    public void onSubmitClick(View view) {
        if (isFormValid()) {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            if (accounts.length >= 1) {
                setNewToken(password);
            } else {
                addNewAccount(email, password);
            }
        }
    }

    private void showToast(int msg) {

        //todo: improve
        if (toast == null) {
            toast = Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText("");

        }

    }

    private void addNewAccount(String email, String password) {
        final Account account = new Account(email, AccountKeyConstants.ACCOUNT_TYPE);
        accountManager.setPassword(account, password);
        accountManager.addAccountExplicitly(account, password, null);
        //todo: get token from server
        accountManager.setAuthToken(account, AccountKeyConstants.AUTHTOKEN_TYPE_FULL_ACCESS, new Token().toString());
        finish();
    }

    private void setNewToken(String password) {
        Account[] account = accountManager.getAccountsByType(AccountKeyConstants.ACCOUNT_TYPE);
        if (validatePassword(password, account[0])) {

            //FIXME: download token in different manner
            //FIXME: do not go back to app until token refreshed

            new Thread(new TimeZone(getApplicationContext())).start();



            finish();
        } else {
            editTextPassword.setError(getString(R.string.invalidPassword));
        }
    }

    private boolean isFormValid() {
        return isEmailValid(FormValidator.getInstance());
    }

    private boolean validatePassword(String password, Account account) {
        return accountManager.getPassword(account).equals(password);
    }


    private boolean isEmailValid(FormValidator validator) {
        Integer hasEmailError = validator.isValid(editTextEmail.getText().toString(),
                FormValidator.FieldType.EMAIL);
        if (hasEmailError != null) {
            editTextEmail.setError(getString(hasEmailError));
            return false;
        } else {
            editTextEmail.setError(null);
            return true;
        }
    }
}
