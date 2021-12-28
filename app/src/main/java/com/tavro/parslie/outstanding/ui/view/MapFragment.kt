package com.tavro.parslie.outstanding.ui.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.location
import com.tavro.parslie.outstanding.R
import com.tavro.parslie.outstanding.data.model.Post
import com.tavro.parslie.outstanding.databinding.FragmentMapBinding
import com.tavro.parslie.outstanding.ui.viewmodel.ContextualViewModelFactory
import com.tavro.parslie.outstanding.ui.viewmodel.PostViewModel
import com.tavro.parslie.outstanding.util.Status

class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding
    private lateinit var viewModel: PostViewModel
    private lateinit var locationProvider: FusedLocationProviderClient
    private lateinit var mapboxMap: MapboxMap
    private lateinit var postManager: PointAnnotationManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        mapboxMap = binding.mapMap.getMapboxMap()
        initViewModel()

        locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        return binding.root
    }

    private fun initViewModel() {
        val viewModelFactory = ContextualViewModelFactory(requireContext())
        viewModel = ViewModelProvider(this, viewModelFactory)[PostViewModel::class.java]

        viewModel.getVicinityData().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    val gson = GsonBuilder().create()
                    val icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_post_24, requireActivity().theme)!!
                        .toBitmap(80, 80)

                    it.data!!.forEach { post ->
                        postManager.create(
                            PointAnnotationOptions()
                                .withPoint(Point.fromLngLat(post.longitude, post.latitude))
                                .withIconImage(icon)
                                .withIconAnchor(IconAnchor.BOTTOM)
                                .withData(gson.toJsonTree(post))
                        )
                    }
                }
                Status.ERROR -> Snackbar.make(binding.root, R.string.nearby_posts_error, Snackbar.LENGTH_LONG).show()
                Status.LOADING -> {}
            }
        }
    }

    private fun onLocationPermissionGranted() {
        val colorSecondary = TypedValue()
        requireContext().theme.resolveAttribute(R.attr.colorSecondary, colorSecondary, true)
        val puckDrawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_navigation_24, requireActivity().theme)

        mapboxMap.loadStyleUri(Style.MAPBOX_STREETS) {
            binding.mapMap.location.updateSettings {
                enabled = true
                pulsingEnabled = true
                pulsingColor = colorSecondary.data
                locationPuck = LocationPuck2D(puckDrawable)
            }

            binding.mapMap.location.addOnIndicatorPositionChangedListener {
                mapboxMap.setCamera(CameraOptions.Builder().center(it).build())
                binding.mapMap.gestures.focalPoint = mapboxMap.pixelForCoordinate(it)
            }

            binding.mapMap.gestures.pitchEnabled = false
            binding.mapMap.gestures.scrollEnabled = false
        }
    }

    @SuppressLint("MissingPermission")
    private val locationPermissionRequest = registerForActivityResult(RequestPermission()) { data ->
        if (data) {
            postManager = binding.mapMap.annotations.createPointAnnotationManager()
            postManager.addClickListener {
                val gson = GsonBuilder().create()
                val post = gson.fromJson(it.getData(), Post::class.java)

                val intent = Intent(requireContext(), PostActivity::class.java)
                // intent.putExtra("post", post) // TODO: make Post parcelable
                startActivity(intent)
                true
            }

            onLocationPermissionGranted()

            locationProvider = LocationServices.getFusedLocationProviderClient(requireActivity())
            locationProvider.lastLocation.addOnSuccessListener { location ->
                println("Position: (${location.latitude}, ${location.longitude})")
                viewModel.fetchNearbyPosts(location.latitude, location.longitude)
            }
        } else {
            requireActivity().finish() // TODO: handle denied permissions better
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = MapFragment()
    }
}