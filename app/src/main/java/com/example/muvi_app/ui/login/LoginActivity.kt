package com.example.muvi_app.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.muvi_app.databinding.ActivityLoginBinding
import com.example.muvi_app.ui.ViewModelFactory
import com.example.muvi_app.ui.main.MainActivity

class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showLoading(false)

        setupViewAndAction()
    }

    private fun setupViewAndAction() {
        setupFullscreenMode()
        setupLoginButtonClick()
        observeLoginResponse()
        observeLoginErrorResponse()
    }

    private fun setupFullscreenMode() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupLoginButtonClick() {
        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            showLoading(true)
            viewModel.login(email, password)
        }
    }

    private fun observeLoginResponse() {
        viewModel.loginResponse.observe(this) { loginResponse ->
            showLoading(false)
            navigateToMainActivity()
        }
    }

    private fun observeLoginErrorResponse() {
        viewModel.loginErrorResponse.observe(this) { loginErrorResponse ->
            showLoading(false)
            showLoginFailedDialog(loginErrorResponse.message)
        }
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showLoginFailedDialog(message: String) {
        AlertDialog.Builder(this).apply {
            setTitle("Login Gagal")
            setMessage(message)
            setPositiveButton("Ulangi") { _, _ ->
                // Do nothing (stay on the login screen)
            }
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
