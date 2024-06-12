package com.example.muvi_app.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginErrorResponse(
    @field:SerializedName("message")
    val message: String,
) : Parcelable