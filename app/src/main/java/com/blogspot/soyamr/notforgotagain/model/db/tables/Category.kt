package com.blogspot.soyamr.notforgotagain.model.db.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.blogspot.soyamr.notforgotagain.domain.Category

@Entity
data class Category(
    @PrimaryKey var id: Long,
    val name: String
) {
    fun toDomain() = Category(name, id)
}