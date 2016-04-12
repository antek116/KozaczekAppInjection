package example.kozaczekapp.timeZoneApi;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TimeZoneModule {

    @Provides
    @Singleton
    public TokenProvider provideTokenProvider(){
        return null;
    }
}
