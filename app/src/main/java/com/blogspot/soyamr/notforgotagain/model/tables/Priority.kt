package com.blogspot.soyamr.notforgotagain.model.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.blogspot.soyamr.notforgotagain.domain.Priority

@Entity
data class Priority(
    @PrimaryKey val id: Long,
    val name: String,
    val color: Int
) {
    fun toDomain() = Priority(name, id)
}