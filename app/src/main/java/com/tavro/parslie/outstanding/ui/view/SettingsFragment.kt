package com.tavro.parslie.outstanding.ui.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tavro.parslie.outstanding.R
import com.tavro.parslie.outstanding.databinding.FragmentSettingsBinding
import com.tavro.parslie.outstanding.ui.viewmodel.AuthViewModel
import com.tavro.parslie.outstanding.ui.viewmodel.ContextualViewModelFactory
import com.tavro.parslie.outstanding.ui.viewmodel.UserViewModel
import com.tavro.parslie.outstanding.util.Status

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var authViewModel: AuthViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)
        initViewModels()
        initListeners()

        return binding.root
    }

    private fun initViewModels() {
        val viewModelFactory = ContextualViewModelFactory(requireContext())
        userViewModel = ViewModelProvider(this, viewModelFactory)[UserViewModel::class.java]
        authViewModel = ViewModelProvider(this, viewModelFactory)[AuthViewModel::class.java]

        authViewModel.getLogoutData().observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
                Status.ERROR -> {
                    Snackbar.make(binding.root, resources.getString(R.string.logout_error), Snackbar.LENGTH_LONG).show()
                }
                else -> {

                }
            }
        }
    }

    private fun initListeners() {
        binding.settingsLogoutBtn.setOnClickListener {
            authViewModel.logout()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}