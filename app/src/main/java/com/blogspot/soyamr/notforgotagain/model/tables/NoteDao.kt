package com.blogspot.soyamr.notforgotagain.model.tables

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface NoteDao {
    @Query("SELECT * FROM note where uid = :uid order by cid")
    fun getAll(uid: Long): List<Note>

    @Query("UPDATE note set isDone = 1 where nid = :nid")
    fun setDone(nid: Long)

    @Query("UPDATE note set isDone = 0 where nid = :nid")
    fun setUnDone(nid: Long)

    @Query("delete from note where nid = :nid")
    fun deleteNote(nid: Long)

    @Query("select * from note where nid = :nid")
    fun getNote(nid: Long): Note

    @Insert()
    fun insertNote(note: Note)

    @Delete
    fun delete(note: Note)

}