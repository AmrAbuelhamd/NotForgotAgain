package com.blogspot.soyamr.notforgotagain.model.net.pojo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val name: String,
    val id: Int,
    @SerialName("api_token")
    val apiToken: String
)