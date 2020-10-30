package com.blogspot.soyamr.notforgotagain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "email") val email: String?,
    @ColumnInfo(name = "password") val password: String?,
    @ColumnInfo(name = "signedIn") val signedIn: Boolean = false,
    @PrimaryKey(autoGenerate = true) val uid: Long = 0
)