package example.kozaczekapp.module;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import example.kozaczekapp.responseTypeProvider.IResponseType;

@Module
public class RequestTypeModule {
    private static final String JSON = "Json";
    private static final String XML = "Xnl";

    @Provides
    @Singleton
    IResponseType provideIRequestType(){
    return null;
    }
}
