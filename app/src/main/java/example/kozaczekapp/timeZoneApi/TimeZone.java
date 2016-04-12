package example.kozaczekapp.timeZoneApi;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.inject.Inject;

import example.kozaczekapp.application.MyApp;
import example.kozaczekapp.authenticator.AccountKeyConstants;
import example.kozaczekapp.authenticator.AuthenticatorActivity;
import example.kozaczekapp.authenticator.Token;
import example.kozaczekapp.connectionProvider.IConnection;
import example.kozaczekapp.connectors.MyHttpConnection;

public class TimeZone implements Runnable {

    public static final String API_KEY = "JBQLYQTBU57B";
    public static final String API_URL = "http://api.timezonedb.com/?zone=Europe/Warsaw&key=" + API_KEY + "&format=";

    private Context context;
    private AuthenticatorActivity activity;

    public TimeZone(Context context, AuthenticatorActivity activity) {
        this.context = context;
        this.activity = activity;
    }

    @Inject
    ITimeZoneParser parser;


    @Override
    public void run() {
        MyHttpConnection connection = new MyHttpConnection();
        ((MyApp) context).getRequestTypeComponentInstance().inject(this);
        String timeStamp = parser.parseResponse(connection.getResponse(API_URL + parser.getType()));
        AccountManager accountManager = AccountManager.get(context);
        Account[] account = accountManager.getAccountsByType(AccountKeyConstants.ACCOUNT_TYPE);
        Calendar cal= new GregorianCalendar(java.util.TimeZone.getTimeZone("GMT"));
        cal.setTimeInMillis(Integer.valueOf(timeStamp) * 1000L);
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        fmt.setCalendar(cal);
        String dateFormatted = fmt.format(cal.getTime());
        accountManager.setAuthToken(account[0], AccountKeyConstants.AUTHTOKEN_TYPE_FULL_ACCESS, new Token(dateFormatted).toString());
    }
}
