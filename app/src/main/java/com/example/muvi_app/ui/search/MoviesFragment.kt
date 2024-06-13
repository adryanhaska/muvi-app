package com.example.muvi_app.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.muvi_app.data.adapter.SearchAdapter
import com.example.muvi_app.data.response.Movie
import com.example.muvi_app.data.response.SearchMovieResponse
import com.example.muvi_app.databinding.FragmentMoviesBinding
import com.example.muvi_app.ui.detail.MovieDetailActivity

class MoviesFragment : Fragment() {

    private lateinit var binding: FragmentMoviesBinding
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeMovieList()
    }

    private fun setupRecyclerView() {
        searchAdapter = SearchAdapter(object : SearchAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Any) {
                if (data is Movie) {
                    val intent = Intent(context, MovieDetailActivity::class.java).apply {
                        putExtra("movie_id", data.id)
                    }
                    startActivity(intent)
                }
            }
        })

        val layoutManager = GridLayoutManager(context, 2)
        binding.rvMovieResult.layoutManager = layoutManager
        binding.rvMovieResult.adapter = searchAdapter
    }

    private fun observeMovieList() {
        val viewModel = (activity as SearchActivity).viewModel
        viewModel.movieList.observe(viewLifecycleOwner) { movies ->
            movies?.let {
                searchAdapter.setData(it)
            }
        }
    }
}