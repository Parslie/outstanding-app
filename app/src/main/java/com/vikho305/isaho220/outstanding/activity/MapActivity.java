package com.vikho305.isaho220.outstanding.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.OnLocationClickListener;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.Symbol;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolOptions;
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.database.Post;
import com.vikho305.isaho220.outstanding.database.User;
import com.vikho305.isaho220.outstanding.viewmodel.MapViewModel;

import java.util.List;
import java.util.Objects;

public class MapActivity extends AuthorizedActivity implements OnMapReadyCallback,
        PermissionsListener, View.OnClickListener {

    private static final int POST_CREATION_REQUEST = 0;
    private static final String TEXT_ICON = "text";
    private static final String PICTURE_ICON = "picture";
    private static final int POST_RADIUS = 1000;

    private MapView mapView;
    private FloatingActionButton makePostButton, trackingModeButton;

    private MapViewModel mapViewModel;

    private SymbolManager postManager, followingManager;
    private LocationComponent locationComponent;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoiaXNha2hvcnZhdGgiLCJhIjoiY2s4dnZ3NWoxMDN1azNncXBuNWl0YWttdSJ9.ksNJFJjaQsqnIFzKy3ZCkg");
        setContentView(R.layout.activity_map);

        // Get layout views
        mapView = findViewById(R.id.map_mapView);
        makePostButton = findViewById(R.id.map_postButton);
        trackingModeButton = findViewById(R.id.map_trackingModeButton);

        // Init view model
        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        mapViewModel.getPosts().observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                updatePostSymbols(posts);
            }
        });
        mapViewModel.getOnlineFollowings().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                updateFollowingSymbols(users);
            }
        });

        // Init activity
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        // Init listeners
        makePostButton.setOnClickListener(this);
        trackingModeButton.setOnClickListener(this);
    }

    private void goToProfile(User user) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("user", user);
        goToActivity(intent);
    }

    private void goToProfile(String userId) {
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("userId", userId);
        goToActivity(intent);
    }

    private void goToPost(Post post) {
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra("post", post);
        goToActivity(intent);
    }

    private void goToPostCreation() {
        Intent intent = new Intent(this, PostCreationActivity.class);
        goToActivityForResult(intent, POST_CREATION_REQUEST);
    }

    private void updatePostSymbols(List<Post> posts) {
        postManager.deleteAll();

        // Add all posts as symbols with their own
        for (Post post : posts) {
            if(post.getMediaType().equals(Post.TEXT_TYPE)){
                postManager.create(new SymbolOptions()
                        .withLatLng(new LatLng(post.getLatitude(), post.getLongitude()))
                        .withIconImage(TEXT_ICON)
                        .withIconSize(1.25f));
            }
            else if(post.getMediaType().equals(Post.IMAGE_TYPE)){
                postManager.create(new SymbolOptions()
                        .withLatLng(new LatLng(post.getLatitude(), post.getLongitude()))
                        .withIconImage(PICTURE_ICON)
                        .withIconSize(1.25f));
            }
        }
    }

    private void updateFollowingSymbols(List<User> users) {
        followingManager.deleteAll();

        // Add all users as symbols
        for (User user : users) {
            followingManager.create(new SymbolOptions()
                    .withLatLng(new LatLng(user.getLatitude(), user.getLongitude()))
                    .withIconImage(TEXT_ICON) // TODO: set image to profile picture
                    .withIconSize(1.25f));
        }
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.LIGHT, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                // Init symbol icons
                style.addImage(TEXT_ICON,
                        Objects.requireNonNull(BitmapUtils.getBitmapFromDrawable(ContextCompat.getDrawable(MapActivity.this, R.drawable.ic_text_24dp))),
                        true);
                style.addImage(PICTURE_ICON,
                        Objects.requireNonNull(BitmapUtils.getBitmapFromDrawable(ContextCompat.getDrawable(MapActivity.this, R.drawable.ic_image_24dp))),
                        true);

                // Init symbol managers
                postManager = new SymbolManager(mapView, mapboxMap, style);
                postManager.setIconAllowOverlap(true);
                postManager.setTextAllowOverlap(true);
                followingManager = new SymbolManager(mapView, mapboxMap, style);
                followingManager.setIconAllowOverlap(true);
                followingManager.setTextAllowOverlap(true);

                // Init symbol click listeners
                postManager.addClickListener(new OnSymbolClickListener() {
                    @Override
                    public void onAnnotationClick(Symbol symbol) {
                        LatLng latLng = symbol.getLatLng();
                        double lat = latLng.getLatitude();
                        double lng = latLng.getLongitude();

                        Post post = mapViewModel.getPostAt(lat, lng);
                        if (post != null)
                            goToPost(post);
                    }
                });
                followingManager.addClickListener(new OnSymbolClickListener() {
                    @Override
                    public void onAnnotationClick(Symbol symbol) {
                        LatLng latLng = symbol.getLatLng();
                        double lat = latLng.getLatitude();
                        double lng = latLng.getLongitude();

                        User user = mapViewModel.getFollowingAt(lat, lng);
                        if (user != null)
                            goToProfile(user);
                    }
                });

                // Fetch symbols
                mapViewModel.fetchPosts(getApplicationContext(), getAuthToken(), POST_RADIUS);
                mapViewModel.fetchFollowings(getApplicationContext(), getAuthToken());

                enableLocationComponent(style);
            }
        });
    }

    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(this)
                    .elevation(5)
                    .accuracyAlpha(.25f)
                    .accuracyColor(Color.RED)
                    .build();

            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build();

            // Set up location component
            locationComponent = mapboxMap.getLocationComponent();
            locationComponent.activateLocationComponent(locationComponentActivationOptions);
            locationComponent.setLocationComponentEnabled(true);
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.setRenderMode(RenderMode.COMPASS);
            locationComponent.addOnLocationClickListener(new OnLocationClickListener() {
                @Override
                public void onLocationComponentClick() {
                    goToProfile(getAuthUserId());
                }
            });
        }
        else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == makePostButton) {
            goToPostCreation();
        }
        else if (v == trackingModeButton) {
            locationComponent.setCameraMode(CameraMode.TRACKING);
            locationComponent.zoomWhileTracking(16f);
        }
    }

    ///////
    // Misc
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();

        // Update symbols on return to activity
        if (postManager != null && followingManager != null) {
            mapViewModel.fetchPosts(getApplicationContext(), getAuthToken(), POST_RADIUS);
            mapViewModel.fetchFollowings(getApplicationContext(), getAuthToken());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}