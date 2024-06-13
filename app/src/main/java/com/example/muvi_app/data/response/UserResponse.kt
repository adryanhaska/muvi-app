package com.example.muvi_app.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class UserResponse(
	val following: List<String?>? = null,
	val id: String? = null,
	val username: String? = null,
	val likes: List<Int?>? = null
) : Parcelable
