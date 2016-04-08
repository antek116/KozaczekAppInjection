package example.kozaczekapp.authenticator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCheckIfValidWhenValid() throws Exception {
        // given
        Token token = new Token();
        //when
        boolean isValid = token.checkIfValid();
        // then
        assertTrue(isValid);
    }


    @Test
    public void testCheckIfValidWhenInvalid() throws Exception {
        // given
        Token token = new Token();
        token.setInvalid();
        //when
        boolean isValid = token.checkIfValid();
        // then
        assertFalse(isValid);
    }

}