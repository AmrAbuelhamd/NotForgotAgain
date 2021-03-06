package com.blogspot.soyamr.notforgotagain.model.db.tables

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.blogspot.soyamr.notforgotagain.model.net.pojo.NewTask
import com.blogspot.soyamr.notforgotagain.model.net.pojo.UpdatedTask


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("parentCategoryId"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Priority::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("parentPriorityId"),
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class Note(
    @PrimaryKey val id: Long,
    val title: String,
    val description: String?,
    val done: Boolean,
    val deadline: Long?,
    val created: Long?,
    val parentCategoryId: Long,
    val parentPriorityId: Long,
    val isSavedToApi: Boolean? = true
) {
    fun toUpdatedTask(
        title: String = this.title,
        description: String? = this.description,
        done: Int = if (this.done) 1 else 0,
        deadline: Long? = this.deadline,
        parentCategoryId: Long = this.parentCategoryId,
        parentPriorityId: Long = this.parentPriorityId,
    ) = UpdatedTask(
        title,
        description,
        done,
        deadline,
        parentCategoryId,
        parentPriorityId
    )

    fun toNetPojoTask() = NewTask(
        title,
        description,
        if (this.done) 1 else 0,
        deadline,
        parentCategoryId,
        parentPriorityId
    )
}