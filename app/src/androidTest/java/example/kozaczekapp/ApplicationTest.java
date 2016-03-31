package example.kozaczekapp;

import android.app.Application;
import android.test.ApplicationTestCase;

import org.junit.Test;

import java.util.ArrayList;

import example.kozaczekapp.KozaczekItems.Article;
import example.kozaczekapp.Service.Parser;

import static org.junit.Assert.assertNull;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
}