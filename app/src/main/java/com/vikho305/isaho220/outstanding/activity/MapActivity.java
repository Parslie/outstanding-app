package com.vikho305.isaho220.outstanding.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.vikho305.isaho220.outstanding.viewmodel.UserViewModel;

import java.util.List;
import java.util.Objects;

public class MapActivity extends AuthorizedActivity implements OnMapReadyCallback,
        OnLocationClickListener, PermissionsListener, View.OnClickListener {

    private static final int POST_CREATION_REQUEST = 0;
    private static final String TEXT_ICON = "text";
    private static final String PICTURE_ICON = "picture";
    private static final int POST_RADIUS = 1000;

    private MapView mapView;
    private FloatingActionButton makePostButton, trackingModeButton;

    private UserViewModel userViewModel;
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

        // Init view models
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
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
        // TODO: when user is updated add as new symbol to map

        // Init activity
        Intent intent = getIntent();
        User user = intent.getParcelableExtra("user");

        if (user != null) // Prevents unnecessary server calls
            userViewModel.setUser(user);
        else if (userViewModel.getUser().getValue() == null) // Prevents unnecessary server calls
            userViewModel.fetchUser(getApplicationContext(), getAuthUserId(), getAuthToken());

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

        for (Post post : posts) {
            if(post.getMediaType().equals(Post.TEXT_TYPE)){
                postManager.create(new SymbolOptions()
                        .withLatLng(new LatLng(post.getLatitude(), post.getLongitude()))
                        .withIconImage(TEXT_ICON)
                        .withIconSize(1.0f));
            }
            else if(post.getMediaType().equals(Post.IMAGE_TYPE)){
                postManager.create(new SymbolOptions()
                        .withLatLng(new LatLng(post.getLatitude(), post.getLongitude()))
                        .withIconImage(PICTURE_ICON)
                        .withIconSize(1.0f));
            }
        }
    }

    private void updateFollowingSymbols(List<User> users) {
        followingManager.deleteAll();

        for (User user : users) {
            followingManager.create(new SymbolOptions()
                    .withLatLng(new LatLng(user.getLatitude(), user.getLongitude()))
                    .withIconImage(TEXT_ICON)
                    .withIconSize(1.0f));
        }
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.LIGHT, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                style.addImage(TEXT_ICON,
                        Objects.requireNonNull(BitmapUtils.getBitmapFromDrawable(ContextCompat.getDrawable(MapActivity.this, R.drawable.ic_text_24dp))),
                        true);

                style.addImage(PICTURE_ICON,
                        Objects.requireNonNull(BitmapUtils.getBitmapFromDrawable(ContextCompat.getDrawable(MapActivity.this, R.drawable.ic_image_24dp))),
                        true);

                postManager = new SymbolManager(mapView, mapboxMap, style);
                postManager.setIconAllowOverlap(true);
                postManager.setTextAllowOverlap(true);
                followingManager = new SymbolManager(mapView, mapboxMap, style);
                followingManager.setIconAllowOverlap(true);
                followingManager.setTextAllowOverlap(true);

                mapViewModel.fetchPosts(getApplicationContext(), getAuthToken(), POST_RADIUS);
                mapViewModel.fetchFollowings(getApplicationContext(), getAuthToken());

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

                enableLocationComponent(style);
            }
        });
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            // Create and customize the LocationComponent's options
            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(this)
                    .elevation(5)
                    .accuracyAlpha(.6f)
                    .accuracyColor(Color.RED)
                    .foregroundDrawable(R.drawable.icon)
                    .build();

            // Get an instance of the component
            locationComponent = mapboxMap.getLocationComponent();

            LocationComponentActivationOptions locationComponentActivationOptions =
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build();

            // Activate with options
            locationComponent.activateLocationComponent(locationComponentActivationOptions);

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);

            // Add the location icon click listener
            locationComponent.addOnLocationClickListener(this);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressLint("StringFormatInvalid")
    @SuppressWarnings( {"MissingPermission"})
    @Override
    public void onLocationComponentClick() {
        if (locationComponent.getLastKnownLocation() != null) {

            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("userId", getAuthUserId());
            goToActivity(intent);

            Toast.makeText(this, String.format(getString(R.string.current_location),
                    locationComponent.getLastKnownLocation().getLatitude(),
                    locationComponent.getLastKnownLocation().getLongitude()), Toast.LENGTH_LONG).show();
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
    // Misc // TODO: check then categorize and organize methods
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

    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
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
    protected void onSaveInstanceState(Bundle outState) {
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