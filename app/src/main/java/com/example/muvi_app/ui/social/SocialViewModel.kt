package com.example.muvi_app.ui.social

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.muvi_app.data.pref.UserModel
import com.example.muvi_app.data.response.Movie
import com.example.muvi_app.data.response.UserDetailResponse
import com.example.muvi_app.repository.MovieRepository
import com.example.muvi_app.repository.UserRepository
import kotlinx.coroutines.launch

class SocialViewModel(
    private val userRepository: UserRepository,
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _userDetail = MutableLiveData<UserDetailResponse>()
    val userDetail: LiveData<UserDetailResponse> = _userDetail

    private val _loggedInUserDetail = MutableLiveData<UserDetailResponse>()
    val loggedInUserDetail: LiveData<UserDetailResponse> = _loggedInUserDetail

    private val _favoriteMovies = MutableLiveData<List<Movie>?>()
    val favoriteMovies: MutableLiveData<List<Movie>?> = _favoriteMovies


    fun getSession(): LiveData<UserModel> = userRepository.getSession().asLiveData()

    fun getUserDetail(userId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = userRepository.getUserDetail(userId)
                _userDetail.value = response
                fetchFavoriteMovies(response.likes)
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = e.message ?: "Unknown error"
            }
        }
    }

    private fun fetchFavoriteMovies(movieIds: List<Int?>?) {
        viewModelScope.launch {
            try {
                val movieIdStrings = movieIds?.map { it.toString() }?.toTypedArray()
                val response = movieIdStrings?.let { movieRepository.getMultMovie(it) }
                if (response != null) {
                    _favoriteMovies.value = response.movies as List<Movie>?
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error fetching favorite movies"
            }
        }
    }

    fun getLoggedInUserDetail(userId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = userRepository.getUserDetail(userId)
                _loggedInUserDetail.value = response
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = e.message ?: "Unknown error"
            }
        }
    }

    fun followUser(userId: String, onComplete: (Result<String>) -> Unit) {
        viewModelScope.launch {
            try {
                val response = userRepository.followUser(userId)
                if (response.isSuccessful) {
                    val message = response.body()?.message ?: "Friend added successfully"
                    onComplete(Result.success(message))
                } else {
                    val errorMessage = "Failed to follow user: ${response.message()}"
                    onComplete(Result.failure(Exception(errorMessage)))
                }
            } catch (e: Exception) {
                onComplete(Result.failure(e))
            }
        }
    }

    fun unfollowUser(userId: String, onComplete: (Result<String>) -> Unit) {
        viewModelScope.launch {
            try {
                val response = userRepository.unfollowUser(userId)
                if (response.isSuccessful) {
                    val message = response.body()?.message ?: "Friend removed successfully"
                    onComplete(Result.success(message))
                } else {
                    val errorMessage = "Failed to unfollow user: ${response.message()}"
                    onComplete(Result.failure(Exception(errorMessage)))
                }
            } catch (e: Exception) {
                onComplete(Result.failure(e))
            }
        }
    }

    companion object {
        internal val TAG = SocialViewModel::class.java.simpleName
    }
}