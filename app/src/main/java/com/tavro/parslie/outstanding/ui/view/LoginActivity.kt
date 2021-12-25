package com.tavro.parslie.outstanding.ui.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tavro.parslie.outstanding.BuildConfig
import com.tavro.parslie.outstanding.R
import com.tavro.parslie.outstanding.data.repository.PreferenceRepository
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
        binding.loginVersion.text = getString(R.string.app_version, BuildConfig.VERSION_NAME)

        initViewModel()
        initListeners()
    }

    private fun initViewModel() {
        val viewModelFactory = ContextualViewModelFactory(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]

        viewModel.getLoginResponse().observe(this, {
            when(it.status) {
                Status.LOADING -> {
                    binding.loginProgressBar.visibility = View.VISIBLE
                    binding.loginLoginBtn.isEnabled = false
                }
                Status.SUCCESS -> {
                    val prefs = PreferenceRepository(applicationContext)
                    prefs.authToken = it.data!!.token
                    prefs.authID = it.data.user.id

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                Status.ERROR -> {
                    binding.loginProgressBar.visibility = View.GONE
                    binding.loginLoginBtn.isEnabled = true

                    // TODO: implement error model client-side and server-side (display like "404 not found")
                    Snackbar.make(this, binding.root, "There was an error...", Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun validateEmail(email: String): Boolean {
        return when (email) {
            "" -> {
                binding.loginEmail.error = resources.getString(R.string.no_email_input)
                false
            }
            else -> true
        }
    }

    private fun validatePassword(password: String): Boolean {
        return when (password) {
            "" -> {
                binding.loginPassword.error = resources.getString(R.string.no_password_input)
                false
            }
            else -> true
        }
    }

    private fun initListeners() {
        binding.loginLoginBtn.setOnClickListener {
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()

            val validEmail = validateEmail(email)
            val validPassword = validatePassword(password)

            if (validEmail && validPassword)
                viewModel.login(email, password)
        }

        binding.loginRegisterBtn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            registerResults.launch(intent)
        }
    }

    private val registerResults = registerForActivityResult(StartActivityForResult()) {
        when(it.resultCode) {
            Activity.RESULT_OK -> {
                val email = it.data!!.getStringExtra("email")!!
                val password = it.data!!.getStringExtra("password")!!
                viewModel.login(email, password)
            }
        }
    }
}