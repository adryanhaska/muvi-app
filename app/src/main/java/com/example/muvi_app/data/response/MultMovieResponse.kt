package com.example.muvi_app.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class MultMovieResponse(
	val movies: List<Movie?>? = null
) : Parcelable


