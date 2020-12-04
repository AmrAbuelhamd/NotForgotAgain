package com.blogspot.soyamr.notforgotagain.model.db.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.blogspot.soyamr.notforgotagain.domain.Category
import com.blogspot.soyamr.notforgotagain.model.net.pojo.NewCategory

@Entity
data class Category(
    @PrimaryKey var id: Long,
    val name: String,
    val isSavedToApi: Boolean? = true
) {
    fun toDomain() = Category(name, id)

    fun toNetPojo() = NewCategory(name)
}