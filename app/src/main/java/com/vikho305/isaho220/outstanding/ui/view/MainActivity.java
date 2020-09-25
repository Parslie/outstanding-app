package com.vikho305.isaho220.outstanding.ui.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mapbox.mapboxsdk.Mapbox;
import com.vikho305.isaho220.outstanding.R;
import com.vikho305.isaho220.outstanding.data.repositories.PreferenceRepository;
import com.vikho305.isaho220.outstanding.ui.viewmodel.ContextualViewModelFactory;
import com.vikho305.isaho220.outstanding.ui.viewmodel.MainViewModel;
import com.vikho305.isaho220.outstanding.util.Resource;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, LocationListener {

    private static final long MIN_LOCATION_TIME = 1000;
    private static final float MIN_LOCATION_METER = 7;

    private FrameLayout fragmentContainer;
    private BottomNavigationView navigationView;

    private ProfileFragment profileFragment;
    private MapFragment mapFragment;

    private LocationManager locationManager;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoiaXNha2hvcnZhdGgiLCJhIjoiY2s4dnZ3NWoxMDN1azNncXBuNWl0YWttdSJ9.ksNJFJjaQsqnIFzKy3ZCkg");
        setContentView(R.layout.activity_main);

        PreferenceRepository preferences = new PreferenceRepository(this);
        profileFragment = ProfileFragment.newInstance(preferences.getAuthUserId());
        mapFragment = MapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainContainer, profileFragment)
                .add(R.id.mainContainer, mapFragment)
                .commit();

        fragmentContainer = findViewById(R.id.mainContainer);
        navigationView = findViewById(R.id.mainNavBar);

        navigationView.setOnNavigationItemSelectedListener(this);
        navigationView.setSelectedItemId(R.id.menuMap);

        initViewModel();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        requestLocationUpdates();
    }

    private void initViewModel() {
        ContextualViewModelFactory viewModelFactory = new ContextualViewModelFactory(this);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(MainViewModel.class);

        viewModel.getHasUpdatedLocation().observe(this, new Observer<Resource<Boolean>>() {
            @Override
            public void onChanged(Resource<Boolean> booleanResource) {
                // TODO: add error-handling
            }
        });
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_LOCATION_TIME, MIN_LOCATION_METER, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];
            int result = grantResults[i];

            if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION) && result == PackageManager.PERMISSION_GRANTED ||
                    permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION) && result == PackageManager.PERMISSION_GRANTED) {
                requestLocationUpdates();
            }
            else {
                setResult(RESULT_CANCELED);
                finish();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuMap:
                showMap();
                return true;
            case R.id.menuProfile:
                showProfileSelf();
                return true;
        }
        return false;
    }

    public void showProfileSelf() {
        getSupportFragmentManager().beginTransaction()
                .hide(mapFragment)
                .show(profileFragment)
                .commit();
    }

    public void showMap() {
        getSupportFragmentManager().beginTransaction()
                .show(mapFragment)
                .hide(profileFragment)
                .commit();
    }

    @Override
    public void onLocationChanged(Location location) {
        viewModel.updateLocation(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}