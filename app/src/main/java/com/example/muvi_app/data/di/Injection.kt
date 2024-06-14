package com.example.muvi_app.data.di

import android.content.Context

import com.example.muvi_app.data.pref.UserPreference
import com.example.muvi_app.data.pref.dataStore
import com.example.muvi_app.repository.MovieRepository
import com.example.muvi_app.repository.UserRepository

object Injection {
    fun providePrefRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
    fun provideMovieRepository(context: Context): MovieRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return MovieRepository.getInstance(pref)
    }

}