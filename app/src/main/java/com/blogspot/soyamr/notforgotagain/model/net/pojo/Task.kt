package com.blogspot.soyamr.notforgotagain.model.net.pojo

import kotlinx.serialization.Serializable

@Serializable
data class Task(
    val id: Long,
    val title: String,
    val description: String?,
    val done: Int = 0,
    val deadline: Long?,
    val category: Category?,
    val priority: Priority?,
    val created: Long?
)