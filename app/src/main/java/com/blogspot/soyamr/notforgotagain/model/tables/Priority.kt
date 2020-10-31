package com.blogspot.soyamr.notforgotagain.model.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Priority(
    @ColumnInfo val name: String?,
    @ColumnInfo val color: Int?,
    @PrimaryKey val pid: Long = 0
)