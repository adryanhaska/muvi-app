package com.example.muvi_app.ui.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muvi_app.data.adapter.FavMoviesAdapter
import com.example.muvi_app.data.pref.UserPreference
import com.example.muvi_app.data.pref.dataStore
import com.example.muvi_app.databinding.ActivityProfileBinding
import com.example.muvi_app.repository.UserRepository
import com.example.muvi_app.ui.ViewModelFactory
import com.example.muvi_app.ui.detail.MovieDetailActivity
import com.example.muvi_app.ui.main.MainActivity
import com.example.muvi_app.ui.search.SearchActivity
import com.example.muvi_app.ui.settings.SettingsActivity
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private val viewModel by viewModels<ProfileViewModel> { ViewModelFactory.getInstance(this) }
    private lateinit var binding: ActivityProfileBinding
    private lateinit var userRepository: UserRepository
    private var loggedInUserId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        setContentView(binding.root)

        val userPreference = UserPreference.getInstance(this.dataStore)
        userRepository = UserRepository(userPreference)

        setupView()
        fetchLoggedInUserDetails()
        binding.rvFavorites.layoutManager = LinearLayoutManager(this)

        viewModel.favoriteMovies.observe(this) { movies ->
            val adapter = movies?.let {
                FavMoviesAdapter(it) { movie ->
                    val intent = Intent(this, MovieDetailActivity::class.java)
                    intent.putExtra("movie_id", movie.id)
                    startActivity(intent)
                }
            }
            binding.rvFavorites.adapter = adapter
        }

        viewModel.error.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }

        viewModel.loggedInUserDetail.observe(this) { userDetail ->
            userDetail?.let {
                binding.textName.text = userDetail.name
                binding.username.text = "@${userDetail.username}"
                binding.followingCount.text = userDetail.following?.size.toString()
            }
        }
    }

    private fun fetchLoggedInUserDetails() {
        lifecycleScope.launch {
            loggedInUserId = userRepository.getUserId()
            loggedInUserId?.let {
                setupUserProfile(it)
            }
        }
    }

    private fun setupUserProfile(userId: String) {
        viewModel.getLoggedInUserDetail(userId)
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

        }

        binding.settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
        }
    }

    private fun setupView() {
        setupFullscreenMode()
        setupBottomAppBar()

        binding.backButton.setOnClickListener {
            onBackPressed()
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
}