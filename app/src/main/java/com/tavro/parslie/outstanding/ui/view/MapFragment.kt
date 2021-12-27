package com.tavro.parslie.outstanding.ui.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Bundle
import android.os.Looper
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.android.gestures.RotateGestureDetector
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.CameraState
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.OnRotateListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.LocationProvider
import com.mapbox.maps.plugin.locationcomponent.generated.LocationComponentSettings
import com.mapbox.maps.plugin.locationcomponent.location
import com.tavro.parslie.outstanding.R
import com.tavro.parslie.outstanding.databinding.FragmentMapBinding

class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding
    private lateinit var mapboxMap: MapboxMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        mapboxMap = binding.mapMap.getMapboxMap()

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            locationResults.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        else
            showLocation()

        return binding.root
    }

    @SuppressLint("MissingPermission")
    private fun showLocation() {
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

    private val locationResults = registerForActivityResult(RequestPermission()) {
        if (it)
            showLocation()
        else
            requireActivity().finish() // TODO: handle denied permissions better
    }

    companion object {
        @JvmStatic
        fun newInstance() = MapFragment()
    }
}