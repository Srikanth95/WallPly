package com.wallply.wallply.utilities;

import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.wallply.wallply.activities.BaseActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sree on 9/17/2017.
 */

public class CloudinaryJsonObjectRequest extends JsonObjectRequest {
    public CloudinaryJsonObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    public CloudinaryJsonObjectRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        try {
            Map<String, String> map = new HashMap<String, String>();
            String credentials = BaseActivity.cloudinaryCredentials.cloudinaryUsername+":"+BaseActivity.cloudinaryCredentials.cloudinaryPassword;
            String auth = "Basic "
                    + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            map.put("Accept", "application/json");
            map.put("Authorization", auth);
            map.put("context", "true");
            return map;
        } catch (Exception e) {
            Log.e("BaseActivity", " all images url : Authentication Filure" );
        }
        return super.getHeaders();
    }
}
