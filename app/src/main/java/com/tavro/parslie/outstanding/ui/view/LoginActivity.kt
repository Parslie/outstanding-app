package com.tavro.parslie.outstanding.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tavro.parslie.outstanding.R
import com.tavro.parslie.outstanding.databinding.ActivityLoginBinding
import com.tavro.parslie.outstanding.ui.viewmodel.ContextualViewModelFactory
import com.tavro.parslie.outstanding.ui.viewmodel.LoginViewModel
import com.tavro.parslie.outstanding.util.Status

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        initViewModel()
        initListeners()
    }

    private fun initViewModel() {
        val viewModelFactory = ContextualViewModelFactory(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        viewModel.getLoginResponse().observe(this, {
            binding.mainLoginStatus.text = "Status: ${it.status}"
            binding.mainLoginData.text = "Data: ${it.data}"
        })

        viewModel.getRegisterResponse().observe(this, {
            binding.mainRegisterStatus.text = "Status: ${it.status}"
            binding.mainRegisterData.text = "Data: ${it.data}"
        })
    }

    private fun initListeners() {
        binding.mainRegisterBtn.setOnClickListener {
            val email = binding.mainRegisterEmailField.text.toString()
            val username = binding.mainRegisterUsernameField.text.toString()
            val password = binding.mainRegisterPassField.text.toString()

            viewModel.register(email, username, password)
        }
        binding.mainLoginBtn.setOnClickListener {
            val email = binding.mainLoginEmailField.text.toString()
            val password = binding.mainLoginPassField.text.toString()

            viewModel.login(email, password)
        }
    }
}