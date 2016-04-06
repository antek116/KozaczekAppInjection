package example.kozaczekapp.Authenticator;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Patryk Gwiazdowski on 06.04.2016.
 * // Good Job Patryk
 */
public class AuthenticatorService extends Service {
    /**
     * {@inheritDoc}
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        MyAuthenticator authenticator = new MyAuthenticator(this);
        return authenticator.getIBinder();
    }
}
