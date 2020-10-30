package com.blogspot.soyamr.notforgotagain.model

import android.content.Context


object NoteRepository {

    private lateinit var userDao: UserDao

    operator fun invoke(context: Context): NoteRepository {
        userDao = NotesDataBase.getDatabase(context).userDao()
        return this
    }

    fun getSignedInUser() = userDao.getSignedInUser()

    fun doWeHaveSuchUser(email: String, password: String) =
        userDao.doWeHaveSuchUser(email, password)

    fun signMeIn(uId: Long) {
        userDao.signInUser(uId)
    }

    fun signMeOut(currentUserId: Long) {
        userDao.signOutUser(currentUserId)
    }

}