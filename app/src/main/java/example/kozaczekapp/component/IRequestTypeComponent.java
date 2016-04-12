package example.kozaczekapp.component;

import javax.inject.Singleton;

import dagger.Component;
import example.kozaczekapp.module.TimeZoneModule;
import example.kozaczekapp.syncAdapter.KozaczekSyncAdapter;


@Singleton
@Component(modules = TimeZoneModule.class, dependencies = {IRequestTypeComponent.class})
public interface IRequestTypeComponent {
        void inject(KozaczekSyncAdapter kozaczekSyncAdapter);
}
