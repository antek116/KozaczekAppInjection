package example.kozaczekapp.authenticator;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import example.kozaczekapp.R;

public class GenericTextWatcher implements TextWatcher {

    private final Button confirmButton;
    private View view;
    private static final String WRONG_EMAIL_VALIDATION = "Wrong Email validation";
    private static final String WRONG_NAME_VALIDATION = "Wrong Name validation";
    private static final String WRONG_SURNAME_VALIDATION = "Wrong Surname validation";
    private static final String BUTTON_NOT_CLICKABLE = "Fields empty or not well formatted";
    private String errorMessage = "Something goes wrong";
    private String EMPTY_FIELD = "Empty Field";
    private static Set<Integer> wrongEditFieldsList = new HashSet<>();


    public GenericTextWatcher(View view, Button confirmButton) {
        this.view = view;
        this.confirmButton = confirmButton;
        errorMessage = EMPTY_FIELD;
        EditText field = (EditText) view.findViewById((view.getId()));
        if (field.getText().toString().isEmpty()) {
            wrongEditFieldsList.add(view.getId());
            confirmButton.setClickable(false);
        }
    }

    private enum wrongField {ADD, REMOVE}

    /**
     *{@inheritDoc}
     */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        switch (view.getId()) {
            case R.id.editTextEnterEmail:
                emailValidator(text);
                confirmButtonSetUp();
                break;
            case R.id.editTextName:
                nameValidator(text);
                confirmButtonSetUp();
                break;
            case R.id.editTextSurname:
                surnameValidator(text);
                confirmButtonSetUp();
                break;
            default:
                confirmButtonSetUp();

        }

    }

    private void emailValidator(String email) {
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        errorSetUp(matcher.matches() || email.isEmpty(), R.id.editTextEnterEmail);
    }

    private void nameValidator(String name) {
        String regExpn = "[A-Z][a-zA-Z]*";
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        errorSetUp(matcher.matches() || name.isEmpty(), R.id.editTextName);
    }

    private void surnameValidator(String surname) {
        String regExpn = "[a-zA-z]+([ '-][a-zA-Z]+)*";
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(surname);
        errorSetUp(matcher.matches() || surname.isEmpty(), R.id.editTextSurname);
    }

    private void errorSetUp(boolean textMatched, int itemId) {
        EditText textField = (EditText) view.findViewById(itemId);
        errorMessageSetup(itemId);
        if (textMatched) {
            wrongFieldsSetUp(itemId, wrongField.REMOVE);
        } else {
            wrongFieldsSetUp(itemId, wrongField.ADD);
            textField.setError(errorMessage);
        }
    }

    private void errorMessageSetup(int itemId) {
        switch (itemId) {
            case R.id.editTextEnterEmail:
                errorMessage = WRONG_EMAIL_VALIDATION;
                break;
            case R.id.editTextName:
                errorMessage = WRONG_NAME_VALIDATION;
                break;
            case R.id.editTextSurname:
                errorMessage = WRONG_SURNAME_VALIDATION;
                break;
        }
    }

    private void wrongFieldsSetUp(int itemId, wrongField field) {
        if (field == wrongField.ADD) {
            wrongEditFieldsList.add(itemId);
        }
        if(field == wrongField.REMOVE){
            wrongEditFieldsList.remove(itemId);
        }
    }

    private void confirmButtonSetUp() {
        if(wrongEditFieldsList.size() == 0){
            confirmButton.setClickable(true);
        }
        if(confirmButton.isFocused()){
            confirmButton.setError(BUTTON_NOT_CLICKABLE);
        }
    }
}
