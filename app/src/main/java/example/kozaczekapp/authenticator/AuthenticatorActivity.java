package example.kozaczekapp.authenticator;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import example.kozaczekapp.R;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";

    public final static String PARAM_USER_PASS = "USER_PASS";

    private final int REQ_SIGNUP = 1;

    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextEmail;
    private AccountManager accountManager;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.login_form_layout);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextSurname = (EditText) findViewById(R.id.editTextSurname);
        editTextEmail = (EditText) findViewById(R.id.editTextEnterEmail);

    }


    /**
     * Called when submitting form
     * It gets field values from form, adds them to intent and calls finishLogin(Intent)
     */

    public void onSubmitClick(View view) {
        Intent intent = new Intent();
        intent.putExtra(AccountKeyStorage.KEY_ACCOUNT_NAME, editTextName.getText().toString());
        intent.putExtra(AccountKeyStorage.KEY_ACCOUNT_SURNAME, editTextSurname.getText().toString());
        intent.putExtra(AccountKeyStorage.KEY_ACCOUNT_EMAIL, editTextEmail.getText().toString());
        finishLogin(intent);
    }


    /**
     * Creates or updates Account with data specified in intent
     *
     * @param intent created in submit class
     */
    private void finishLogin(Intent intent) {
        accountManager = AccountManager.get(getBaseContext());
        String name = intent.getStringExtra(AccountKeyStorage.KEY_ACCOUNT_NAME);
        String surname = intent.getStringExtra(AccountKeyStorage.KEY_ACCOUNT_SURNAME);
        final Account account = new Account(name, "example.kozaczek");
        accountManager.addAccountExplicitly(account, null, null);
        setAccountAuthenticatorResult(intent.getExtras());
        finish();
    }
}
