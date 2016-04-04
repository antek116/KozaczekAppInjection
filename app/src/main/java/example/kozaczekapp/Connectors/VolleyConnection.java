package example.kozaczekapp.Connectors;


import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import example.kozaczekapp.ConnectionProvider.IConnection;

public class VolleyConnection implements IConnection {

    private Context context;
    private String result = null;
    private String encoding;

    /**
     * Constructor
     * @param context of application.
     */
    public VolleyConnection(Context context) {
        this.context = context;
    }

    /**
     * Method used to get Response from Server.
     *
     * @param mBaseUrl Url to service as String
     * @return response from service as String.
     */
    @Override
    public String getResponse(String mBaseUrl) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest req = new StringRequest(Request.Method.GET, mBaseUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                result = response;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(req);
        while (result == null) {
        }
        return result;
    }

    /**
     * Sets encoding for current connection
     *
     * @param encoding to be set
     */
    @Override
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    /**
     * @return Encoding for current connection
     */
    @Override
    public String getEncoding() {
        return encoding;
    }
}

