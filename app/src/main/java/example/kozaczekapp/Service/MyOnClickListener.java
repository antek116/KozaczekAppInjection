package example.kozaczekapp.Service;


import android.content.Intent;
        import android.net.Uri;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.Toast;

        import example.kozaczekapp.MainActivity;
        import example.kozaczekapp.R;

/**
 * @{inheridoc}
 */
public class MyOnClickListener implements View.OnClickListener {
    String urlToArticle;
    MainActivity activity;
    public MyOnClickListener(String url){
        this.urlToArticle = url;
    };
    public MyOnClickListener(MainActivity activity){
        this.activity = activity;
    }

    // FIXME ta klasa jest niepotrzebna
    /**
     * Called when a view has been clicked.
     * @param v View The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.CardView :
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(urlToArticle));
                v.getContext().startActivity(i);
                break;
            case R.id.refresh :
                if(activity.checkNetworkConnection()) {
                    activity.startOrStopRefreshingAnimation(true, 1);
                    activity.startService(activity.getKozaczekServiceIntent());
                } else {
                    activity.startOrStopRefreshingAnimation(true, 2);
                    Toast.makeText(activity.getApplicationContext(), "No internet connection", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }
}
