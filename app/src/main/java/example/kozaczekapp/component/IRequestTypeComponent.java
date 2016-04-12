package example.kozaczekapp.component;


import javax.inject.Singleton;

import dagger.Component;
import example.kozaczekapp.module.TimeZoneModule;
import example.kozaczekapp.timeZoneApi.TimeZone;

@Singleton
@Component(modules = TimeZoneModule.class)
public interface IRequestTypeComponent {
    void inject(TimeZone timeZone);
}
