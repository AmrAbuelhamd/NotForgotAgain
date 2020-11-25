package com.blogspot.soyamr.notforgotagain.model.db.tables

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    foreignKeys = [
        ForeignKey(
        entity = Category::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("parentCategoryId"),
        onDelete = ForeignKey.CASCADE),
        ForeignKey(
        entity = Priority::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("parentPriorityId"),
        onDelete = ForeignKey.CASCADE),
    ]
)
data class Note(
    @PrimaryKey val id: Long,
    val title: String,
    val description: String?,
    val done: Boolean = false,
    val deadline: Long?,
    val created: Long?,
    val parentCategoryId: Long,
    val parentPriorityId: Long,
    val isSavedToApi:Boolean? = true
)