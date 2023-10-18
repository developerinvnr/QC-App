package com.example.qc.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

import com.example.qc.utils.AppConfig;
import com.example.qc.utils.CommonVolleyCall;

import java.util.Map;

public class SendVolleyCall {
    public void executeProgram(Context context, String program_name, final Map<String, String> params, final volleyCallback volleyCallbackObj) {
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.COMMON_URL + program_name, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                volleyCallbackObj.onSuccess(response);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                volleyCallbackObj.onError(error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                return params;
            }

        };

        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CommonVolleyCall.getInstance(context).addRequestQueue(strReq);
    }

    public void executeProgramNotification(Context context, String program_name, final Map<String, String> params, final volleyCallback volleyCallbackObj) {
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.COMMON_URL + program_name, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                volleyCallbackObj.onSuccess(response);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                volleyCallbackObj.onError(error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                return params;
            }

        };

        // Adding request to request queue
        //AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        strReq.setRetryPolicy(new DefaultRetryPolicy(300000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CommonVolleyCall.getInstance(context).addRequestQueue(strReq);
    }

}
