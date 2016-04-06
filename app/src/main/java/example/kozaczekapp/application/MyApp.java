package example.kozaczekapp.application;

import android.app.Application;

import example.kozaczekapp.component2.DaggerIConnectionComponent;
import example.kozaczekapp.component2.IConnectionComponent;
import example.kozaczekapp.module2.ConnectionModule;

/**
 * Creating Singleton IConnectionComponent instance
 */
public class MyApp extends Application {

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
}
