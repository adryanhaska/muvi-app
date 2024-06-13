package com.example.muvi_app.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.muvi_app.data.pref.UserModel
import com.example.muvi_app.data.response.MLSResponse
import com.example.muvi_app.data.response.MovieDetailResponse
import com.example.muvi_app.data.response.MultMovieResponse
import com.example.muvi_app.repository.MovieRepository
import com.example.muvi_app.repository.UserRepository
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val userRepository: UserRepository,
    private val movieRepository: MovieRepository) : ViewModel() {

    private val _movieDetail = MutableLiveData<MovieDetailResponse>()
    val movieDetail: LiveData<MovieDetailResponse> = _movieDetail

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _movies = MutableLiveData<MultMovieResponse>()
    val movies: LiveData<MultMovieResponse> = _movies

    private val _listId = MutableLiveData<MLSResponse>()
    val listId: LiveData<MLSResponse> = _listId

    fun getSession(): LiveData<UserModel> = userRepository.getSession().asLiveData()

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

    fun getMovieML(movieId: Int){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = movieRepository.getIdSynopsys(movieId)
                _listId.value = response
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
}