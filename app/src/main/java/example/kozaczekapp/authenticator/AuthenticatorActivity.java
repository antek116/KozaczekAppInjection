package example.kozaczekapp.authenticator;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import example.kozaczekapp.R;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {
    private EditText editTextPassword;
    private EditText editTextEmail;
    private Button btnLogin;
    private boolean accountExists;
    private AccountManager accountManager;

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
        accountExists = accountManager.getAccountsByType(AccountKeyConstants.ACCOUNT_TYPE).length > 0;
        if (accountExists) {
            Account[] accounts = accountManager.getAccountsByType(AccountKeyConstants.ACCOUNT_TYPE);
            editTextEmail.setText(accounts[0].name);
            editTextEmail.setEnabled(false);
            btnLogin.setText(R.string.login);
        }
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
            accountManager.setAuthToken(account[0], AccountKeyConstants.AUTHTOKEN_TYPE_FULL_ACCESS, new Token().toString());
            finish();
        } else {
            btnLogin.setError(getString(R.string.invalidPassword));
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
