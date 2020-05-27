package com.vikho305.isaho220.outstanding.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.fragment.FollowerListFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FollowerActivity extends AuthorizedActivity implements FollowerListFragment.InteractionListener {

    private FollowerListFragment followerListFragment;
    private Button followRequestsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        if (findViewById(R.id.frag_container) != null) {
            followerListFragment = new FollowerListFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, followerListFragment).commit();
        }
        else {

        }

        followRequestsButton = findViewById(R.id.followRequests_button);
        followRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFollowRequests();
            }
        });
    }

    private void goToFollowRequests() {
        Intent intent = new Intent(this, FollowRequestActivity.class);
        goToActivity(intent);
    }

    @Override
    public void onListItemClick(String item) {

    }

    @Override
    public void getListItems(final FollowerListFragment listener) {
        Response.Listener<JSONObject> response = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Gson gson = new Gson();
                    List<String> items = gson.fromJson(response.getString("grupper"), new TypeToken<ArrayList<String>>(){}.getType());
                    listener.onCallback(items);
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorResponse = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());

                List<String> errorItems = new ArrayList<>();
                errorItems.add("error_one");
                errorItems.add("error_two");
                errorItems.add("error_three");
                listener.onCallback(errorItems);
            }
        };

        JsonObjectRequest request = new JsonObjectRequest(JsonObjectRequest.Method.GET, "https://tddd80server.herokuapp.com/grupper", new JSONObject(), response, errorResponse);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


}
