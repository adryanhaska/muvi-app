package com.example.muvi_app.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.muvi_app.data.pref.UserModel
import com.example.muvi_app.data.response.Movie
import com.example.muvi_app.data.response.SearchMovieResponse
import com.example.muvi_app.repository.MovieRepository
import com.example.muvi_app.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class SearchViewModel (
    private val userRepository: UserRepository,
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _searchList = MutableLiveData<List<Movie>>()
    val searchList: LiveData<List<Movie>> = _searchList

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun searchMovie(name: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val movies = movieRepository.getMovies(name)
                println("moview dari repo $movies")
                _searchList.value = movies
            } catch (e: Exception) {
                handleCallError()
            } finally {
                _isLoading.value = false
            }
        }
    }


    private fun handleSearchResponse(response: Response<SearchMovieResponse>) {
        _isLoading.value = false
        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            _searchList.value = responseBody.results?.let { ArrayList(it) }
        }
    }

    private fun handleCallError() {
        _isLoading.value = false
    }

    fun getSession(): LiveData<UserModel> = userRepository.getSession().asLiveData()

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    companion object {
        internal val TAG = SearchViewModel::class.java.simpleName
    }
}