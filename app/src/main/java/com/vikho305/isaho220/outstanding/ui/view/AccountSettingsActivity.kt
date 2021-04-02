package com.vikho305.isaho220.outstanding.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.vikho305.isaho220.outstanding.R
import com.vikho305.isaho220.outstanding.databinding.ActivityAccountSettingsBinding
import com.vikho305.isaho220.outstanding.ui.viewmodel.AccountSettingsViewModel
import com.vikho305.isaho220.outstanding.ui.viewmodel.ContextualViewModelFactory

class AccountSettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountSettingsBinding
    private lateinit var viewModel: AccountSettingsViewModel

    var hasSetAccount = true;
    var hasSetProfile = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account_settings)

        initViewModel()
        setOnClickListeners()
    }

    private fun initViewModel() {
        val viewModelFactory = ContextualViewModelFactory(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AccountSettingsViewModel::class.java)

        viewModel.hasSetAccount().observe(this, {
            hasSetAccount = it.data
            if (hasSetAccount && hasSetProfile)
                finish()
        })
        viewModel.hasSetProfile().observe(this, {
            hasSetProfile = it.data
            if (hasSetAccount && hasSetProfile)
                finish()
        })
        viewModel.getUser().observe(this, {
            binding.accountSettingsName.setText(it.username)
            binding.accountSettingsDescription.setText(it.description)
            binding.accountSettingsEmail.setText(it.email)
            // TODO: set pfp
        })

        viewModel.fetchUser()
    }

    private fun setOnClickListeners() {
        binding.accountSettingsPfpChooseBtn.setOnClickListener {
            println("Clicked Choose Button")
        }
        binding.accountSettingsPfpResetBtn.setOnClickListener {
            println("Clicked Reset Button")
        }
        binding.accountSettingsSaveBtn.setOnClickListener {
            val username = binding.accountSettingsName.text.toString()
            val description = binding.accountSettingsDescription.text.toString()
            val email = binding.accountSettingsEmail.text.toString()
            val newPassword = binding.accountSettingsNewPass.text.toString()
            val newPasswordConfirmation = binding.accountSettingsNewPassConfirmation.text.toString()
            val currentPassword = binding.accountSettingsCurrentPass.text.toString()

            // TODO: validate current password
            
            if (newPassword == newPasswordConfirmation && email.isNotEmpty() && username.isNotEmpty()) {
                viewModel.setAccount(username, email, newPassword)
                hasSetAccount = false
            }
            else {
                // TODO: implement in-app warning
                println("The new password and confirmation do not match!")
            }

            // TODO: implement pfp-changing
            viewModel.setProfile(null, description)
            hasSetProfile = false
        }
    }
}