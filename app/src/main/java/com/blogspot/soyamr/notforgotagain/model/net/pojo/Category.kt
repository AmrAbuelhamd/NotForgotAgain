package com.blogspot.soyamr.notforgotagain.model.net.pojo

import com.blogspot.soyamr.notforgotagain.model.db.tables.Category
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Long,
    val name: String
) {
    fun toDataBaseCategory() = Category(id, name)
}