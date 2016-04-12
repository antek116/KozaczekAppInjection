package example.kozaczekapp.timeZoneApi;

import javax.inject.Inject;

public class TimeZoneParserProvider {
    private ITimeZoneParser provider;
    private static TimeZoneParserProvider instance = null;

    /**
     * Depending Injection in constructor.
     *
     * @param provider instance of provider witch extend IConnection interface.
     */

    private TimeZoneParserProvider(ITimeZoneParser provider) {
        this.provider = provider;
    }

    /**
     * Method used to get Response from specific URL.
     *
     * @return TimeZoneParserProvider instance
     */
    @Inject
    public static TimeZoneParserProvider getInstance(ITimeZoneParser provider){
        if(instance == null){
            instance = new TimeZoneParserProvider(provider);
        }else{
            instance.provider = provider;
        }
        return instance;
    }
}
