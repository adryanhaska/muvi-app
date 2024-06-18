package com.example.muvi_app.data.network

import com.example.muvi_app.data.response.FollowResponse
import com.example.muvi_app.data.response.LoginResponse
import com.example.muvi_app.data.response.MLCResponse
import com.example.muvi_app.data.response.MLSResponse
import com.example.muvi_app.data.response.MovieDetailResponse
import com.example.muvi_app.data.response.MultMovieResponse
import com.example.muvi_app.data.response.RegisterResponse
import com.example.muvi_app.data.response.SearchMovieResponse
import com.example.muvi_app.data.response.UnfollowResponse
import com.example.muvi_app.data.response.UserDetailResponse
import com.example.muvi_app.data.response.UserProfile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


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

    @GET("movie/id/{movie_id}")
    suspend fun getMovieDetail(
        @Header("Authorization")
        token: String,

        @Path("movie_id") movieId: Int
    ): MovieDetailResponse

    @GET("movie/recent")
    suspend fun getRecentMovie(
        @Header("Authorization")
        token: String,

        @Query("type") type: String
    ): SearchMovieResponse


    @GET("user")
    suspend fun getUser(
        @Header("Authorization") token: String,
        @Query("username") username: String
    ): List<UserProfile>

    @GET("user/{user_id}")
    suspend fun getUserDetail(
        @Header("Authorization")
        token: String,

        @Path("user_id") userId: String
    ): UserDetailResponse

    @PUT("user/{user_id}/friend")
    suspend fun followUser(
        @Header("Authorization")
        token: String,

        @Path("user_id")
        userId: String
    ): Response<FollowResponse>

    @DELETE("user/{user_id}/friend")
    suspend fun unfollowUser(
        @Header("Authorization")
        token: String,

        @Path("user_id")
        userId: String
    ): Response<UnfollowResponse>

    @POST("movie/id")
    suspend fun getMultMovie(
        @Header("Authorization")
        token: String,
        @Body multId: MultId
    ): MultMovieResponse

    data class MultId(
        val movieIds: Array<String>
    )

    @GET("recommend-synopsys")
    suspend fun getSynopsys(
        @Query("id") id: Int
    ): MLSResponse

    @GET("recommendation-collab")
    suspend fun getColab(
        @Query("id") id: String
    ): MLCResponse

}