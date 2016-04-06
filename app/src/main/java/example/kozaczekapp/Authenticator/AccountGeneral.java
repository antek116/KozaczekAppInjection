package example.kozaczekapp.Authenticator;

interface AccountGeneral {
    /**
     * Account type id
     */
    String ACCOUNT_TYPE = "com.udinic.auth_example";

    /**
     * Account name
     */
    public static final String ACCOUNT_NAME = "Udinic";

    /**
     * Auth token types
     */
    String AUTHTOKEN_TYPE_READ_ONLY = "Read only";
    String AUTHTOKEN_TYPE_READ_ONLY_LABEL = "Read only access to an Udinic account";

    String AUTHTOKEN_TYPE_FULL_ACCESS = "Full access";
    String AUTHTOKEN_TYPE_FULL_ACCESS_LABEL = "Full access to an Udinic account";
//    ServerAuthenticate sServerAuthenticate = new ParseComServerAuthenticate();

}
