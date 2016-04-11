package example.kozaczekapp.responseTypeProvider;

public interface IResponseType {
    String zone = "/?zone=Europe/Warsaw";
    String url = "http://api.timezonedb.com";
    String apiKey = "&key=JBQLYQTBU57B";
    String getUrlWithResponseType();
}
