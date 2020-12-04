package com.blogspot.soyamr.notforgotagain.model.net.pojo

import com.blogspot.soyamr.notforgotagain.model.db.tables.Priority

import kotlinx.serialization.Serializable

@Serializable
data class Priority(
    val id: Long,
    val name: String,
    val color: String
) {
    fun toDataBasePriority() = Priority(id, name, color)
}