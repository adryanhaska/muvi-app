package com.example.muvi_app.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class MLGResponse(
	@field:SerializedName("rec_1")
	val rec1: String,

	@field:SerializedName("rec_2")
	val rec2: String,

	@field:SerializedName("rec_3")
	val rec3: String,

	@field:SerializedName("rec_4")
	val rec4: String,

	@field:SerializedName("rec_5")
	val rec5: String
) : Parcelable
