package example.kozaczekapp.authenticator;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import example.kozaczekapp.R;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {
    private static final String TAG = "AuthenticatorActiviy";
    private EditText editTextPassword;
    private EditText editTextEmail;
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
        accountExists = accountManager.getAccountsByType(AccountKeyStorage.ACCOUNT_TYPE).length > 0;
        if(getIntent().getBooleanExtra(AccountKeyStorage.ARG_CLICKED_FROM_SETTINGS, false)) {
            if (accountExists) {
                //TODO move this to AccountAuthenticator
                Toast.makeText(getApplicationContext(),getText(R.string.accountExists),Toast.LENGTH_SHORT).show();
                finish();
            }
        }else {
            if (accountExists) {
                editTextEmail.setText(accountManager.getAccountsByType(AccountKeyStorage.ARG_ACCOUNT_TYPE)[0].name);
                Button btnLogin = (Button) findViewById(R.id.buttonConfirmLogin);
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
            if (validatePassword(password, account[0])){
                accountManager.setAuthToken(account[0], "", String.valueOf(System.currentTimeMillis()));
                finish();
            }
        }else {
            final Account account = new Account(email, AccountKeyStorage.ACCOUNT_TYPE);
            accountManager.setPassword(account, password);
            accountManager.addAccountExplicitly(account, password, null);
            //TODO użyc Madzi tokena
            finish();
        }
}
    }
    private boolean validatePassword(String password, Account account){
        return accountManager.getPassword(account).equals(password);
    }

    private boolean isEmailValid(FormValidator validator){
        Integer hasEmailError = validator.isValid(editTextEmail.getText().toString(),
                FormValidator.FieldType.EMAIL);
        if(hasEmailError != null){
            editTextEmail.setError(getString(hasEmailError));
            return false;
        }else{
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
}
