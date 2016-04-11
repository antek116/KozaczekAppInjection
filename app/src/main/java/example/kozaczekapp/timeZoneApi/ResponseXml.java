package example.kozaczekapp.timeZoneApi;

import example.kozaczekapp.responseTypeProvider.IResponseType;

public class ResponseXml implements IResponseType {

    private static final String FORMAT = "&format=xml";

    @Override
    public String getUrlWithResponseType() {
        return url + zone + FORMAT +apiKey;
    }
}
