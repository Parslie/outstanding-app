package com.tavro.parslie.outstanding.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.tavro.parslie.outstanding.R
import com.tavro.parslie.outstanding.data.repository.PreferenceRepository
import com.tavro.parslie.outstanding.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var prefs: PreferenceRepository
    private lateinit var mapFragment: MapFragment
    private lateinit var profileFragment: ProfileFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        prefs = PreferenceRepository(this)

        mapFragment = MapFragment.newInstance()
        profileFragment = ProfileFragment.newInstance(prefs.authID)

        supportFragmentManager.beginTransaction()
            .add(R.id.main_container, mapFragment)
            .add(R.id.main_container, profileFragment)
            .hide(profileFragment)
            .commit()

        binding.mainToolbar.setNavigationOnClickListener {
            binding.mainDrawerLayout.openDrawer(binding.mainDrawer)
        }

        binding.mainDrawer.setNavigationItemSelectedListener {
            var success = false

            when (it.itemId) {
                R.id.mainMenu_map -> {
                    showMap()
                    success = true
                } R.id.mainMenu_profile -> {
                    showProfile()
                    success = true
                } R.id.mainMenu_settings -> {
                    // TODO: implement settings fragment
                } R.id.mainMenu_logout -> {
                    // TODO: implement logging out
                }
            }

            if (success)
                binding.mainDrawerLayout.closeDrawer(binding.mainDrawer)
            success
        }
    }

    private fun showMap() {
        supportFragmentManager.beginTransaction()
            .hide(profileFragment)
            .show(mapFragment)
            .commit()
    }

    private fun showProfile() {
        supportFragmentManager.beginTransaction()
            .hide(mapFragment)
            .show(profileFragment)
            .commit()
    }
}