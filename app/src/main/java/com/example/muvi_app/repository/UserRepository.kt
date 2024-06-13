package com.example.muvi_app.repository

import android.util.Log
import com.example.muvi_app.data.network.ApiConfig
import com.example.muvi_app.data.network.ApiService
import com.example.muvi_app.data.pref.UserModel
import com.example.muvi_app.data.pref.UserPreference
import com.example.muvi_app.data.response.LoginResponse
import com.example.muvi_app.data.response.Movie
import com.example.muvi_app.data.response.Profile
import com.example.muvi_app.data.response.RegisterResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class UserRepository(
    private val userPreference: UserPreference
) {

    private val apiService: ApiService = ApiConfig.getApiService()

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun register(
        username: String,
        email: String,
        password: String,
        birth: String,
        gender: Int,
        name: String,
    ): RegisterResponse {
        val registerBody = ApiService.RegisterBody(username, email, password, birth, gender, name)
        return apiService.register(registerBody)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        val loginBody = ApiService.LoginBody(email, password)
        val response = apiService.login(loginBody)
        if (response.token.isNotEmpty()) {
            saveUserSession(email, response)
        }
        return response
    }



    suspend fun getUser(token: String, userId: String): List<Profile?>? {
        return try {
            val response = apiService.getUser(token, userId)
            response.usersResponse
        } catch (e: Exception) {
            null
        }
    }


    suspend fun logout() {
        userPreference.logout()
    }

    private suspend fun saveUserSession(email: String, response: LoginResponse) {
        println(response)
        val user = UserModel(email = email, token = response.token, id = response.userId, isLogin = true)
        userPreference.saveSession(user)
        logUserSession()
    }

    private suspend fun logUserSession() {
        val session = userPreference.getSession().first()
        Log.d("Login Berhasil", session.toString())
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(userPreference: UserPreference): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference)
            }.also { instance = it }
    }
}
