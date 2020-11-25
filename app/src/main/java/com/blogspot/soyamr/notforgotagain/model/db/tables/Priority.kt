package com.blogspot.soyamr.notforgotagain.model.db.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.blogspot.soyamr.notforgotagain.domain.Priority

@Entity
data class Priority(
    @PrimaryKey val id: Long,
    val name: String,
    val color: String
) {
    fun toDomain() = Priority(name, id)
}