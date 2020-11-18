package com.blogspot.soyamr.notforgotagain.model.db.tables

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PriorityDao {
    @Query("SELECT * FROM priority order by id")
    fun getAll(): List<Priority>

    @Query("SELECT * FROM priority WHERE id = :id")
    fun getPriority(id: Long): Priority

    @Insert
    fun insertPriority(vararg priority: Priority)

}