package com.blogspot.soyamr.notforgotagain.model.net.pojo

import kotlinx.serialization.Serializable

@Serializable
data class Priority(
    val id: Long,
    val name: String,
    val color: String
)