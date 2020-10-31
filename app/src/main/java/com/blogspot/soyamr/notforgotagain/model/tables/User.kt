package com.blogspot.soyamr.notforgotagain.model.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @ColumnInfo val name: String?,
    @ColumnInfo val email: String?,
    @ColumnInfo val password: String?,
    @ColumnInfo val signedIn: Boolean = false,
    @PrimaryKey(autoGenerate = true) val uid: Long = 0
)