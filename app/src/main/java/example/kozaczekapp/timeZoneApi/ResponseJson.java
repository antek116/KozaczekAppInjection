package example.kozaczekapp.timeZoneApi;

import example.kozaczekapp.responseTypeProvider.IResponseType;

public class ResponseJson implements IResponseType {


    private static final String FORMAT = "&format=json";

    @Override
    public String getUrlWithResponseType() {
        return url + zone + FORMAT +apiKey;
    }
}
