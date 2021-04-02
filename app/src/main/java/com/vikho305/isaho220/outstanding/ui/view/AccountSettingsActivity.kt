package com.vikho305.isaho220.outstanding.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vikho305.isaho220.outstanding.R
import com.vikho305.isaho220.outstanding.databinding.ActivityAccountSettingsBinding
import com.vikho305.isaho220.outstanding.ui.viewmodel.AccountSettingsViewModel
import com.vikho305.isaho220.outstanding.ui.viewmodel.ContextualViewModelFactory
import com.vikho305.isaho220.outstanding.ui.viewmodel.MainViewModel
import com.vikho305.isaho220.outstanding.util.Resource

class AccountSettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountSettingsBinding
    private lateinit var viewModel: AccountSettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account_settings)

        initViewModel()
        setOnClickListeners()
    }

    private fun initViewModel() {
        val viewModelFactory = ContextualViewModelFactory(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AccountSettingsViewModel::class.java)
    }

    private fun setOnClickListeners() {
        binding.accountSettingsPfpChooseBtn.setOnClickListener {
            println("Clicked Choose Button")
        }
        binding.accountSettingsPfpResetBtn.setOnClickListener {
            println("Clicked Reset Button")
        }
        binding.accountSettingsSaveBtn.setOnClickListener {
            println("Clicked Save Button")
        }
    }
}