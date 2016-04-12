package example.kozaczekapp.module;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import example.kozaczekapp.R;
import example.kozaczekapp.timeZoneApi.JsonTimeZoneParser;
import example.kozaczekapp.timeZoneApi.ITimeZoneParser;
import example.kozaczekapp.timeZoneApi.XmlTimeZoneParser;

@Module
public class TimeZoneModule {

    private Context context;

    public TimeZoneModule(Context context){
        this.context = context;
    }

    @Provides
    @Singleton
    public ITimeZoneParser provideTokenProvider(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean checked = prefs.getBoolean(context.getString(R.string.checkBoxPreferences),false);
        if(checked){
            return new JsonTimeZoneParser();
        } else{
            return new XmlTimeZoneParser();
        }
    }
}
