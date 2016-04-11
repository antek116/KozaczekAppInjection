package example.kozaczekapp.authenticator;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import example.kozaczekapp.R;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {
    private static final String TAG = "AuthenticatorActiviy";
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
        accountExists = accountManager.getAccountsByType(AccountKeyStorage.ACCOUNT_TYPE).length > 0;
        if (getIntent().getBooleanExtra(AccountKeyStorage.ARG_CLICKED_FROM_SETTINGS, false)) {
            if (accountExists) {
                //TODO move this to AccountAuthenticator
                Toast.makeText(getApplicationContext(), getText(R.string.accountExists), Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            if (accountExists) {
                Account[] accounts = accountManager.getAccountsByType(AccountKeyStorage.ACCOUNT_TYPE);
                editTextEmail.setText(accounts[0].name);
                editTextEmail.setEnabled(false);
                btnLogin.setText(R.string.login);
            }
        }
    }

    /**
     * Called when submitting form
     * It gets field values from form, adds them to intent and calls finishLogin(Intent)
     */
    public void onSubmitClick(View view) {
        if (isFormValid()) {
            Log.d(TAG, "onSubmitClick: " + accountManager.getAccountsByType(AccountKeyStorage.ACCOUNT_TYPE).length);

            String email = editTextEmail.getText().toString();
            String password = editTextPassword.getText().toString();
            if (accountExists) {
                Account[] account = accountManager.getAccountsByType(AccountKeyStorage.ACCOUNT_TYPE);
                if (validatePassword(password, account[0])) {

                    finish();
                } else {
                    btnLogin.setError(getString(R.string.invalidPassword));
                }
            } else {
                final Account account = new Account(email, AccountKeyStorage.ACCOUNT_TYPE);
                accountManager.setPassword(account, password);
                accountManager.addAccountExplicitly(account, password, null);
                accountManager.setAuthToken(account, AccountKeyStorage.AUTHTOKEN_TYPE_FULL_ACCESS, new Token().toString());
                finish();
            }
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

    private boolean isNameValid(FormValidator validator){
        Integer hasNameError = validator.isValid(editTextName.getText().toString(),
                FormValidator.FieldType.NAME);
        if(hasNameError != null){
            editTextName.setError(getString(hasNameError));
            return false;
        } else{
            editTextName.setError(null);
            return true;
        }
    }
    private boolean isSurnameValid(FormValidator validator){
        Integer hasSurnameError = validator.isValid(editTextSurname.getText().toString(),
                FormValidator.FieldType.NAME);
        if(hasSurnameError != null){
            editTextSurname.setError(getString(hasSurnameError));
            return false;
        } else{
            editTextSurname.setError(null);
            return true;
        }
    }
    private void fieldsListenersSetUp(){
        editTextName.addTextChangedListener(new ClearErrorWatcher(editTextName));
        editTextSurname.addTextChangedListener(new ClearErrorWatcher(editTextSurname));
        editTextEmail.addTextChangedListener(new ClearErrorWatcher(editTextEmail));
        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!editTextEmail.isFocused()) {
                    isEmailValid(validator);
                }
            }
        });
        editTextName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!editTextName.isFocused()) {
                    isNameValid(validator);
                }
            }
        });
        editTextSurname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!editTextSurname.isFocused()) {
                    isSurnameValid(validator);
                }
            }
        });
    }
}
