package com.blogspot.soyamr.notforgotagain.model.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @ColumnInfo val name: String?,
    @PrimaryKey(autoGenerate = true) var cid: Long = 0
)