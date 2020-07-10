package com.vikho305.isaho220.outstanding;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
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
import com.mapbox.mapboxsdk.utils.BitmapUtils;
import com.vikho305.isaho220.outstanding.database.Post;

import java.util.List;
import java.util.Objects;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TEXT_ICON = "text";
    private static final String PICTURE_ICON = "picture";
    private static final int POST_RADIUS = 1000;

    private MapView mapView;

    private MapViewModel mapViewModel;

    private MapboxMap mapboxMap;
    private SymbolManager postManager;
    private LocationComponent locationComponent;

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.map_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Get layout views
        mapView = view.findViewById(R.id.map_mapView);

        // Init view model
        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        mapViewModel.getPosts().observe(getViewLifecycleOwner(), new Observer<List<Post>>() {
            @Override
            public void onChanged(List<Post> posts) {
                // TODO: add posts to symbol manager
            }
        });

        // Init fragment
        mapView.getMapAsync(this);
    }

    //////////////
    // Map methods

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.LIGHT, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                // Init symbol icons
                /*
                style.addImage(TEXT_ICON,
                        BitmapFactory.decodeResource(getResources(), R.drawable.ic_text_24dp), true);
                style.addImage(PICTURE_ICON,
                        BitmapFactory.decodeResource(getResources(), R.drawable.ic_image_24dp), true);
                 */

                // Init symbol managers
                postManager = new SymbolManager(mapView, mapboxMap, style);
                postManager.setIconAllowOverlap(true);
                postManager.setTextAllowOverlap(true);

                // Init symbol click listeners
                postManager.addClickListener(new OnSymbolClickListener() {
                    @Override
                    public void onAnnotationClick(Symbol symbol) {
                        LatLng latLng = symbol.getLatLng();
                        double lat = latLng.getLatitude();
                        double lng = latLng.getLongitude();

                        Post post = mapViewModel.getPostAt(lat, lng);
                        // TODO: if post isn't null, go to post fragment
                    }
                });

                // Fetch symbols
                mapViewModel.fetchPosts(requireContext(), POST_RADIUS);

                enableLocationComponent(style);
            }
        });
    }

    private void enableLocationComponent(@NonNull Style style) {
        LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(requireContext())
                .elevation(5)
                .accuracyAlpha(.25f)
                .accuracyColor(Color.RED)
                .build();

        LocationComponentActivationOptions locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(requireContext(), style)
                        .locationComponentOptions(customLocationComponentOptions)
                        .build();

        // Permission check
        if ((ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) | ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            return;
        }

        // Set up location component
        locationComponent = mapboxMap.getLocationComponent();
        locationComponent.activateLocationComponent(locationComponentActivationOptions);
        locationComponent.setLocationComponentEnabled(true);
        locationComponent.setCameraMode(CameraMode.TRACKING);
        locationComponent.setRenderMode(RenderMode.COMPASS);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            int result = grantResults[i];

            if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION) && result == PackageManager.PERMISSION_GRANTED ||
                    permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION) && result == PackageManager.PERMISSION_GRANTED) {
                mapboxMap.getStyle(new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        enableLocationComponent(style);
                    }
                });
            }
            else {
                return; // TODO: add more extensive handling
            }
        }
    }

    //////////////////////////////
    // Essential lifecycle methods

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }
}