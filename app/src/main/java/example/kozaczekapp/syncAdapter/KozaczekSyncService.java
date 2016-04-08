package example.kozaczekapp.syncAdapter;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class KozaczekSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static KozaczekSyncAdapter kozaczekSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock){
            if(kozaczekSyncAdapter == null){
                kozaczekSyncAdapter = new KozaczekSyncAdapter(getApplicationContext(),true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return kozaczekSyncAdapter.getSyncAdapterBinder();
    }
}
