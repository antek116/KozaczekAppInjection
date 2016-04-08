package example.kozaczekapp.authenticator;


import java.sql.Timestamp;

public class Token {
    private final static long HOUR_IN_MILLIS = 3600000;
    private Timestamp timeStampEnd;

    public Token() {
        timeStampEnd = new Timestamp(getValidity());
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
        String stringWithToken = timeStampEnd.toString();
        return stringWithToken;
    }

    /**
     * @return validity returns the end of 1 hour validity in millis
     */
    private long getValidity() {
        long validity = System.currentTimeMillis() + HOUR_IN_MILLIS;
        return validity;
    }

    /**
     * Sets the token as an invalid for test purpose
     */
    protected void setInvalid() {
        timeStampEnd.setTime(System.currentTimeMillis());
    }
}
