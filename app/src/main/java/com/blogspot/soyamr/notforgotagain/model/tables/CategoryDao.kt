package com.blogspot.soyamr.notforgotagain.model.tables

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category")
    fun getAll(): List<Category>

    @Query("SELECT * FROM category WHERE cid = :cid")
    fun getCategory(cid: Long): Category

    @Query("delete from category WHERE cid = :cid")
    fun deleteCategory(cid: Long)

    @Insert
    fun insertCategory(category: Category)


    @Query("select cid from category where cid = (select max (cid) from category)")
    fun getBiggestId(): Long


    @Delete
    fun delete(category: Category)

    @Query("select cid from category where name like :categorySpinnerText")
    fun getCategoryId(categorySpinnerText: String): Long
}