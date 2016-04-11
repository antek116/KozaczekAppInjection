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