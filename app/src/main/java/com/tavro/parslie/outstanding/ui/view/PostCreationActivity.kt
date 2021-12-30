package com.tavro.parslie.outstanding.ui.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.google.gson.GsonBuilder
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.tavro.parslie.outstanding.R
import com.tavro.parslie.outstanding.data.model.Post
import com.tavro.parslie.outstanding.databinding.ActivityPostCreationBinding
import com.tavro.parslie.outstanding.ui.viewmodel.ContextualViewModelFactory
import com.tavro.parslie.outstanding.ui.viewmodel.PostViewModel
import com.tavro.parslie.outstanding.util.Status

class PostCreationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostCreationBinding
    private lateinit var viewModel: PostViewModel
    private lateinit var locationProvider: FusedLocationProviderClient

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_creation)

        locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        initViewModel()
        initListeners()
    }

    private fun initViewModel() {
        val viewModelFactory = ContextualViewModelFactory(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[PostViewModel::class.java]

        viewModel.getCreationData().observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    val intent = Intent()
                    intent.putExtra("post", it.data)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                Status.ERROR -> Snackbar.make(binding.root, R.string.post_creation_error, Snackbar.LENGTH_LONG).show()
                Status.LOADING -> {}
            }
        }
    }

    private fun initListeners() {
        binding.postCreationPostBtn.setOnClickListener {
            val title: String = binding.postCreationTitle.text.toString()
            val content: String = binding.postCreationContent.text.toString()

            if (title.isEmpty()) {
                binding.postCreationTitle.error = resources.getString(R.string.no_title_input)
            } else {
                viewModel.createPost(title, content, latitude, longitude)
            }
        }

        binding.postCreationToolbar.setNavigationOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }

    @SuppressLint("MissingPermission")
    private val locationPermissionRequest = registerForActivityResult(RequestPermission()) { data ->
        if (data) {
            locationProvider = LocationServices.getFusedLocationProviderClient(this)
            locationProvider.lastLocation.addOnSuccessListener { location ->
                latitude = location.latitude
                longitude = location.longitude
            }
        } else {
            finish() // TODO: handle denied permissions better
        }
    }
}