package com.example.muvi_app.ui.detail

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.muvi_app.data.adapter.CastPagerAdapter
import com.example.muvi_app.databinding.ActivityDetailBinding
import com.example.muvi_app.ui.ViewModelFactory
import android.webkit.WebView
import com.example.muvi_app.ui.webView.WebViewActivity

class MovieDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<MovieDetailViewModel> { ViewModelFactory.getInstance(this) }

    private lateinit var castPagerAdapter: CastPagerAdapter
//    private lateinit var similarMoviesAdapter: SimilarMoviesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        observeViewModel()
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

//        similarMoviesAdapter = SimilarMoviesAdapter()
//        binding.rvSimilarMovies.apply {
//            layoutManager = GridLayoutManager(this@MovieDetailActivity, 2)
//            adapter = similarMoviesAdapter
//        }
    }

    private fun observeViewModel() {
        val movieId = intent.getIntExtra("movie_id", -1)
        if (movieId == -1) {
            Toast.makeText(this, "Movie ID not found", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            viewModel.getMovieDetail(movieId)
        }

        viewModel.movieDetail.observe(this, Observer { movieDetail ->
            // Update UI with movie detail data
            binding.movieTitle.text = movieDetail.title
            binding.movieDescription.text = movieDetail.overview
            binding.movieDuration.text = "${movieDetail.runtime} min" // Example for duration

            binding.movieGenre.text = movieDetail.genres?.joinToString(", ") { it?.name ?: "" }
            Glide.with(this)
                .load("https://image.tmdb.org/t/p/original${movieDetail.posterPath}")
                .into(binding.moviePoster)



            // Update ViewPager2 adapter with cast data
            movieDetail.credits?.cast?.let {
                castPagerAdapter.submitList(it)
            }

            // Update RecyclerView adapter with similar movies data
//            movieDetail.similarMovies?.let {
//                similarMoviesAdapter.submitList(it)
//            }
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
            Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
        })
    }
}
