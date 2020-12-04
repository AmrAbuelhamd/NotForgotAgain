package com.blogspot.soyamr.notforgotagain.model.db.tables

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface DeletedNotesIdsDao {

    @Query("select * from deletednotesids")
    fun getAll(): List<DeletedNotesIds>

    @Insert
    fun insert(deletedNotesIds: DeletedNotesIds)

    @Delete
    fun delete(deletedNotesIds: DeletedNotesIds)

    @Query("DELETE FROM deletednotesids")
    fun deleteAll()
}