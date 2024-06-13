package com.example.muvi_app.data.response

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class MovieDetailResponse(
	@SerializedName("original_language")
	val originalLanguage: String? = null,

	@SerializedName("imdb_id")
	val imdbId: String? = null,

	val video: Boolean? = null,

	val title: String? = null,

	@SerializedName("backdrop_path")
	val backdropPath: String? = null,

	val credits: Credits? = null,

	val genres: List<GenresItem?>? = null,

	@SerializedName("production_countries")
	val productionCountries: List<ProductionCountriesItem?>? = null,

	val id: Int? = null,

	@SerializedName("vote_count")
	val voteCount: Int? = null,

	val budget: Int? = null,

	val overview: String? = null,

	@SerializedName("original_title")
	val originalTitle: String? = null,

	val runtime: Int? = null,

	val trailerUrl: String? = null,

	@SerializedName("poster_path")
	val posterPath: String? = null,

	@SerializedName("origin_country")
	val originCountry: List<String?>? = null,

	@SerializedName("spoken_languages")
	val spokenLanguages: List<SpokenLanguagesItem?>? = null,

	@SerializedName("production_companies")
	val productionCompanies: List<ProductionCompaniesItem?>? = null,

	@SerializedName("release_date")
	val releaseDate: String? = null,

	@SerializedName("belongs_to_collection")
	val belongsToCollection: BelongsToCollection? = null,

	val tagline: String? = null,

	val adult: Boolean? = null,

	val homepage: String? = null,

	val status: String? = null
) : Parcelable

@Parcelize
data class SpokenLanguagesItem(
	val name: String? = null,
	@SerializedName("iso_639_1")
	val iso6391: String? = null,
	@SerializedName("english_name")
	val englishName: String? = null
) : Parcelable

@Parcelize
data class ProductionCompaniesItem(
	@SerializedName("logo_path")
	val logoPath: String? = null,
	val name: String? = null,
	val id: Int? = null,
	@SerializedName("origin_country")
	val originCountry: String? = null
) : Parcelable

@Parcelize
data class ProductionCountriesItem(
	@SerializedName("iso_3166_1")
	val iso31661: String? = null,
	val name: String? = null
) : Parcelable

@Parcelize
data class GenresItem(
	val name: String? = null,
	val id: Int? = null
) : Parcelable

@Parcelize
data class BelongsToCollection(
	@SerializedName("backdrop_path")
	val backdropPath: String? = null,
	val name: String? = null,
	val id: Int? = null,
	@SerializedName("poster_path")
	val posterPath: String? = null
) : Parcelable

@Parcelize
data class CastItem(
	@SerializedName("cast_id")
	val castId: Int? = null,
	val character: String? = null,
	val gender: Int? = null,
	@SerializedName("credit_id")
	val creditId: String? = null,
	@SerializedName("known_for_department")
	val knownForDepartment: String? = null,
	@SerializedName("original_name")
	val originalName: String? = null,
	val name: String? = null,
	@SerializedName("profile_path")
	val profilePath: String? = null,
	val id: Int? = null,
	val adult: Boolean? = null,
	val order: Int? = null
) : Parcelable

@Parcelize
data class CrewItem(
	val gender: Int? = null,
	@SerializedName("credit_id")
	val creditId: String? = null,
	@SerializedName("known_for_department")
	val knownForDepartment: String? = null,
	@SerializedName("original_name")
	val originalName: String? = null,
	val name: String? = null,
	@SerializedName("profile_path")
	val profilePath: String? = null,
	val id: Int? = null,
	val adult: Boolean? = null,
	val department: String? = null,
	val job: String? = null
) : Parcelable

@Parcelize
data class Credits(
	val cast: List<CastItem?>? = null,
	val crew: List<CrewItem?>? = null
) : Parcelable
