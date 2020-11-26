package com.blogspot.soyamr.notforgotagain.model.db.tables

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category order by id")
    fun getAll(): List<Category>

    @Query("SELECT * FROM category WHERE id = :id")
    fun getCategory(id: Long): Category

    @Insert
    fun insertCategory(category: List<Category>)

    @Query("Select * from category where isSavedToApi = 0")
    fun getUnSubmittedCategories(): List<Category>

    @Query("DELETE FROM category where id != -1 ")
    fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM category WHERE id = :id)")
    fun isRowExist(id : Long) : Boolean
}