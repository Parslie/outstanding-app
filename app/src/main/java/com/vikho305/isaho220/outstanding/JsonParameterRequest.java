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

    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    @Override
    public byte[] getBody() {
        return parameters.toString().getBytes();
    }
}
