package com.blogspot.soyamr.notforgotagain.model.db.tables

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    fun getAll(): LiveData<List<Note>>

    @Query("UPDATE note set done = 1 where id = :id")
    fun setDone(id: Long)

    @Query("delete from note where id = :id")
    fun deleteNote(id: Long)

    @Query("select * from note where id = :id")
    fun getNote(id: Long): Note

    @Insert()
    fun insertNote(note: List<Note>)

    @Delete
    fun delete(note: Note)

    @Query(
        "SELECT category.id as cId, category.name as cName , priority.id as pId," +
                " priority.name as pName, priority.color as pColor, note.id as nId," +
                " note.title as nTitle, note.description as nDescription, note.done as nDone " +
                ",note.deadline as nDeadline , note.created as nCreated from category, note," +
                " priority where note.parentCategoryId=category.id and note.parentPriorityId=priority.id " +
                "and note.parentCategoryId=:categoryId order by category.name"
    )
    fun getFullNoteDataRelatedToCategory(categoryId: Long): List<FullNoteData>

    @Query(
        "SELECT category.id as cId, category.name as cName , priority.id as pId, priority.name as pName, priority.color as pColor, note.id as nId, note.title as nTitle, note.description as nDescription, note.done as nDone ,note.deadline as nDeadline , note.created as nCreated from category, note, priority where note.parentCategoryId=category.id and note.parentPriorityId=priority.id and note.id=:currentNoteId"
    )
    fun getFullNoteData(currentNoteId: Long): FullNoteData


    @Query("Select * from note where isSavedToApi = 0")
    fun getUnSubmittedNotes(): List<Note>

    @Query("DELETE FROM Note")
    fun deleteAll()
}