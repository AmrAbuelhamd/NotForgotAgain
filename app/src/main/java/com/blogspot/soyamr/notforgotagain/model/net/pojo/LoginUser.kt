package com.blogspot.soyamr.notforgotagain.model.net.pojo

import kotlinx.serialization.Serializable

@Serializable
data class LoginUser(
    val email: String,
    val password: String
)