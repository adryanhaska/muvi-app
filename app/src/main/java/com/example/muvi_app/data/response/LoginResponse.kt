package com.example.muvi_app.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(
    @field:SerializedName("userId")
    val userId: String,

    @field:SerializedName("token")
    val token: String,
) : Parcelable