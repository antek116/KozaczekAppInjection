package example.kozaczekapp.authenticator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import example.kozaczekapp.R;

import static org.junit.Assert.*;

public class FormValidatorTest {

    FormValidator formValidator;
    @Before
    public void setUp() throws Exception {
        formValidator = FormValidator.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        formValidator = null;
    }


    @Test
    public void shouldReturnTheSameInstance(){
        //given
        //when
        FormValidator formValidatorNew = FormValidator.getInstance();
        //them
        assertEquals(formValidator,formValidatorNew);
    }
    @Test
    public void shouldReturnNull_emailIsValid(){
        //given
        //when
        Integer isValid=formValidator.isValid("ant@gmail.com", FormValidator.FieldType.EMAIL);
        //
        assertEquals(null,isValid);
    }
    @Test
    public void shoulReturnWrongEmailFormatError_emailIsIncorrect(){
        //given
        //when
        Integer isValid=formValidator.isValid("ant@.com", FormValidator.FieldType.EMAIL);
        //then
        assertEquals((Integer)R.string.wrong_email_format,isValid);
    }
    @Test
    public void shouldReturnFieldEmptyError_emailIsEmpty(){
        //given
        //when
        Integer isValid=formValidator.isValid("", FormValidator.FieldType.EMAIL);
        //then
        assertEquals((Integer)R.string.empty_field_error,isValid);
    }
}