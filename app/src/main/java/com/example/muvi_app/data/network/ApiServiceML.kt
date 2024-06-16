package com.example.muvi_app.data.network

import com.example.muvi_app.data.response.MLCResponse
import com.example.muvi_app.data.response.MLSResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiServiceML {
    @Headers("Accept: application/json; charset=utf-8")
    @GET("recommend-synopsys")
    suspend fun getSynopsys(
        @Query("id") movieId: Int
    ): ResponseBody

    @POST("recommend-metadata")
    suspend fun getGenre(
        @Body recommendBody: RecommendBody
    ): ResponseBody

    data class RecommendBody(
        val input: String,
    )

    @GET("recommendation-collab")
    suspend fun getColab(
        @Query("id") userId: String
    ): MLCResponse
}