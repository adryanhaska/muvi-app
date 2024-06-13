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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.muvi_app.R
import com.example.muvi_app.data.response.Movie
import com.example.muvi_app.ui.search.SearchActivity
import com.example.muvi_app.ui.settings.SettingsActivity
import com.example.muvi_app.ui.social.SocialActivity
import com.example.muvi_app.databinding.ActivityMainBinding
import com.example.muvi_app.ui.detail.MovieDetailActivity

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel> { ViewModelFactory.getInstance(this) }
    private lateinit var binding: ActivityMainBinding
    private lateinit var moviesAdapter: MoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        setContentView(binding.root)

        setupObservers()
        setupView()

        setupRecyclerView()

        viewModel.getSession().observe(this, Observer { user ->
            // Get the recommended movies once the session is available
            viewModel.getMovieRecommend(arrayOf("37094","6073","10712","22584","24664","16263","12232","11838","11796","10234"))
            showLoading(false)
        })

        viewModel.movies.observe(this, Observer { response ->
            moviesAdapter.submitList(response.movies as List<Movie>)
        })
    }

    private fun setupRecyclerView() {
        val recyclerViewPopular = findViewById<RecyclerView>(R.id.recyclerViewPopular)
        moviesAdapter = MoviesAdapter()
        moviesAdapter.setOnItemClickCallback(object : MoviesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Movie) {
                val intent = Intent(this@MainActivity, MovieDetailActivity::class.java)
                intent.putExtra("movie_id", data.id)
                startActivity(intent)
            }
        })
        recyclerViewPopular.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = moviesAdapter
        }

        val recyclerViewRecomen = findViewById<RecyclerView>(R.id.recyclerViewRecomen)
        recyclerViewRecomen.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = moviesAdapter
            showLoading2(false)
        }

        val recyclerViewNow = findViewById<RecyclerView>(R.id.recyclerViewNow)
        recyclerViewNow.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = moviesAdapter
            showLoading3(false)
        }

        val recyclerViewUpcoming= findViewById<RecyclerView>(R.id.recyclerViewUpcoming)
        recyclerViewUpcoming.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = moviesAdapter
            showLoading4(false)
        }
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

    private fun showLoading2(isLoading: Boolean) {
        binding.progressBar2.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showLoading3(isLoading: Boolean) {
        binding.progressBar3.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showLoading4(isLoading: Boolean) {
        binding.progressBar4.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}

