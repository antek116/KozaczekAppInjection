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
    private boolean accountExists;
    private AccountManager accountManager;
    private Toast toast;
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.login_form_layout);
        toast = new Toast(getBaseContext());
        accountManager = AccountManager.get(getBaseContext());
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextEmail = (EditText) findViewById(R.id.editTextEnterEmail);
        btnLogin = (Button) findViewById(R.id.buttonConfirmLogin);
        accountExists = accountManager.getAccountsByType(AccountKeyConstants.ACCOUNT_TYPE).length > 0;
        if (accountExists) {
            Account[] accounts = accountManager.getAccountsByType(AccountKeyConstants.ACCOUNT_TYPE);
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
    }

    /**
     * Called when submitting form
     * It gets field values from form, adds them to intent and calls finishLogin(Intent)
     */
    public void onSubmitClick(View view) {
        if (isFormValid()) {
            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            if (accountExists) {
                setNewToken(password);
            } else {
                addNewAccount(email, password);
            }
        }
    }

    private void showToast(int msg){
        try{ toast.getView().isShown();     // true if visible
            toast.setText( msg);
        } catch (Exception e) {         // invisible if exception
            toast = Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT);
        }
        toast.show();  //finally display it
    }
    private void addNewAccount(String email, String password) {
        final Account account = new Account(email, AccountKeyConstants.ACCOUNT_TYPE);
        accountManager.setPassword(account, password);
        accountManager.addAccountExplicitly(account, password, null);
        accountManager.setAuthToken(account, AccountKeyConstants.AUTHTOKEN_TYPE_FULL_ACCESS, new Token().toString());
        finish();
    }

    private void setNewToken(String password) {
        Account[] account = accountManager.getAccountsByType(AccountKeyConstants.ACCOUNT_TYPE);
        if (validatePassword(password, account[0])) {
            new TimeZone(getBaseContext()).run();
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
