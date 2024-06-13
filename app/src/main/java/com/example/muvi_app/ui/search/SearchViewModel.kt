package com.example.muvi_app.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvi_app.data.response.Movie
import com.example.muvi_app.data.response.Profile
import com.example.muvi_app.repository.MovieRepository
import com.example.muvi_app.repository.UserRepository
import kotlinx.coroutines.launch

class SearchViewModel(
    private val userRepository: UserRepository,
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _movieList = MutableLiveData<List<Movie>>()
    val movieList: LiveData<List<Movie>> = _movieList

    private val _peopleList = MutableLiveData<List<Profile>?>()
    val peopleList: MutableLiveData<List<Profile>?> = _peopleList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun searchMovies(name: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val movies = movieRepository.getMovies(name)
                _movieList.value = movies
            } catch (e: Exception) {
                handleCallError()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchPeople(username: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val users = userRepository.getUser("your_token_here", username)
                _peopleList.value = users as List<Profile>?
            } catch (e: Exception) {
                handleCallError()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun handleCallError() {
        _isLoading.value = false
    }
}