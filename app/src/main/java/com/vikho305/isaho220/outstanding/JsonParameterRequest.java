package com.vikho305.isaho220.outstanding;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

public class JsonParameterRequest extends StringRequest {

    private JSONObject parameters;

    public JsonParameterRequest(int method, String url, JSONObject parameters, Response.Listener<String> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, listener, errorListener);
        this.parameters = parameters;
    }

    // Enables sending in json parameters
    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    // Returns parameters as bytes and handles null parameters
    @Override
    public byte[] getBody() {
        if (parameters != null)
            return parameters.toString().getBytes();
        else
            return new JSONObject().toString().getBytes();
    }
}
