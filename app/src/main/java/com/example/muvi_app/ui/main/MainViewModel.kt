package com.example.muvi_app.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.muvi_app.data.pref.UserModel
import com.example.muvi_app.data.response.MLCResponse
import com.example.muvi_app.data.response.MLSResponse
import com.example.muvi_app.data.response.Movie
import com.example.muvi_app.data.response.MovieDetailResponse
import com.example.muvi_app.data.response.MultMovieResponse
import com.example.muvi_app.data.response.SearchMovieResponse
import com.example.muvi_app.repository.MovieRepository
import com.example.muvi_app.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
    private val movieRepository: MovieRepository
) : ViewModel() {


    private val _movieDetail = MutableLiveData<MovieDetailResponse>()
    val movieDetail: LiveData<MovieDetailResponse> = _movieDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _movies = MutableLiveData<MultMovieResponse>()
    val movies: LiveData<MultMovieResponse> = _movies

    private val _movieListPopular = MutableLiveData<List<Movie>>()
    val movieListPopular: LiveData<List<Movie>> = _movieListPopular

    private val _movieListNow = MutableLiveData<List<Movie>>()
    val movieListNow: LiveData<List<Movie>> = _movieListNow

    private val _movieListUp = MutableLiveData<List<Movie>>()
    val movieListUp: LiveData<List<Movie>> = _movieListUp

    private val _listId = MutableLiveData<MLCResponse?>()
    val listId: LiveData<MLCResponse?> = _listId


    fun getSession(): LiveData<UserModel> = userRepository.getSession().asLiveData()

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    fun popularRecentMovie(name: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val moviePop = movieRepository.getMovieRecent("popular")
                _movieListPopular.value = moviePop

                val movieNow = movieRepository.getMovieRecent("now_playing")
                _movieListNow.value = movieNow

                val movieUp = movieRepository.getMovieRecent("upcoming")
                _movieListUp.value = movieUp
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

    fun getMovieML(userID: String){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = movieRepository.getIdCollab(userID)
                _listId.value = response
                _isLoading.value = false
            } catch (e: Exception) {
                _listId.value = MLCResponse(listOf(
                    4995,
                    3558,
                    11041,
                    12150,
                    2057,
                    13681,
                    26180,
                    17771,
                    22230,
                    42750))
                _isLoading.value = false
                _error.value = e.message ?: "Unknown error"
            }
        }
    }


    fun getMovieDetail(movieId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = movieRepository.getMovieDetail(movieId)
                _movieDetail.value = response
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = e.message ?: "Unknown error"
            }
        }
    }

    fun getMovieRecommend(movieIds: Array<String>) {
        viewModelScope.launch {
            try {
                val response = movieRepository.getMultMovie(movieIds)
                _movies.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        internal val TAG = MainViewModel::class.java.simpleName
    }
}