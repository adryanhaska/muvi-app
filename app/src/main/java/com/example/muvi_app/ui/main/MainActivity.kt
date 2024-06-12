package com.example.muvi_app.ui.main

import android.os.Bundle
import com.example.muvi_app.ui.ViewModelFactory
import com.example.muvi_app.ui.welcome.WelcomeActivity
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.muvi_app.ui.search.SearchActivity
import com.example.muvi_app.ui.settings.SettingsActivity
import com.example.muvi_app.ui.social.SocialActivity
import com.example.muvi_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> { ViewModelFactory.getInstance(this) }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        setContentView(binding.root)

        setupObservers()
        setupView()
    }

    private fun setupObservers() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                navigateToWelcomeActivity()
            }
        }
    }

    private fun setupBottomAppBar() {
        binding.homeButton.setOnClickListener {
            // No need to navigate anywhere, as we're already in the home activity
        }

        binding.searchButton.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
            finish()
        }

        binding.socialButton.setOnClickListener {
            startActivity(Intent(this, SocialActivity::class.java))
            finish()
        }

        binding.settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
        }
    }

    private fun setupView() {
        setupFullscreenMode()
        setupBottomAppBar()
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
    }


    private fun navigateToWelcomeActivity() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}

