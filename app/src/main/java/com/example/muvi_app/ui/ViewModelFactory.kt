package com.example.muvi_app.ui

import com.example.muvi_app.data.di.Injection
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.muvi_app.repository.MovieRepository
import com.example.muvi_app.repository.UserRepository
import com.example.muvi_app.ui.detail.MovieDetailViewModel
import com.example.muvi_app.ui.login.LoginViewModel
import com.example.muvi_app.ui.main.MainViewModel
import com.example.muvi_app.ui.search.SearchViewModel
import com.example.muvi_app.ui.settings.SettingsViewModel
import com.example.muvi_app.ui.signup.SignUpViewModel
import com.example.muvi_app.ui.social.SocialViewModel

class ViewModelFactory(
    private val userRepository: UserRepository,
    private val movieRepository: MovieRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository, movieRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(userRepository, movieRepository) as T
            }
            modelClass.isAssignableFrom(SocialViewModel::class.java) -> {
                SocialViewModel(userRepository, movieRepository) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(userRepository, movieRepository) as T
            }
            modelClass.isAssignableFrom(MovieDetailViewModel::class.java) -> {
                MovieDetailViewModel(userRepository, movieRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(
                        Injection.providePrefRepository(context),
                        Injection.provideMovieRepository(context)
                    )
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}