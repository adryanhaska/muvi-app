package com.example.muvi_app.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class UserResponse(

	@field:SerializedName("UserResponse")
	val usersResponse: List<Profile?>? = null
)
@Parcelize
data class Profile(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("following")
	val following: List<String?>? = null,

	@field:SerializedName("likes")
	val likes: List<Int?>? = null,

	@field:SerializedName("username")
	val username: String? = null,
) : Parcelable
