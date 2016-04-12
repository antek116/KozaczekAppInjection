package example.kozaczekapp.module;

public interface TokenProvider {
    String getFormat();
    TimeZoneParser getParser();
}
