package com.example.muvi_app.ui.search

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.compose.ui.graphics.Color
import androidx.recyclerview.widget.GridLayoutManager
import com.example.muvi_app.R
import com.example.muvi_app.data.adapter.SearchAdapter
import com.example.muvi_app.data.response.Movie
import com.example.muvi_app.databinding.ActivitySearchBinding
import com.example.muvi_app.ui.ViewModelFactory
import com.example.muvi_app.ui.detail.MovieDetailActivity
import com.example.muvi_app.ui.main.MainActivity
import com.example.muvi_app.ui.settings.SettingsActivity
import com.example.muvi_app.ui.social.SocialActivity
import com.example.muvi_app.ui.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private val viewModel by viewModels<SearchViewModel> { ViewModelFactory.getInstance(this) }
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchAdapter : SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
        setContentView(binding.root)

        setupObservers()
        setupView()
        setupRecyclerView()
        setupSearchView()
    }

    private fun setupObservers() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                navigateToWelcomeActivity()
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            // Tambahkan logika untuk menangani loading indicator jika diperlukan
        }

        viewModel.searchList.observe(this) { movies ->
            println("panggil set data $movies")
            searchAdapter.setData(movies)
        }
    }

    private fun setupBottomAppBar() {
        binding.homeButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.searchButton.setOnClickListener {
            //do nothing
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

    private fun setupRecyclerView() {
        searchAdapter = SearchAdapter()
        binding.recyclerView.adapter = searchAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(this, 2)

        searchAdapter.setOnItemClickCallback(object : SearchAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Movie) {
                val intent = Intent(this@SearchActivity, MovieDetailActivity::class.java)
                intent.putExtra("movie_id", data.id)
                startActivity(intent)
            }
        })
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener  {
            override fun onQueryTextSubmit(query: String): Boolean {
                val searchText = query.trim()
                if (searchText.isNotEmpty()) {
                    println("panggil viewmodel $searchText")
                    viewModel.searchMovie(searchText)
                } else {
                    Toast.makeText(this@SearchActivity, "Please enter a search query", Toast.LENGTH_SHORT).show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Optional: Handle text changes if needed
                return false
            }
        })
    }


    private fun navigateToWelcomeActivity() {
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }
}