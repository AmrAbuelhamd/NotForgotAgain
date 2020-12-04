package com.blogspot.soyamr.notforgotagain.model.net.pojo

import kotlinx.serialization.Serializable

@Serializable
data class DeletedTaskResponse(
    val message: String,
    val task_id: String
)