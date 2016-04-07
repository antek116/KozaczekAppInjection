package example.kozaczekapp.authenticator;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import example.kozaczekapp.R;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";
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

        //FormValidation.
        Button confirmButton = (Button) findViewById(R.id.buttonConfirmLogin);
        confirmButton.setClickable(false);
        editTextEmail.addTextChangedListener(new GenericTextWatcher(editTextEmail, confirmButton));
        editTextName.addTextChangedListener(new GenericTextWatcher(editTextName, confirmButton));
        editTextSurname.addTextChangedListener(new GenericTextWatcher(editTextSurname, confirmButton));
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
        AccountManager accountManager = AccountManager.get(getBaseContext());
        String name = intent.getStringExtra(AccountKeyStorage.KEY_ACCOUNT_NAME);
        String surname = intent.getStringExtra(AccountKeyStorage.KEY_ACCOUNT_SURNAME);
        String email = intent.getStringExtra(AccountKeyStorage.KEY_ACCOUNT_EMAIL);
        final Account account = new Account(name + " " + surname, "example.kozaczek");
        Bundle customData = new Bundle();
        customData.putString(AccountKeyStorage.KEY_ACCOUNT_EMAIL,email);
        accountManager.addAccountExplicitly(account, null, customData);
        accountManager.setUserData(account, AccountKeyStorage.KEY_ACCOUNT_EMAIL, email);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK);
        finish();
    }
}
