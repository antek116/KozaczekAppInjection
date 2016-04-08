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
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";
    private EditText editTextName;
    private EditText editTextSurname;
    private EditText editTextEmail;
    private  FormValidator validator = FormValidator.getInstance();
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
        fieldsListenersSetUp();
    }

    /**
     * Called when submitting form
     * It gets field values from form, adds them to intent and calls finishLogin(Intent)
     */
    public void onSubmitClick(View view) {
        boolean isValidForm = isFormValid();
        if (isValidForm) {
            Intent intent = new Intent();
            intent.putExtra(AccountKeyStorage.KEY_ACCOUNT_NAME, editTextName.getText().toString());
            intent.putExtra(AccountKeyStorage.KEY_ACCOUNT_SURNAME, editTextSurname.getText().toString());
            intent.putExtra(AccountKeyStorage.KEY_ACCOUNT_EMAIL, editTextEmail.getText().toString());
            finishLogin(intent);
        }
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
        customData.putString(AccountKeyStorage.KEY_ACCOUNT_EMAIL, email);
        accountManager.addAccountExplicitly(account, null, customData);
        accountManager.setUserData(account, AccountKeyStorage.KEY_ACCOUNT_EMAIL, email);
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK);
        finish();
    }

    private boolean isFormValid() {
        FormValidator validator = FormValidator.getInstance();
        return (isEmailValid(validator) & isSurnameValid(validator) & isNameValid(validator));
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
