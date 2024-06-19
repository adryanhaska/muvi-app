package com.example.muvi_app.ui.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.muvi_app.data.network.ApiService
import com.example.muvi_app.data.response.RegisterErrorResponse
import com.example.muvi_app.data.response.RegisterResponse
import com.example.muvi_app.repository.UserRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignUpViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerResponse: LiveData<RegisterResponse> = _registerResponse


    fun register(
        username: String,
        email: String,
        password: String,
        birth: String,
        gender: Int,
        name: String,
        ) {
        viewModelScope.launch {
            try {
                val response = userRepository.register(username, email, password, birth, gender, name)
                _registerResponse.value = response
            } catch (e: HttpException) {
                handleHttpException(e)
            } catch (e: Exception) {
            }
        }
    }

    private fun handleHttpException(e: HttpException) {
        val jsonInString = e.response()?.errorBody()?.string()
        val errorBody = Gson().fromJson(jsonInString, RegisterErrorResponse::class.java)
        val errorMessage = errorBody?.message ?: "Unknown error"
    }

    companion object {
        internal val TAG = SignUpViewModel::class.java.simpleName
    }
}
