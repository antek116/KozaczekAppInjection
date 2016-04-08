package example.kozaczekapp.authenticator;


import java.sql.Timestamp;

public class Token {
    private Timestamp timeStampEnd;
    private final static long HOUR_IN_MILLIS = 3600000;

    public Token() {
        timeStampEnd = new Timestamp(System.currentTimeMillis() + HOUR_IN_MILLIS);
    }

    /**
     * @return isValid returns the validity state of the token
     */
    public boolean checkIfValid() {
        Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
        boolean isValid = (timeStampEnd.compareTo(currentTimeStamp) > 0);
        return  isValid;
    }

    /**
     * @return stringWithToken returns the token as a string
     */
    @Override
    public String toString() {
        String stringWithToken = timeStampEnd.toString();
        return stringWithToken;
    }

    /**
     * Sets the token as an invalid for test purpose
     */
    protected void setInvalid() {
        timeStampEnd.setTime(System.currentTimeMillis());
    }
}
