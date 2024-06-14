package com.example.muvi_app.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UserResponse(

	@field:SerializedName("UserResponse")
	val usersResponse: List<Profile?>? = null
)

@Parcelize
data class Profile(

	@field:SerializedName("following")
	val following: List<String?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("likes")
	val likes: List<String?>? = null
) : Parcelable