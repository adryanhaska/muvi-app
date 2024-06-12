package com.example.muvi_app.ui.login

import com.example.muvi_app.data.response.LoginErrorResponse
import com.example.muvi_app.data.response.LoginResponse
import com.example.muvi_app.repository.UserRepository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _loginErrorResponse = MutableLiveData<LoginErrorResponse>()
    val loginErrorResponse: LiveData<LoginErrorResponse> = _loginErrorResponse

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = userRepository.login(email, password)
                _loginResponse.value = response
            } catch (e: HttpException) {
                Log.d(TAG, "Gagal Login! $e")
                handleHttpException(e)
            }
        }
    }

    private fun handleHttpException(e: HttpException) {
        val jsonInString = e.response()?.errorBody()?.string()
        val errorBody = Gson().fromJson(jsonInString, LoginErrorResponse::class.java)
        val errorMessage = errorBody.message
        _loginErrorResponse.value = errorBody  // Update the error response LiveData
        Log.d(TAG, "Gagal Login! $errorMessage")
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}
