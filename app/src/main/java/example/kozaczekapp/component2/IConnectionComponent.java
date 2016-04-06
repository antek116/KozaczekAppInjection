package example.kozaczekapp.component2;


import javax.inject.Singleton;

import dagger.Component;
import example.kozaczekapp.module2.ConnectionModule;
import example.kozaczekapp.service2.KozaczekService;


/**
 * interface of IconnectionComponent class used to Dagger Injection.
 */
@Singleton
@Component(modules = ConnectionModule.class)
public interface IConnectionComponent {
    void inject(KozaczekService kozaczekService);
}
