package com.tavro.parslie.outstanding.ui.view

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tavro.parslie.outstanding.R
import com.tavro.parslie.outstanding.databinding.ActivityRegisterBinding
import com.tavro.parslie.outstanding.ui.viewmodel.ContextualViewModelFactory
import com.tavro.parslie.outstanding.ui.viewmodel.RegisterViewModel
import com.tavro.parslie.outstanding.util.Status

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)

        initViewModel()
        initListeners()
    }

    private fun initViewModel() {
        val viewModelFactory = ContextualViewModelFactory(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[RegisterViewModel::class.java]

        viewModel.getRegisterResponse().observe(this, {
            when(it.status) {
                Status.LOADING -> {
                    binding.registerProgressBar.visibility = View.VISIBLE
                    binding.registerLoginBtn.isEnabled = false
                }
                Status.SUCCESS -> {
                    val data = Intent()
                    data.putExtra("email", binding.registerEmail.text.toString())
                    data.putExtra("password", binding.registerPassword.text.toString())
                    setResult(Activity.RESULT_OK, data)
                    finish()
                }
                Status.ERROR -> {
                    binding.registerProgressBar.visibility = View.INVISIBLE
                    binding.registerLoginBtn.isEnabled = true
                }
            }
        })
    }

    private fun initListeners() {
        binding.registerBackBtn.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        binding.registerLoginBtn.setOnClickListener {
            val email = binding.registerEmail.text.toString()
            val username = binding.registerUsername.text.toString()
            val password = binding.registerPassword.text.toString()
            val confirmPassword = binding.registerConfirmPassword.text.toString()

            if (password == confirmPassword)
                viewModel.register(email, username, password)
            else
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show()
        }
    }
}