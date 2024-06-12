package com.example.muvi_app.ui.signup

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.muvi_app.databinding.ActivitySignupBinding
import com.example.muvi_app.ui.ViewModelFactory
import com.example.muvi_app.ui.login.LoginActivity
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var signupViewModel: SignUpViewModel

    private var selectedDate = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        signupViewModel = obtainViewModel()

        setupView()
        setupAction()
    }

    private fun obtainViewModel(): SignUpViewModel {
        val factory = ViewModelFactory.getInstance(application)
        return ViewModelProvider(this, factory).get(SignUpViewModel::class.java)
    }

    private fun setupView() {
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

        binding.dobPickerButton.setOnClickListener {
            showDatePickerDialog()
        }
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val username = binding.username.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val fullName = binding.fullName.text.toString()
            val gender = if (binding.male.isChecked) 1 else 0

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dobFormatted = sdf.format(selectedDate.time)

            showLoading(true)
            signupViewModel.register(username, email, password, dobFormatted, gender, fullName)
        }

        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        signupViewModel.registerResponse.observe(this) { registerResponse ->
            showLoading(false)
            if (registerResponse.status == 201) {
                AlertDialog.Builder(this).apply {
                    setTitle("Success!")
                    setMessage("New account has been successfully created. Let's proceed to login.")
                    setPositiveButton("Continue") { _, _ ->
                        finish()
                    }
                    create()
                    show()
                }
            } else {
                val registerRes = registerResponse.message
                AlertDialog.Builder(this).apply {
                    setTitle("Registration Failed")
                    setMessage(registerRes)
                    setPositiveButton("Retry") { _, _ ->
                        // Optional: You can handle retry logic here if needed
                    }
                    create()
                    show()
                }
            }
        }
    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedDate.set(year, month, dayOfMonth)
                updateDateButton()
            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun updateDateButton() {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dobFormatted = sdf.format(selectedDate.time)
        binding.dobPickerButton.text = dobFormatted
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}
