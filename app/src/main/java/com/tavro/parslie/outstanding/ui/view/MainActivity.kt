package com.tavro.parslie.outstanding.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.google.android.material.navigation.NavigationBarView
import com.tavro.parslie.outstanding.R
import com.tavro.parslie.outstanding.data.repository.PreferenceRepository
import com.tavro.parslie.outstanding.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NavigationBarView.OnItemSelectedListener {
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
            .commit()

        binding.mainNavigation.setOnItemSelectedListener(this)
        binding.mainNavigation.selectedItemId = R.id.mainMenu_map
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

    private fun showSettings() {
        supportFragmentManager.beginTransaction()
            .hide(mapFragment)
            .hide(profileFragment)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.mainMenu_map -> showMap()
            R.id.mainMenu_profile -> showProfile()
            R.id.mainMenu_settings -> showSettings()
            else -> return false
        }
        return true
    }
}