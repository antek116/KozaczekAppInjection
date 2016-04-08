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
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";
    public static final String ACCOUNT_TYPE = "example.kozaczek";

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
            AccountManager accountManager = AccountManager.get(getBaseContext());
            String name = editTextName.getText().toString();
            String surname = editTextSurname.getText().toString();
            String email = editTextEmail.getText().toString();
            final Account account = new Account(name + " " + surname, ACCOUNT_TYPE);
            accountManager.addAccountExplicitly(account, null, null);
            accountManager.setUserData(account, AccountKeyConstants.KEY_ACCOUNT_EMAIL, email);
            accountManager.setUserData(account, AccountKeyConstants.KEY_ACCOUNT_NAME, name);
            accountManager.setUserData(account, AccountKeyConstants.KEY_ACCOUNT_SURNAME, surname);
            finish();
        }
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
