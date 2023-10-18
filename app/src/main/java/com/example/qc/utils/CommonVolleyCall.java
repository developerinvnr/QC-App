package com.example.qc.utils;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class CommonVolleyCall {
    private static Context mcontext;
    private static CommonVolleyCall commonVolleyCall;
    private RequestQueue requestQueue;


    CommonVolleyCall(Context context) {
        mcontext = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mcontext.getApplicationContext());

        }
        return requestQueue;
    }

    public static synchronized CommonVolleyCall getInstance(Context context) {
        if (commonVolleyCall == null) {
            commonVolleyCall = new CommonVolleyCall(context);
        }
        return commonVolleyCall;
    }

    public <T> void addRequestQueue(Request<T> request) {
        requestQueue.add(request);

    }
}
