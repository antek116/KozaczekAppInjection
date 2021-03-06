package example.kozaczekapp.authenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 */
public class AuthenticatorService extends Service {
    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        AccountAuthenticator authenticator = new AccountAuthenticator(this);
        return authenticator.getIBinder();
    }
}
