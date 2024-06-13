package com.example.muvi_app.repository

import com.example.muvi_app.data.network.ApiConfig
import com.example.muvi_app.data.pref.UserPreference
import com.example.muvi_app.data.response.Movie
import com.example.muvi_app.data.response.MovieDetailResponse
import kotlinx.coroutines.flow.first

class MovieRepository(private val userPreference: UserPreference) {

    private val movieApiService = ApiConfig.getApiService()

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
        return movieApiService.getMovieDetail(movieId)
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