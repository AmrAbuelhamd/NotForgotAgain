package com.blogspot.soyamr.notforgotagain.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT uid FROM user WHERE signedIn = 1")
    fun getSignedInUser(): Int

    @Query("UPDATE user set signedIn = 0 where uid = :id")
    fun signOutUser(id: Int)

    @Query("UPDATE user set signedIn = 1 where uid = :id")
    fun signInUser(id: Int)

    @Query("SELECT uid FROM user WHERE email like :email AND password like :password")
    fun doWeHaveSuchUser(email: String, password: String): Int

    @Insert
    fun insertUser(user: User)

    @Delete
    fun delete(user: User)

//    @Query(
//        "SELECT * FROM user WHERE first_name LIKE :first AND " +
//                "last_name LIKE :last LIMIT 1"
//    )
//    fun findByName(first: String, last: String): User
}