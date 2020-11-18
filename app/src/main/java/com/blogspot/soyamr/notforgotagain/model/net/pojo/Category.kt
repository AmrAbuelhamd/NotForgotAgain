package com.blogspot.soyamr.notforgotagain.model.net.pojo

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Long,
    val name: String
)