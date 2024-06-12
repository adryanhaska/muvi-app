package com.example.muvi_app.data.pref

data class UserModel(
    val email: String,
    val token: String,
    val id: String,
    val isLogin: Boolean = false
)