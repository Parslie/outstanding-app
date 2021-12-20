package com.tavro.parslie.outstanding.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tavro.parslie.outstanding.R
import com.tavro.parslie.outstanding.databinding.ActivityLoginBinding
import com.tavro.parslie.outstanding.ui.viewmodel.ContextualViewModelFactory
import com.tavro.parslie.outstanding.ui.viewmodel.LoginViewModel

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
    }

    private fun initListeners() {
        binding.mainRegisterBtn.setOnClickListener {
            val email = binding.mainRegisterEmailField.text
            val username = binding.mainRegisterUsernameField.text
            val password = binding.mainRegisterPassField.text
        }
        binding.mainLoginBtn.setOnClickListener {
            val email = binding.mainLoginEmailField.text
            val password = binding.mainLoginPassField.text
        }
    }
}