package com.blogspot.soyamr.notforgotagain.model.net.pojo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdatedTask(
    val title: String,
    val description: String?,
    val done: Int,
    val deadline: Long?,
    @SerialName("category_id")
    val categoryId: Long,
    @SerialName("priority_id")
    val priorityId: Long
)
