package example.kozaczekapp.authenticator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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