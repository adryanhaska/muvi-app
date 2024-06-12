package com.example.muvi_app.ui.social

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.muvi_app.databinding.ActivitySocialBinding
import com.example.muvi_app.ui.ViewModelFactory
import com.example.muvi_app.ui.main.MainActivity
import com.example.muvi_app.ui.search.SearchActivity
import com.example.muvi_app.ui.settings.SettingsActivity
import com.example.muvi_app.ui.welcome.WelcomeActivity

class SocialActivity : AppCompatActivity() {

    private val viewModel by viewModels<SocialViewModel> { ViewModelFactory.getInstance(this) }
    private lateinit var binding: ActivitySocialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySocialBinding.inflate(layoutInflater)
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
            //do nothing
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

}