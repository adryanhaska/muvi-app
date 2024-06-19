package com.example.muvi_app.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class LikeResponse(
	val movie: String? = null,
	val message: String? = null
) : Parcelable
