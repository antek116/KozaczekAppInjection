package example.kozaczekapp.Application;

import org.junit.Before;
import org.junit.Test;

import example.kozaczekapp.Component.IConnectionComponent;

import static org.junit.Assert.*;

public class MyAppTest {

    MyApp myApp;
    @Before
    public void setup(){
        myApp = new MyApp();
    }

    @Test
    public void shouldReturnTheSameReferenceToMyAppInstance(){
        //given
        MyApp mySecoundApp = new MyApp();;
        //when
        IConnectionComponent componentSecound= mySecoundApp.getComponentInstance();
        IConnectionComponent componentOne = myApp.getComponentInstance();
        //then
        assertEquals(componentOne,componentSecound);



    }

}