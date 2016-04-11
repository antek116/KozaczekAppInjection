package example.kozaczekapp.authenticator;

import java.sql.Timestamp;

public class Token {

    private final static long HOUR_IN_MILLIS = 60000;
    private Timestamp timeStampEnd;

    public Token() {
        timeStampEnd = new Timestamp(getValidity());
    }

    public Token(String validity) {
        timeStampEnd = Timestamp.valueOf(validity);
    }

    /**
     * @return isValid returns the validity state of the token
     */
    public boolean checkIfValid() {
        Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
        boolean isValid = false;
        if (timeStampEnd.compareTo(currentTimeStamp) > 0) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Generates new token.
     */
    public void generateToken() {
        timeStampEnd.setTime(getValidity());
    }

    /**
     * @return stringWithToken returns the token as a string
     */
    @Override
    public String toString() {
        return timeStampEnd.toString();
    }

    /**
     * @return validity returns the end of 1 hour validity in millis
     */
    private long getValidity() {
        return System.currentTimeMillis() + HOUR_IN_MILLIS;
    }

    /**
     * Sets the token as an invalid for test purpose
     */
    protected void setInvalid() {
        timeStampEnd.setTime(System.currentTimeMillis());
    }
}
