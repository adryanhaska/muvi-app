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
import com.example.muvi_app.data.response.UserDetailResponse
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

    private val _moviesGenre = MutableLiveData<MultMovieResponse>()
    val moviesGenre: LiveData<MultMovieResponse> = _moviesGenre

    private val _listId = MutableLiveData<MLSResponse?>()
    val listId: LiveData<MLSResponse?> = _listId

    private val _listIdGenre = MutableLiveData<MLSResponse?>()
    val listIdGenre: LiveData<MLSResponse?> = _listIdGenre

    private val _userDetail = MutableLiveData<UserDetailResponse>()
    val userDetail: LiveData<UserDetailResponse> = _userDetail

    private val _isMovieLiked = MutableLiveData<Boolean>()
    val isMovieLiked: LiveData<Boolean> = _isMovieLiked

    private val _likeSuccess = MutableLiveData<Boolean>()
    val likeSuccess: LiveData<Boolean> = _likeSuccess

    private val _unlikeSuccess = MutableLiveData<Boolean>()
    val unlikeSuccess: LiveData<Boolean> = _unlikeSuccess

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

    fun checkIfMovieIsLiked(movieId: Int?) {
        viewModelScope.launch {
            try {
                val userId = userRepository.getUserId()
                if (userId != null && movieId != null) {
                    val userDetails = userRepository.getUserDetail(userId)
                    _userDetail.value = userDetails
                    _isMovieLiked.value = userDetails.likes?.contains(movieId) == true
                } else {
                    _isMovieLiked.value = false
                }
            } catch (e: Exception) {
                _error.value = e.message
                _isMovieLiked.value = false
            }
        }
    }

    fun likeMovie(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = movieRepository.likeMovie(movieId)
                if (response.isSuccessful) {
                    _likeSuccess.value = true
                    checkIfMovieIsLiked(movieId) // Refresh like state
                } else {
                    _likeSuccess.value = false
                }
            } catch (e: Exception) {
                _likeSuccess.value = false
            }
        }
    }

    fun unlikeMovie(movieId: Int) {
        viewModelScope.launch {
            try {
                val response = movieRepository.unlikeMovie(movieId)
                if (response.isSuccessful) {
                    _unlikeSuccess.value = true
                    checkIfMovieIsLiked(movieId) // Refresh like state
                } else {
                    _unlikeSuccess.value = false
                }
            } catch (e: Exception) {
                _unlikeSuccess.value = false
            }
        }
    }

    fun getMovieML(movieId: Int){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = movieRepository.getIdSynopsys(movieId)
                println("GGS $response")
                _listId.value = response
                _isLoading.value = false
            } catch (e: Exception) {
                _listId.value = MLSResponse(listOf(
                    "112454",
                    "629015",
                    "68721",
                    "10623"))
                _isLoading.value = false
                _error.value = e.message ?: "Unknown error"
            }
        }
    }

    fun getMovieMLG(movieId: Int){
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = movieRepository.getIdGenres(movieId)
                println("GGR $response")
                _listIdGenre.value = response
                _isLoading.value = false
            } catch (e: Exception) {
                _listIdGenre.value = MLSResponse(listOf(
                    "574074",
                    "779047",
                    "1084244",
                    "976573",
                    "897192"))
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

    fun getMovieRecommendGenre(movieIds: Array<String>) {
        viewModelScope.launch {
            try {
                val response = movieRepository.getMultMovie(movieIds)
                _moviesGenre.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}