package com.example.muvi_app.data.network

import com.example.muvi_app.data.response.LoginResponse
import com.example.muvi_app.data.response.RegisterResponse
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name")
        name: String,

        @Field("email")
        email: String,

        @Field("password")
        password: String
    ): RegisterResponse

    @POST("login")
    suspend fun login(
        @Body loginBody: LoginBody
    ): LoginResponse

    data class LoginBody(
        val email: String,
        val password: String
    )

}