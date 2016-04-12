package example.kozaczekapp.application;

import android.app.Application;

import example.kozaczekapp.component.DaggerIConnectionComponent;
import example.kozaczekapp.component.DaggerIRequestTypeComponent;
import example.kozaczekapp.component.IConnectionComponent;
import example.kozaczekapp.component.IRequestTypeComponent;
import example.kozaczekapp.module.ConnectionModule;
import example.kozaczekapp.module.TimeZoneModule;

/**
 * Creating Singleton IConnectionComponent instance
 */
public class MyApp extends Application {

    private static IRequestTypeComponent requestTypeComponent = null;
    private static IConnectionComponent componentInstance = null;

    /**
     *
     * @return Singleton istance of IconnectionnCommponent.
     */
    public IConnectionComponent getComponentInstance(){
        if (componentInstance == null) {
            return componentInstance = DaggerIConnectionComponent
                    .builder()
                    .connectionModule(new ConnectionModule(getBaseContext()))
                    .build();
        } else {
            return componentInstance;
        }
    }

    /**
     *
     * @return Singleton istance of IconnectionnCommponent.
     */
    public IRequestTypeComponent getRequestTypeComponentInstance(){
        if (requestTypeComponent == null) {
            return requestTypeComponent = DaggerIRequestTypeComponent
                    .builder()
                    .timeZoneModule(new TimeZoneModule(getBaseContext()))
                    .build();
        } else {
            return requestTypeComponent;
        }
    }

}
