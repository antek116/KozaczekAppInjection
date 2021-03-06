package example.kozaczekapp.authenticator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import example.kozaczekapp.R;

/**
 *  Used to validation email, name and surname format is correct
 */
public final class FormValidator {

    private static FormValidator instance = null;

    enum FieldType {EMAIL, NAME, SURNAME}

    private FormValidator(){}

    /**
     *
     * @return singleton instance of FormValidator
     */
    public static FormValidator getInstance(){
        if(instance == null){
            return instance =  new FormValidator();
        }
        else{
            return instance;
        }
    }

    /**
     *
     * @param text text in String that have to be checked.
     * @param fieldType enum EMAIL,NAME OR SURNAME
     * @return null if text isValid, id of Error in string.xml if text is invalid.
     */
    public Integer isValid(String text, FieldType fieldType) {
        switch (fieldType) {
            case EMAIL:
                return isEmailValid(text);
            case NAME:
                return isNameValid(text);
            case SURNAME:
                return isSurnameValid(text);
            default:
                return null;
        }
    }

    private Integer isSurnameValid(String surname) {
        String regExpn = "[a-zA-z]+([ '-][a-zA-Z]+)*";
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(surname);
        if(matcher.matches()){
            return null;
        }
        else{
            return R.string.wrong_surname_format;
        }
    }

    private Integer isNameValid(String name) {
        if(name.isEmpty())
            return R.string.empty_field_error;
        String regExpn = "[A-Z][a-zA-Z]*";
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        if(matcher.matches()){
            return null;
        }
        else{
            return R.string.wrong_name_format;
        }
    }

    private Integer isEmailValid(String email) {
        if(email.isEmpty())
            return R.string.empty_field_error;
        String regExpn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        if(matcher.matches()){
            return null;
        }
        else{
            return R.string.wrong_email_format;
        }
    }
}
