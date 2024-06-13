package com.example.muvi_app.data.response

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class SearchMovieResponse(
	val page: Int? = null,

	@field:SerializedName("total_pages")
	val totalPages: Int? = null,
	val results: List<Movie>? = null,

	@field:SerializedName("total_result")
	val totalResults: Int? = null
) : Parcelable

@Parcelize
data class Movie(
	val title: String? = null,

	val overview: String? = null,

	@field:SerializedName("original_language")
	val originalLanguage: String? = null,

	@field:SerializedName("original_title")
	val originalTitle: String? = null,

	@field:SerializedName("genre_ids")
	val genreIds: List<Int?>? = null,

	@field:SerializedName("poster_path")
	val posterPath: String? = null,

	@field:SerializedName("backdrop_path")
	val backdropPath: String? = null,

	@field:SerializedName("release_date")
	val releaseDate: String? = null,

	@field:SerializedName("vote_average")
	val voteAverage: Double? = null,

	@field:SerializedName("vote_count")
	val voteCount: Int? = null,

	val id: Int? = null,
	val adult: Boolean? = null
) : Parcelable
