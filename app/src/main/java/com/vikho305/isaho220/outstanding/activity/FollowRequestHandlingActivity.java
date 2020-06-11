package com.vikho305.isaho220.outstanding.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.vikho305.isaho220.outstanding.JsonParameterRequest;
import com.vikho305.isaho220.outstanding.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FollowRequestHandlingActivity extends AuthorizedActivity  {

    private Button acceptButton, declineButton;
    private TextView usernameTextView;
    private String id, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_request_handling);

        // Get layout views
        usernameTextView = findViewById(R.id.requestHandling_username);
        acceptButton = findViewById(R.id.accept_button);
        declineButton = findViewById(R.id.decline_button);

        // Init activity
        usernameTextView.setText(username);

        Intent intent = getIntent();
        String u = intent.getStringExtra("username");

        String[] parts = Objects.requireNonNull(u).split("@");
        username = parts[0];
        id = parts[1];

        // Init listeners
        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept();
            }
        });
        declineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decline();
            }
        });
    }

    private void accept() {
        JsonParameterRequest request = new JsonParameterRequest(
                Request.Method.POST,
                getResources().getString(R.string.accept_follow_url, id),
                null,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        acceptButton.setEnabled(false);
                        declineButton.setEnabled(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getAuthToken());
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    private void decline() {
        JsonParameterRequest request = new JsonParameterRequest(
                Request.Method.POST,
                getResources().getString(R.string.reject_follow_url, id),
                null,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        acceptButton.setEnabled(false);
                        declineButton.setEnabled(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getAuthToken());
                return headers;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

}
