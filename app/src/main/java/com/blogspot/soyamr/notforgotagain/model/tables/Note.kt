package com.blogspot.soyamr.notforgotagain.model.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.blogspot.soyamr.notforgotagain.model.NoteBoss
import java.sql.Date

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("uid"),
            childColumns = arrayOf("uid"),
            onDelete = ForeignKey.CASCADE),
        ForeignKey(
            entity = Category::class,
            parentColumns = arrayOf("cid"),
            childColumns = arrayOf("cid"),
            onDelete = ForeignKey.CASCADE),
        ForeignKey(
            entity = Priority::class,
            parentColumns = arrayOf("pid"),
            childColumns = arrayOf("pid"),
            onDelete = ForeignKey.NO_ACTION)
    ]
)
data class Note(
    @ColumnInfo val title: String?,
    @ColumnInfo val description: String?,
    @ColumnInfo val date: String?,
    @ColumnInfo val isDone: Boolean = false,
    @ColumnInfo val uid: Long,
    @ColumnInfo val cid: Long,
    @ColumnInfo val pid: Long,
    @PrimaryKey(autoGenerate = true) val nid: Long = 0
)