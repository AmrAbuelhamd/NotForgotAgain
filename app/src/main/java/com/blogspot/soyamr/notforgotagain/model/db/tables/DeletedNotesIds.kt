package com.blogspot.soyamr.notforgotagain.model.db.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DeletedNotesIds(@PrimaryKey val id: Long)