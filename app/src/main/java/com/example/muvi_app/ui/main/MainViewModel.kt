package com.example.muvi_app.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.muvi_app.data.pref.UserModel
import com.example.muvi_app.repository.MovieRepository
import com.example.muvi_app.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
    movieRepository: MovieRepository
) : ViewModel() {

    fun getSession(): LiveData<UserModel> = userRepository.getSession().asLiveData()

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    companion object {
        internal val TAG = MainViewModel::class.java.simpleName
    }
}