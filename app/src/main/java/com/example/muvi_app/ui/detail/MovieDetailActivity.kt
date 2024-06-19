package com.example.muvi_app.ui.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.muvi_app.R
import com.example.muvi_app.data.adapter.CastPagerAdapter
import com.example.muvi_app.data.response.Movie
import com.example.muvi_app.databinding.ActivityDetailBinding
import com.example.muvi_app.ui.ViewModelFactory
import com.example.muvi_app.ui.main.MoviesAdapter
import com.example.muvi_app.ui.webView.WebViewActivity

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<MovieDetailViewModel> { ViewModelFactory.getInstance(this) }

    private lateinit var moviesAdapter: MoviesAdapter
    private lateinit var moviesAdapterGenre: MoviesAdapter
    private lateinit var castPagerAdapter: CastPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeViewModel()
        setupRecyclerView()

        val movieId = intent.getIntExtra("movie_id", -1)
        if (movieId == -1) {
            Toast.makeText(this, "Movie ID not found", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Log.d("MovieDetailActivity", "Movie ID: $movieId")
            viewModel.getMovieDetail(movieId)
            viewModel.getMovieML(movieId)
            viewModel.getMovieMLG(movieId)
            viewModel.checkIfMovieIsLiked(movieId)
        }
    }

    private fun setupRecyclerView() {
        val recyclerViewPopular = findViewById<RecyclerView>(R.id.rvSimilarMovies)
        moviesAdapter = MoviesAdapter()
        moviesAdapter.setOnItemClickCallback(object : MoviesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Movie) {
                val intent = Intent(this@MovieDetailActivity, MovieDetailActivity::class.java)
                intent.putExtra("movie_id", data.id)
                startActivity(intent)
            }
        })
        recyclerViewPopular.apply {
            layoutManager = LinearLayoutManager(this@MovieDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = moviesAdapter
        }

        val recyclerViewGenre = findViewById<RecyclerView>(R.id.rvSimilarGenre)
        moviesAdapterGenre = MoviesAdapter()
        moviesAdapterGenre.setOnItemClickCallback(object : MoviesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Movie) {
                val intent = Intent(this@MovieDetailActivity, MovieDetailActivity::class.java)
                intent.putExtra("movie_id", data.id)
                startActivity(intent)
            }
        })
        recyclerViewGenre.apply {
            layoutManager = LinearLayoutManager(this@MovieDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = moviesAdapterGenre
        }
    }

    private fun setupUI() {
        // Setup adapters for ViewPager2 and RecyclerView
        castPagerAdapter = CastPagerAdapter()
        binding.castViewPager.adapter = castPagerAdapter

        binding.watchTrailerButton.setOnClickListener {
            println("url ${viewModel.movieDetail.value?.trailerUrl}")
            viewModel.movieDetail.value?.trailerUrl?.let { url ->
                println(url)
                if (url.isEmpty()) {
                    Toast.makeText(this, "No trailer available", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(this, WebViewActivity::class.java).apply {
                        putExtra(WebViewActivity.EXTRA_URL, url)
                    }
                    startActivity(intent)
                }
            }
        }

        binding.addToFavoriteButton.setOnClickListener {
            val movieId = viewModel.movieDetail.value?.id ?: return@setOnClickListener
            if (viewModel.isMovieLiked.value == true) {
                viewModel.unlikeMovie(movieId)
            } else {
                viewModel.likeMovie(movieId)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.movieDetail.observe(this, Observer { movieDetail ->
            // Update UI with movie detail data
            binding.movieTitle.text = movieDetail.title
            binding.movieDescription.text = movieDetail.overview
            binding.movieDuration.text = "${movieDetail.runtime} min" // Example for duration
            binding.movieGenre.text = movieDetail.genres?.joinToString(", ") { it?.name ?: "" }
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/original${movieDetail.posterPath}")
                .into(binding.moviePoster)

            movieDetail.credits?.cast?.let {
                castPagerAdapter.submitList(it)
            }
        })

        viewModel.isMovieLiked.observe(this, Observer { isLiked ->
            binding.addToFavoriteButton.text = if (isLiked) {
                getString(R.string.remove_favorite)
            } else {
                getString(R.string.add_to_favorite)
            }
        })

        viewModel.likeSuccess.observe(this, Observer { success ->
            if (success) {
                Toast.makeText(this, "Movie liked successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to like movie", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.unlikeSuccess.observe(this, Observer { success ->
            if (success) {
                Toast.makeText(this, "Movie unliked successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to unlike movie", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.listId.observe(this, Observer { ids ->
            val dum = arrayOf("10138", "112454", "629015", "68721", "10623")
            ids?.recommendations?.let { recommendations ->
                println("ids: $recommendations")

                try {
                    // Get the recommended movies once the session is available
                    if (recommendations.isNotEmpty()) {
                        viewModel.getMovieRecommend(recommendations.toTypedArray())
                    } else {
                        // Handle case where recommendations list is empty
                        viewModel.getMovieRecommend(dum)
                    }
                } catch (e: Exception) {
                    Log.e("Error fetching recommendations", e.toString())
                    viewModel.getMovieRecommend(dum)
                }
            } ?: run {
                // Handle case where ids is null
                viewModel.getMovieRecommend(dum)
            }
        })

        viewModel.listIdGenre.observe(this, Observer { ids ->
            val dum = arrayOf("574074", "779047", "1084244", "976573", "897192")
            ids?.recommendations?.let { recommendations ->
                println("ids: $recommendations")

                try {
                    // Get the recommended movies once the session is available
                    if (recommendations.isNotEmpty()) {
                        viewModel.getMovieRecommendGenre(recommendations.toTypedArray())
                    } else {
                        // Handle case where recommendations list is empty
                        viewModel.getMovieRecommendGenre(dum)
                    }
                } catch (e: Exception) {
                    Log.e("Error fetching recommendations", e.toString())
                    viewModel.getMovieRecommendGenre(dum)
                }
            } ?: run {
                // Handle case where ids is null
                viewModel.getMovieRecommendGenre(dum)
            }
        })

        viewModel.movies.observe(this, Observer { response ->
            moviesAdapter.submitList(response.movies as List<Movie>)
        })

        viewModel.moviesGenre.observe(this, Observer { response ->
            println("tersbumit $response")
            moviesAdapterGenre.submitList(response.movies as List<Movie>)
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            // Show loading indicator
            if (isLoading) {
                // Show progress bar or loading animation
            } else {
                // Hide progress bar or loading animation
            }
        })

        viewModel.error.observe(this, Observer { error ->
            // Show error message
            println(error)
            Toast.makeText(this, "Error v: $error", Toast.LENGTH_SHORT).show()
        })
    }
}
