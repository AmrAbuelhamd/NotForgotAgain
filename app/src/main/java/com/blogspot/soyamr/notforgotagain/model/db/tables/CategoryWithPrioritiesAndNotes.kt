package com.blogspot.soyamr.notforgotagain.model.db.tables

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithPrioritiesAndNotes(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "parentCategoryId"
    )
    val notes: List<Note>
)