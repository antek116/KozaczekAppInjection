package example.kozaczekapp.Authenticator;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import example.kozaczekapp.R;

/**
 * Created by Patryk Gwiazdowski on 06.04.2016.
 * // Good Job Patryk
 */
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
    private Button buttonConfirm;
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
        buttonConfirm = (Button) findViewById(R.id.buttonConfirmLogin);

    }

    /**
     * Called when submitting form
     * It gets field values from form, adds them to intent and calls finishLogin(Intent)
     */
    public void submit() {
        Intent intent = new Intent();
        intent.putExtra(AccountKeyStorage.KEY_ACCOUNT_NAME, editTextName.getText().toString());
        intent.putExtra(AccountKeyStorage.KEY_ACCOUNT_SURNAME, editTextSurname.getText().toString());
        intent.putExtra(AccountKeyStorage.KEY_ACCOUNT_EMAIL, editTextEmail.getText().toString());
        finishLogin(intent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Creates or updates Account with data specified in intent
     * @param intent created in submit class
     */
    private void finishLogin(Intent intent) {
        accountManager.addAccountExplicitly()
    }
}
