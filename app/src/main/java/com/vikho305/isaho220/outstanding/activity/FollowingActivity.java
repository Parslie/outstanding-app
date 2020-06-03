package com.vikho305.isaho220.outstanding.activity;

import android.content.Intent;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.database.User;
import com.vikho305.isaho220.outstanding.fragment.FolloweeListFragment;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FollowingActivity extends AuthorizedActivity implements FolloweeListFragment.InteractionListener {

    private FolloweeListFragment followeeListFragment;
    private List<User> followings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followees);

        if (findViewById(R.id.frag_container) != null) {
            followeeListFragment = new FolloweeListFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, followeeListFragment).commit();
        }
        else {

        }
    }

    @Override
    public void onListItemClick(String item) {

    }

    @Override
    public void getListItems(final FolloweeListFragment listener) {
        Response.Listener<JSONArray> response = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Gson gson = new Gson();

                followings = gson.fromJson(response.toString(), new TypeToken<ArrayList<User>>(){}.getType());
                List<String> items = new ArrayList<String>();
                for (User user : followings) {
                    String username = user.getUsername() + "@" + user.getId();
                    items.add(username);
                }

                listener.onCallback(items);
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

        Intent intent = getIntent();
        User user = intent.getParcelableExtra("user");

        String url = getResources().getString(R.string.get_followings_url, user.getId(), 0);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, response, errorResponse){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + getAuthToken());
                return headers;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


}
