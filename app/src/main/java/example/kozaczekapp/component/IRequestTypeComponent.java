package example.kozaczekapp.component;

import javax.inject.Singleton;

import dagger.Component;
import example.kozaczekapp.module.RequestTypeModule;
import example.kozaczekapp.module.TimeZoneModule;
import example.kozaczekapp.syncAdapter.KozaczekSyncAdapter;


@Singleton
@Component(modules = TimeZoneModule.class)
public interface IRequestTypeComponent {
//    void inject(KozaczekSyncAdapter kozaczekSyncAdapter);
}
