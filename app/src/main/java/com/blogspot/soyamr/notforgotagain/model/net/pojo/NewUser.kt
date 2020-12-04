package com.blogspot.soyamr.notforgotagain.model.net.pojo

import kotlinx.serialization.Serializable

@Serializable
data class NewUser(
    val email: String,
    val name: String,
    val password: String
)