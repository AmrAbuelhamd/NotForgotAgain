package com.blogspot.soyamr.notforgotagain.model.db.tables

import androidx.room.*

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category order by id")
    fun getAll(): List<Category>

    @Query("SELECT * FROM category WHERE id = :id")
    fun getCategory(id: Long): Category

    @Insert
    fun insertCategory(vararg category: Category)

}