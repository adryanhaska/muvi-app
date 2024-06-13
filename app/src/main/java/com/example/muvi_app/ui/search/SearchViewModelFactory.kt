package com.example.muvi_app.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.muvi_app.repository.MovieRepository
import com.example.muvi_app.repository.UserRepository

class SearchViewModelFactory(
    private val userRepository: UserRepository,
    private val movieRepository: MovieRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(userRepository, movieRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
