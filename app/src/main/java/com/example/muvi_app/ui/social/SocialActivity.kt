package com.example.muvi_app.ui.social

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
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
import com.example.muvi_app.data.response.Movie
import com.example.muvi_app.databinding.ActivitySocialBinding
import com.example.muvi_app.repository.UserRepository
import com.example.muvi_app.ui.ViewModelFactory
import com.example.muvi_app.ui.detail.MovieDetailActivity
import com.example.muvi_app.ui.main.MainActivity
import com.example.muvi_app.ui.search.SearchActivity
import com.example.muvi_app.ui.settings.SettingsActivity
import kotlinx.coroutines.launch

class SocialActivity : AppCompatActivity() {

    private val viewModel by viewModels<SocialViewModel> { ViewModelFactory.getInstance(this) }
    private lateinit var binding: ActivitySocialBinding
    private lateinit var userRepository: UserRepository
    private var loggedInUserId: String? = null
    private var targetUserId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySocialBinding.inflate(layoutInflater)
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

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun fetchLoggedInUserDetails() {
        lifecycleScope.launch {
            loggedInUserId = userRepository.getUserId()
            loggedInUserId?.let {
                setupUserProfile()
            }
        }
    }

    private fun setupUserProfile() {
        targetUserId = intent.getStringExtra("USER_ID")
        targetUserId?.let { userId ->
            viewModel.getUserDetail(userId)
            viewModel.getSession().observe(this) { loggedInUser ->
                loggedInUser?.let {
                    viewModel.getLoggedInUserDetail(it.id)
                }
            }
        }

        viewModel.userDetail.observe(this) { userDetail ->
            userDetail?.let {
                binding.textName.text = userDetail.name
                binding.username.text = "@${userDetail.username}"
                binding.followingCount.text = userDetail.following?.size.toString()
            }
        }

        viewModel.favoriteMovies.observe(this) { favoriteMovies ->
            favoriteMovies?.let {
                displayFavoriteMovies(it)
            }
        }

        viewModel.loggedInUserDetail.observe(this) { loggedInUserDetail ->
            loggedInUserDetail?.let {
                val isFollowing = it.following?.contains(targetUserId) ?: false
                if (isFollowing) {
                    showUnfollowButton()
                } else {
                    showFollowButton()
                }
            }
        }

        binding.followButton.setOnClickListener {
            targetUserId?.let { userId ->
                viewModel.followUser(userId) { result ->
                    result.onSuccess {
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                        showUnfollowButton()
                    }.onFailure {
                        Toast.makeText(this, "Follow failed: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.unfollowButton.setOnClickListener {
            targetUserId?.let { userId ->
                viewModel.unfollowUser(userId) { result ->
                    result.onSuccess {
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                        showFollowButton()
                    }.onFailure {
                        Toast.makeText(this, "Unfollow failed: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }
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
            // do nothing
        }

        binding.settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            finish()
        }
    }

    private fun showFollowButton() {
        binding.followButton.visibility = View.VISIBLE
        binding.unfollowButton.visibility = View.GONE
    }

    private fun showUnfollowButton() {
        binding.followButton.visibility = View.GONE
        binding.unfollowButton.visibility = View.VISIBLE
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

    private fun displayFavoriteMovies(movies: List<Movie>) {
        val adapter = FavMoviesAdapter(movies) { movie ->
            val intent = Intent(this, MovieDetailActivity::class.java)
            intent.putExtra("movie_id", movie.id)
            startActivity(intent)
        }
        binding.rvFavorites.adapter = adapter
    }
}