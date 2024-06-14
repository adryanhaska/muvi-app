package com.example.muvi_app.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class MLSResponse(
	@field:SerializedName("recommendations")
	val recommendations: List<String>
) : Parcelable
