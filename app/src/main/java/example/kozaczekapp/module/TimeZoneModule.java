package example.kozaczekapp.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class TimeZoneModule {

    @Provides
    @Singleton
    public TokenProvider provideTokenProvider() {

        //shared pref . get token
        //if json
        //return json token provier


//        if xml
//        return xml token provider
        return null;


    }

}
