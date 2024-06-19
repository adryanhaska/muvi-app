package com.example.muvi_app.data.response

import com.google.gson.annotations.SerializedName

data class UserDetailResponse(

	@field:SerializedName("following")
	val following: List<String?>? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("likes")
	val likes: List<Int?>? = null
)