package com.example.muvi_app.data.network

import com.example.muvi_app.data.response.LoginResponse
import com.example.muvi_app.data.response.MovieDetailResponse
import com.example.muvi_app.data.response.RegisterResponse
import com.example.muvi_app.data.response.SearchMovieResponse
import retrofit2.http.*
import retrofit2.Call


interface ApiService {

    @POST("register")
    suspend fun register(
        @Body registerBody: RegisterBody
    ): RegisterResponse

    data class RegisterBody(
        val username: String,
        val email: String,
        val password: String,
        val birth: String,
        val gender: Int,
        val name: String,
    )

    @POST("login")
    suspend fun login(
        @Body loginBody: LoginBody
    ): LoginResponse

    data class LoginBody(
        val email: String,
        val password: String
    )

    @GET("movie")
    suspend fun searchMovies(
        @Header("Authorization")
        token: String,

        @Query("name") name: String
    ): SearchMovieResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int
    ): MovieDetailResponse
}