package com.blogspot.soyamr.notforgotagain.model.tables

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PriorityDao {
    @Query("SELECT * FROM priority")
    fun getAll(): List<Priority>

    @Query("SELECT * FROM priority WHERE pid = :pid")
    fun getPriority(pid: Long): Priority

    @Insert()
    fun insertPriority(vararg priority: Priority)

    @Query("select pid from priority where name like :prioritySpinnerText")
    fun getPriorityId(prioritySpinnerText: String): Long
}