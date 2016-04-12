package example.kozaczekapp.authenticator;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void checkIfValidWhenValid() throws Exception {
        // given
        Token token = new Token();
        //when
        boolean isValid = token.checkIfValid();
        // then
        assertTrue(isValid);
    }

    @Test
    public void checkIfValidWhenInvalid() throws Exception {
        // given
        Token token = new Token();
        token.setInvalid();
        //when
        boolean isValid = token.checkIfValid();
        // then
        assertFalse(isValid);
    }

    @Test
    public void checkIfValidWhenValidAfterGetToken() throws Exception {
        // given
        String validity = "2016-04-12 00:18:38.508";
        Token token = new Token(validity);
        //when
        boolean isValid = token.checkIfValid();
        // then
        assertTrue(isValid);
    }

    @Test
    public void checkIfValidWhenInvalidAfterGetToken() throws Exception {
        // given
        String validity = "2016-04-11 00:18:38.508";
        Token token = new Token(validity);
        //when
        boolean isValid = token.checkIfValid();
        // then
        assertFalse(isValid);
    }

    @Test
    public void generateTokenWhenSuccess() throws Exception {
        // given
        Token token = new Token();
        token.setInvalid();
        //when
        token.generateToken();
        boolean isValid = token.checkIfValid();
        // then
        assertTrue(isValid);
    }
}