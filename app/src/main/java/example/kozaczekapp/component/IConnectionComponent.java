package example.kozaczekapp.component;


import javax.inject.Singleton;

import dagger.Component;
import example.kozaczekapp.module.ConnectionModule;
import example.kozaczekapp.syncAdapter.KozaczekSyncAdapter;


/**
 * interface of IconnectionComponent class used to Dagger Injection.
 */
@Singleton
@Component(modules = ConnectionModule.class)
public interface IConnectionComponent {
    void inject(KozaczekSyncAdapter kozaczekSyncAdapter);
}
