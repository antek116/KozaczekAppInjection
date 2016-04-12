package example.kozaczekapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;

public class MyProperMainActivity extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        //widoki, dodanie fragmentu z lista i synciem


    }


    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return super.onMenuItemSelected(featureId, item);

        //logout or remove account->
        //jak sie uda to start mainActivity + clear stack
    }
}
