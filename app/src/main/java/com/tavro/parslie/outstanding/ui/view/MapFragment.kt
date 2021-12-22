package com.tavro.parslie.outstanding.ui.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.gestures.gestures
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

    private fun showLocation() {
        mapboxMap.loadStyleUri(Style.MAPBOX_STREETS) {
            binding.mapMap.location.updateSettings {
                enabled = true
                pulsingEnabled = true
            }
        }

        binding.mapMap.location.addOnIndicatorPositionChangedListener {
            binding.mapMap.getMapboxMap().setCamera(CameraOptions.Builder().center(it).build())
            binding.mapMap.gestures.focalPoint = mapboxMap.pixelForCoordinate(it)
        }
        binding.mapMap.gestures.scrollEnabled = false
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