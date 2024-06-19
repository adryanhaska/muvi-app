package com.example.muvi_app.ui.settings

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.muvi_app.databinding.ActivitySettingsBinding
import com.example.muvi_app.ui.ViewModelFactory
import com.example.muvi_app.ui.main.MainActivity
import com.example.muvi_app.ui.profile.ProfileActivity
import com.example.muvi_app.ui.search.SearchActivity
import com.example.muvi_app.ui.social.SocialActivity
import com.example.muvi_app.ui.welcome.WelcomeActivity

class SettingsActivity : AppCompatActivity() {

    private val viewModel by viewModels<SettingsViewModel> { ViewModelFactory.getInstance(this) }
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
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
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.searchButton.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
            finish()
        }

        binding.socialButton.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }

        binding.settingsButton.setOnClickListener {
            //do nothing
        }
    }

    private fun setupView() {
        setupFullscreenMode()
        setupBottomAppBar()
        binding.logoutButton.setOnClickListener {
            viewModel.logout()
            navigateToWelcomeActivity()
        }
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
}