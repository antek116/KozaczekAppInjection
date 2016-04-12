package example.kozaczekapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

public abstract class BaseActivity extends Activity {

    interface OnConnectionChangeListener {
        void  onChange(boolean isOnline);
    }

    BroadcastReceiver connectionReciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //for each OnConnectionChangeListener  call onchange()

        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);


        //jak mamy konto:
            // - czy token is valid?
                //tak -> nic nie rob
                //nie -> otworz signup activity do odswieżenia tokenu + zakoncz obecne (finish())


        //jak nie mamy konta:
            //otworz signup activity do zalozenia konta

    }


    @Override
    protected void onResume() {
        super.onResume();

        //rejestruj broadcast na polaczenie
    }


    addConnetionChangeListener() { //w jakiejs kolekcji trzymamy listenery}
    removeConnetionChangeListener()



}


class MyArticleListFragment extends Fragment implements BaseActivity.OnConnectionChange {



    @Override
    public void onResume() {
        super.onResume();
        ((BaseActivity)getActivity()).addConnectionChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((BaseActivity)getActivity()).removeConnectionChangeListener(this);
    }

    @Override
    public void onChange(boolean isOnline) {

    }
}
