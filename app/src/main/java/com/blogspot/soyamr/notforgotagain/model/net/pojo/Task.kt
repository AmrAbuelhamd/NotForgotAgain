package com.blogspot.soyamr.notforgotagain.model.net.pojo

import kotlinx.serialization.Serializable
import com.blogspot.soyamr.notforgotagain.model.db.tables.Category as dbCategory
import com.blogspot.soyamr.notforgotagain.model.db.tables.Note as dbNote
import com.blogspot.soyamr.notforgotagain.model.db.tables.Priority as dbPriority

@Serializable
data class Task(
    val id: Long,
    val title: String,
    val description: String?,
    val done: Int = 0,
    val deadline: Long?,
    val category: Category,
    val priority: Priority,
    val created: Long?
) {
    fun toDataBaseNote(isSavedToApi: Boolean = true) =
        dbNote(
            id,
            title,
            description,
            done != 0,
            deadline,
            created,
            category.id,
            priority.id,
            isSavedToApi
        )

    fun toDataBaseCategory() = dbCategory(category.id, category.name)
    fun toDataBasePriority() = dbPriority(priority.id, priority.name, priority.color)
}