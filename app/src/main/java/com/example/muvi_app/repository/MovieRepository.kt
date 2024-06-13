package com.example.muvi_app.repository

import com.example.muvi_app.data.network.ApiConfig
import com.example.muvi_app.data.network.ApiService
import com.example.muvi_app.data.pref.UserPreference
import com.example.muvi_app.data.response.MLCResponse
import com.example.muvi_app.data.response.MLSResponse
import com.example.muvi_app.data.response.Movie
import com.example.muvi_app.data.response.MovieDetailResponse
import com.example.muvi_app.data.response.MultMovieResponse
import kotlinx.coroutines.flow.first

class MovieRepository(private val userPreference: UserPreference) {

    private val movieApiService = ApiConfig.getApiService()
    private val mlApiService = ApiConfig.getApiServiceMl()

    suspend fun getMovies(name: String): List<Movie> {
        val token = "Bearer ${userPreference.getSession().first().token}"
        println("Running search $name with token $token")

        try {
            val response = movieApiService.searchMovies(token, name)
            println("Response data ${response.results}")
            return response.results ?: emptyList()
        } catch (e: Exception) {
            println("Error fetching movies: ${e.message}")
            return emptyList()
        }
    }

    suspend fun getMovieDetail(movieId: Int): MovieDetailResponse {
        val token = "Bearer ${userPreference.getSession().first().token}"
        return movieApiService.getMovieDetail(token, movieId)
    }

    suspend fun getMultMovie(movieIds: Array<String>): MultMovieResponse {
        val token = "Bearer ${userPreference.getSession().first().token}"
        val Ids = ApiService.MultId(movieIds)
        println("Id $Ids")
        return movieApiService.getMultMovie(token, Ids)
    }

    suspend fun getIdCollab(userId: String): MLCResponse {
        return mlApiService.getColab(userId)
    }

    suspend fun getIdSynopsys(movieId: Int): MLSResponse {
        println("getid $movieId")
        println("return syn ${mlApiService.getSynopsys(movieId)}")
        return mlApiService.getSynopsys(movieId)
    }

    companion object {
        @Volatile
        private var instance: MovieRepository? = null

        fun getInstance(userPreference: UserPreference): MovieRepository =
            instance ?: synchronized(this) {
                instance ?: MovieRepository(userPreference)
            }.also { instance = it }
    }
}