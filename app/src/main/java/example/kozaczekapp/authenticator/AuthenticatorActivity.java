package example.kozaczekapp.authenticator;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import example.kozaczekapp.R;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";
    public static final String ACCOUNT_TYPE = "example.kozaczek";

    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextEmail;

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
        AccountManager accountManager = AccountManager.get(getBaseContext());
        String name = editTextName.getText().toString();
        String surname = editTextSurname.getText().toString();
        String email = editTextEmail.getText().toString();
        final Account account = new Account(name + " " + surname, ACCOUNT_TYPE);
        accountManager.addAccountExplicitly(account, null, null);
        accountManager.setUserData(account, AccountKeyStorage.KEY_ACCOUNT_EMAIL, email);
        accountManager.setUserData(account, AccountKeyStorage.KEY_ACCOUNT_NAME, name);
        accountManager.setUserData(account, AccountKeyStorage.KEY_ACCOUNT_SURNAME, surname);
        finish();
    }
}
