package com.example.muvi_app.ui.profile

import android.util.Log
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
import com.example.muvi_app.ui.social.SocialViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProfileViewModel(
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

    fun getLoggedInUserDetail(userId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = userRepository.getUserDetail(userId)
                _loggedInUserDetail.value = response
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

    companion object {
        internal val TAG = ProfileViewModel::class.java.simpleName
    }
}
