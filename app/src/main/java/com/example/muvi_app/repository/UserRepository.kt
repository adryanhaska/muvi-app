package com.example.muvi_app.repository

import android.util.Log
import com.example.muvi_app.data.network.ApiConfig
import com.example.muvi_app.data.network.ApiService
import com.example.muvi_app.data.pref.UserModel
import com.example.muvi_app.data.pref.UserPreference
import com.example.muvi_app.data.response.FollowResponse
import com.example.muvi_app.data.response.LoginResponse
import com.example.muvi_app.data.response.RegisterResponse
import com.example.muvi_app.data.response.UnfollowResponse
import com.example.muvi_app.data.response.UserDetailResponse
import com.example.muvi_app.data.response.UserProfile
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import retrofit2.HttpException
import retrofit2.Response

class UserRepository(
    private val userPreference: UserPreference
) {

    private val apiService: ApiService = ApiConfig.getApiService(userPreference)

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

    suspend fun getUserId(): String? {
        return userPreference.getUserId()
    }

    suspend fun getUser(username: String): List<UserProfile>? {
        val token = userPreference.getToken()
        return try {
            apiService.getUser("Bearer $token", username)
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            null
        } catch (e: JsonSyntaxException) {
            null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getUserDetail(userId: String): UserDetailResponse {
        val token = userPreference.getToken()
        return apiService.getUserDetail("Bearer $token", userId)
    }

    suspend fun followUser(userId: String): Response<FollowResponse> {
        val token = userPreference.getToken()
        return apiService.followUser("Bearer $token", userId)
    }

    suspend fun unfollowUser(userId: String): Response<UnfollowResponse> {
        val token = userPreference.getToken()
        return apiService.unfollowUser("Bearer $token", userId)
    }

    suspend fun logout() {
        userPreference.logout()
    }

    private suspend fun saveUserSession(email: String, response: LoginResponse) {
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