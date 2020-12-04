package com.blogspot.soyamr.notforgotagain.model.net.pojo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewTask(
    val title: String,
    val description: String?,
    val done: Int=0,
    val deadline: Long?,
    @SerialName("category_id")
    val categoryId: Long,
    @SerialName("priority_id")
    val priorityId: Long
){
    override fun toString(): String {
        return "NewTask(title='$title', description=$description, done=$done, deadline=$deadline, categoryId=$categoryId, priorityId=$priorityId)"
    }
}
