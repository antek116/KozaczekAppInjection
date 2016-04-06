package example.kozaczekapp.Application;

import android.app.Application;

import example.kozaczekapp.Component.DaggerIConnectionComponent;
import example.kozaczekapp.Component.IConnectionComponent;
import example.kozaczekapp.Module.ConnectionModule;

public class MyApp extends Application {

    private static IConnectionComponent componentInstance = null;

    @Override
    public void onCreate() {
        super.onCreate();

        componentInstance = DaggerIConnectionComponent
                .builder()
                .connectionModule(new ConnectionModule(getBaseContext()))
                .build();
    }

    /**
     *
     * @return Singleton istance of IconnectionnCommponent.
     */
    public  IConnectionComponent getComponentInstance() {
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
