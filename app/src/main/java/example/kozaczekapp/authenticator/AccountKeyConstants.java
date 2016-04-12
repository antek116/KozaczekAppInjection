package example.kozaczekapp.authenticator;

/**
 * Stores key strings for accounts.
 */
public final class AccountKeyConstants {
    public static final String KEY_ACCOUNT_NAME = "Account_Name";
    public static final String KEY_ACCOUNT_SURNAME = "Account_Surname";
    public static final String KEY_ACCOUNT_EMAIL = "Account_Email";
    public static final String AUTHTOKEN_TYPE_READ_ONLY = "Read only";
    public static final String AUTHTOKEN_TYPE_READ_ONLY_LABEL = "Read only access to account";
    public static final String AUTHTOKEN_TYPE_FULL_ACCESS = "Full access";
    public static final String AUTHTOKEN_TYPE_FULL_ACCESS_LABEL = "Full access to account";
    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_CLICKED_FROM_SETTINGS = "IS_ADDING_ACCOUNT";
    //TODO get this from string resource
    public static final String ACCOUNT_TYPE = "example.kozaczek";

    public static final String ARG_IS_ADDING_NEW_ACCOUNT = "NEW_ACCOUNT";
    public static final String ACTION_DISPLAY_LOGIN = "display.login.screen";
    public static final String ACTION_TOKEN_DOWNLOADED = "can.download.new.data";
}
