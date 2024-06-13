package com.example.muvi_app.data.network

import com.example.muvi_app.data.response.MLCResponse
import com.example.muvi_app.data.response.MLSResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServiceML {
    @GET("recommend-synopsys")
    suspend fun getSynopsys(
        @Query("id") movieId: Int
    ): MLSResponse

    @GET("recommendation-collab")
    suspend fun getColab(
        @Query("id") userId: String
    ): MLCResponse
}