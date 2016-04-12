package example.kozaczekapp.timeZoneApi;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import javax.inject.Inject;

import example.kozaczekapp.application.MyApp;
import example.kozaczekapp.authenticator.AccountKeyConstants;
import example.kozaczekapp.authenticator.Token;
import example.kozaczekapp.connectors.MyHttpConnection;

public class TimeZone implements Runnable {

    public static final String API_KEY = "JBQLYQTBU57B";
    public static final String API_URL = "http://api.timezonedb.com/?zone=Europe/Warsaw&key=" + API_KEY + "&format=";

    private Context context;

    public TimeZone(Context context) {
        this.context = context;
    }

    @Inject
    ITimeZoneParser parser;

    @Override
    public void run() {
        MyHttpConnection connection = new MyHttpConnection();
        ((MyApp) context).getRequestTypeComponentInstance().inject(this);
        connection.getResponse(API_URL + parser.getType());
        AccountManager accountManager = AccountManager.get(context);
        Account[] account = accountManager.getAccountsByType(AccountKeyConstants.ACCOUNT_TYPE);
        accountManager.setAuthToken(account[0], AccountKeyConstants.AUTHTOKEN_TYPE_FULL_ACCESS, new Token().toString());
    }
}
