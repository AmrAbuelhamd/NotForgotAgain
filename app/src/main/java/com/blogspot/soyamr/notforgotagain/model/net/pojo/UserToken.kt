package com.blogspot.soyamr.notforgotagain.model.net.pojo;

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserToken(
    @SerialName("api_token")
    val apiToken: String
)
