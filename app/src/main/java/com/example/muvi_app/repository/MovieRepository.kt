package com.example.muvi_app.repository

import com.example.muvi_app.data.pref.UserPreference

class MovieRepository(private val userPreference: UserPreference) {
    companion object {
        @Volatile
        private var instance: MovieRepository? = null

        fun getInstance(userPreference: UserPreference): MovieRepository =
            instance ?: synchronized(this) {
                instance ?: MovieRepository(userPreference)
            }.also { instance = it }
    }
}